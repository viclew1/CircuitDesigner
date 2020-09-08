package fr.lewon.circuit.designer.model.road

import kotlin.math.cos
import kotlin.math.sin

class Obstacle(var xFrom: Double, var yFrom: Double, var xTo: Double, var yTo: Double) {

    companion object {
        fun generateCurve(
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
    }

}