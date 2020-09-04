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
            width: Int,
            height: Int,
            arraySize: Int
        ): List<Obstacle> {
            val obstacles = ArrayList<Obstacle>()
            var xFrom = width.toDouble() * cos(radianFrom)
            var yFrom = height.toDouble() * sin(radianFrom)
            for (i in 0 until arraySize) {
                val t: Double = radianFrom + radianSize / i.toDouble()
                val x = width.toDouble() * cos(t)
                val y = height.toDouble() * sin(t)
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