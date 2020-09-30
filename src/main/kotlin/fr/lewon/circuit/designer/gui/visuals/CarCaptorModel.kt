package fr.lewon.circuit.designer.gui.visuals

import fr.lewon.circuit.designer.model.geometry.IntersectionUtil
import fr.lewon.circuit.designer.model.geometry.Vector
import fr.lewon.circuit.designer.nn.CarCaptor
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import kotlin.math.abs

class CarCaptorModel(private val tileSz: Double, private val carCaptor: CarCaptor) : VisualModel() {

    val maxLength = carCaptor.maxLength
    var length = maxLength
        private set

    lateinit var hitbox: fr.lewon.circuit.designer.model.geometry.Line
    private val visualLine = Line()

    init {
        updateHitbox()
        updateVisual()
    }

    override fun updateVisual() {
        visualLine.startX = hitbox.from().x * tileSz
        visualLine.startY = hitbox.from().y * tileSz
        visualLine.endX = hitbox.to().x * tileSz
        visualLine.endY = hitbox.to().y * tileSz
    }

    fun updateLength(obstacles: List<fr.lewon.circuit.designer.model.geometry.Line>) {
        var tempLength = maxLength
        var tempHitbox = buildHitbox(tempLength)
        if (!lineIntersectsAny(tempHitbox, obstacles)) {
            this.length = tempLength
            return
        }
        var lastIntersected = true
        var minus = true
        var delta = tempLength
        for (i in 0..10) {
            val newLength = if (minus) {
                tempLength - delta / 2.0
            } else {
                tempLength + delta / 2.0
            }
            delta = abs(tempLength - newLength)
            tempLength = newLength
            tempHitbox = buildHitbox(tempLength)
            val newLastIntersected = lineIntersectsAny(tempHitbox, obstacles)
            if (newLastIntersected != lastIntersected) {
                minus = !minus
            }
            lastIntersected = newLastIntersected
        }
        this.length = tempLength
    }

    private fun lineIntersectsAny(
        line: fr.lewon.circuit.designer.model.geometry.Line,
        obstacles: List<fr.lewon.circuit.designer.model.geometry.Line>
    ): Boolean {
        for (obs in obstacles) {
            if (IntersectionUtil.intersects(line, obs)) {
                return true
            }
        }
        return false
    }

    private fun buildHitbox(l: Double): fr.lewon.circuit.designer.model.geometry.Line {
        return fr.lewon.circuit.designer.model.geometry.Line(
            Vector(carCaptor.car.position.x, carCaptor.car.position.y),
            Vector(carCaptor.car.position.x + l, carCaptor.car.position.y)
        ).also {
            val angle = -carCaptor.angle - carCaptor.car.heading
            it.rotateAround(carCaptor.car.position.x, carCaptor.car.position.y, angle)
        }
    }

    override fun updateHitbox() {
        hitbox = buildHitbox(length)
    }

    override fun render(gc: GraphicsContext) {
        gc.stroke = Color(1.0, 1.0, 1.0, 0.2)
        gc.lineWidth = 1.0
        drawLine(gc, visualLine)
        gc.stroke = Color.BLACK
    }

}