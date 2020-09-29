package fr.lewon.circuit.designer.gui.visuals

import fr.lewon.circuit.designer.model.geometry.Vector
import fr.lewon.circuit.designer.model.road.RoadElement
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.shape.Line

class RoadModel(
    private val row: Int,
    private val col: Int,
    private val road: RoadElement,
    private val obstacleSz: Double,
    private val tileSz: Double
) : VisualModel() {

    val hitboxLines = ArrayList<fr.lewon.circuit.designer.model.geometry.Line>()
    private val visualLines = ArrayList<Line>()

    init {
        road.obstacles.forEach {
            val dx = col * obstacleSz
            val dy = row * obstacleSz
            val from = Vector(it.xFrom * obstacleSz, it.yFrom * obstacleSz)
                .getVectorSum(Vector(dx, dy))
            val to = Vector(it.xTo * obstacleSz, it.yTo * obstacleSz)
                .getVectorSum(Vector(dx, dy))
            hitboxLines.add(fr.lewon.circuit.designer.model.geometry.Line(from, to))
            visualLines.add(Line(from.x * tileSz, from.y * tileSz, to.x * tileSz, to.y * tileSz))
        }
    }

    override fun updateVisual() {

    }

    override fun updateHitbox() {

    }

    override fun render(gc: GraphicsContext) {
        gc.stroke = road.type.color
        gc.lineWidth = 3.0
        visualLines.forEach { drawLine(gc, it) }
        gc.lineWidth = 1.0
        gc.stroke = Color.BLACK
    }

}