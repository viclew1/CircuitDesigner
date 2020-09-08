package fr.lewon.circuit.designer.model.road

import fr.lewon.circuit.designer.model.geometry.Point
import javafx.scene.paint.Color
import kotlin.math.cos
import kotlin.math.sin

abstract class RoadElement(
    val name: String,
    val width: Int,
    val height: Int,
    val obstacles: List<Obstacle>,
    val color: Color = Color.BLACK
) {
    fun rotate(angle: Double) {
        obstacles.forEach {
            val newFrom = rotatePointAround(it.xFrom, it.yFrom, 0.5, 0.5, angle)
            val newTo = rotatePointAround(it.xTo, it.yTo, 0.5, 0.5, angle)
            it.xFrom = newFrom.x
            it.yFrom = newFrom.y
            it.xTo = newTo.x
            it.yTo = newTo.y
        }
    }

    private fun rotatePointAround(x: Double, y: Double, centerX: Double, centerY: Double, angle: Double): Point {
        val newX = cos(angle) * (x - centerX) + sin(angle) * (y - centerY) + centerX
        val newY = -sin(angle) * (x - centerX) + cos(angle) * (y - centerY) + centerY
        return Point(newX, newY)
    }
}