package fr.lewon.circuit.designer.model.road

import kotlin.math.cos
import kotlin.math.sin

class Obstacle(val xFrom: Int, val yFrom: Int, val xTo: Int, val yTo: Int) {

    companion object {
        fun generateCurve(
                centerX: Int,
                centerY: Int,
                radianFrom: Double,
                radianSize: Double,
                radiusX: Int,
                radiusY: Int,
                arraySize: Int
        ): List<Obstacle> {
            val obstacles = ArrayList<Obstacle>()
            var xFrom = radiusX.toDouble() * cos(radianFrom)
            var yFrom = -radiusY.toDouble() * sin(radianFrom)
            for (i in 1..arraySize) {
                val t: Double = radianFrom + radianSize * i.toDouble() / arraySize.toDouble()
                val x = radiusX.toDouble() * cos(t)
                val y = -radiusY.toDouble() * sin(t)
                obstacles.add(
                    Obstacle(
                        centerX + xFrom.toInt(),
                        centerY + yFrom.toInt(),
                        centerX + x.toInt(),
                        centerY + y.toInt()
                    )
                )
                xFrom = x
                yFrom = y
            }
            return obstacles
        }
    }

}