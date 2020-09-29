package fr.lewon.circuit.designer.gui.visuals

import fr.lewon.circuit.designer.model.geometry.IntersectionUtil
import fr.lewon.circuit.designer.model.geometry.Vector
import fr.lewon.circuit.designer.nn.CarCaptor
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import javafx.scene.shape.Path
import javafx.scene.shape.Shape
import javafx.scene.transform.Transform
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

class CarCaptorModel(private val tileSz: Double, private val carCaptor: CarCaptor) : VisualModel() {

    var length = carCaptor.maxLength
        private set
    private var tempLength = carCaptor.maxLength

    lateinit var hitbox: fr.lewon.circuit.designer.model.geometry.Line
    private val visualLine = Line()

    init {
        updateHitbox()
        updateVisual()
    }

    override fun updateVisual() {
        visualLine.startX = hitbox.from.x * tileSz
        visualLine.startY = hitbox.from.y * tileSz
        visualLine.endX = hitbox.to.x * tileSz
        visualLine.endY = hitbox.to.y * tileSz
    }

    fun updateLength(obstacles: List<fr.lewon.circuit.designer.model.geometry.Line>) {
        tempLength = carCaptor.maxLength
        updateHitbox()
        if (!lineIntersectsAny(hitbox, obstacles)) {
            this.length = tempLength
            return
        }
        var lastIntersected = true
        var minus = true
        var delta = length
        for (i in 0..10) {
            val newLength = if (minus) {
                tempLength - delta / 2.0
            } else {
                tempLength + delta / 2.0
            }
            delta = abs(tempLength - newLength)
            tempLength = newLength
            updateHitbox()
            val newLastIntersected = lineIntersectsAny(hitbox, obstacles)
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

    override fun updateHitbox() {
        hitbox = fr.lewon.circuit.designer.model.geometry.Line(Vector(0.0, 0.0), Vector(tempLength, 0.0)).also {
            it.rotateAround(0.0, 0.0, carCaptor.angle + carCaptor.car.heading)
            it.translate(carCaptor.car.position.x, carCaptor.car.position.y)
        }
    }

    override fun render(gc: GraphicsContext) {
        gc.stroke = Color(Color.WHITE.red, Color.WHITE.green, Color.WHITE.blue, 0.2)
        gc.lineWidth = 1.0
        drawLine(gc, visualLine)
        gc.stroke = Color.BLACK
    }

}