package fr.lewon.circuit.designer.model.geometry

import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class Vector(x: Double = 0.0, y:Double = 0.0): Point(x, y) {

    fun getRotatedVector(rx: Double, ry: Double, angle: Double): Vector {
        val newX = cos(angle) * (x - rx) + sin(angle) * (y - ry) + rx
        val newY = -sin(angle) * (x - rx) + cos(angle) * (y - ry) + ry
        return Vector(newX, newY)
    }

    fun getVectorMult(m: Double): Vector {
        return Vector(x * m, y * m)
    }

    fun getVectorSum(v: Vector): Vector {
        return Vector(x + v.x, y + v.y)
    }

    fun getOppositeVector(): Vector {
        return Vector(-x, -y)
    }

    fun length(): Double {
        return sqrt(x * x + y * y)
    }

}