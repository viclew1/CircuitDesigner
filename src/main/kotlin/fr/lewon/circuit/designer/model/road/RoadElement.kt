package fr.lewon.circuit.designer.model.road

import fr.lewon.circuit.designer.model.geometry.Point
import kotlin.math.cos
import kotlin.math.sin

abstract class RoadElement(
    val name: String,
    val width: Double,
    val height: Double,
    val obstacles: List<Obstacle>,
    val type: RoadElementType = RoadElementType.STANDARD
) {
    var rotation: Double = 0.0

    fun rotate(angle: Double) {
        rotation += angle
        if (rotation < 0) {
            rotation += Math.PI * 2
        } else if (rotation > Math.PI * 2) {
            rotation -= Math.PI * 2
        }
        obstacles.forEach {
            val newFrom = rotatePointAround(it.xFrom, it.yFrom, angle)
            val newTo = rotatePointAround(it.xTo, it.yTo, angle)
            it.xFrom = newFrom.x
            it.yFrom = newFrom.y
            it.xTo = newTo.x
            it.yTo = newTo.y
        }
    }

    private fun rotatePointAround(x: Double, y: Double, angle: Double): Point {
        val newX = cos(angle) * (x - 0.5) + sin(angle) * (y - 0.5) + 0.5
        val newY = -sin(angle) * (x - 0.5) + cos(angle) * (y - 0.5) + 0.5
        return Point(newX, newY)
    }
}