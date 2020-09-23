package fr.lewon.circuit.designer.gui.visuals

import fr.lewon.circuit.designer.model.geometry.Vector
import fr.lewon.circuit.designer.model.road.Obstacle
import fr.lewon.circuit.designer.model.road.RoadElement
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.shape.Line

class RoadVisual(
    private val row: Int,
    private val col: Int,
    private val road: RoadElement,
    private val obstacleSz: Double,
    private val tileSz: Double): Visual() {

    val hitboxLines = ArrayList<Line>()
    val visualLines = ArrayList<Line>()

    init {
        road.obstacles.forEach {
            val dx = col * obstacleSz
            val dy = row * obstacleSz
            val from = Vector(it.xFrom * obstacleSz, it.yFrom * obstacleSz)
            val to = Vector(it.xTo * obstacleSz, it.yTo * obstacleSz)
            val line = Line()
            line.startX = dx + from.x
            line.startY = dy + from.y
            line.endX = dx + to.x
            line.endY = dy + to.y
            line.rotate = 180.0 * road.rotation / Math.PI
            hitboxLines.add(line)
            visualLines.add(Line().also { vl ->
                vl.startX = line.startX * tileSz
                vl.startY = line.startY * tileSz
                vl.endX = line.endX * tileSz
                vl.endY = line.endY * tileSz
                vl.rotate = line.rotate
            })
        }
    }

    override fun updateVisual() {
        visualLines.forEach { it.rotate += 0.01 }
    }

    override fun updateHitbox() {

    }

    override fun render(gc: GraphicsContext) {
        gc.stroke = road.type.color
        gc.lineWidth = 3.0
        visualLines.forEach {
            gc.strokeLine(it.startX, it.startY, it.endX, it.endY)
        }
        gc.lineWidth = 1.0
        gc.stroke = Color.BLACK
    }

}