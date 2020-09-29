package fr.lewon.circuit.designer.model.road

import fr.lewon.circuit.designer.model.geometry.Vector
import kotlin.math.cos
import kotlin.math.sin

abstract class RoadElement(val name: String, val type: RoadElementType = RoadElementType.STANDARD) {

    val obstacles: List<Obstacle> = generateObstacles()
    var rotation: Double = 0.0

    protected abstract fun generateObstacles(): List<Obstacle>

    protected fun generateStraightObstacle(xFrom: Double, yFrom: Double, xTo: Double, yTo: Double): Obstacle {
        return Obstacle(xFrom, yFrom, xTo, yTo)
    }

    protected fun generateCurveObstacle(
        centerX: Double,
        centerY: Double,
        radianFrom: Double,
        radianSize: Double,
        radiusX: Double,
        radiusY: Double,
        arraySize: Int
    ): List<Obstacle> {
        val obstacles = ArrayList<Obstacle>()
        var xFrom = radiusX * cos(radianFrom)
        var yFrom = -radiusY * sin(radianFrom)
        for (i in 1..arraySize) {
            val t: Double = radianFrom + radianSize * i.toDouble() / arraySize.toDouble()
            val x = radiusX * cos(t)
            val y = -radiusY * sin(t)
            obstacles.add(
                Obstacle(
                    centerX + xFrom,
                    centerY + yFrom,
                    centerX + x,
                    centerY + y
                )
            )
            xFrom = x
            yFrom = y
        }
        return obstacles
    }

    fun rotate(angle: Double) {
        rotation += angle
        if (rotation < 0) {
            rotation += Math.PI * 2
        } else if (rotation > Math.PI * 2) {
            rotation -= Math.PI * 2
        }
        obstacles.forEach {
            val rotatedFrom = Vector(it.xFrom, it.yFrom).getRotatedVector(0.5, 0.5, angle)
            val rotatedTo = Vector(it.xTo, it.yTo).getRotatedVector(0.5, 0.5, angle)
            it.xFrom = rotatedFrom.x
            it.yFrom = rotatedFrom.y
            it.xTo = rotatedTo.x
            it.yTo = rotatedTo.y
        }
    }

}