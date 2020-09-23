package fr.lewon.circuit.designer.model.geometry

import kotlin.math.sqrt

class Vector(x: Double = 0.0, y:Double = 0.0): Point(x, y) {

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