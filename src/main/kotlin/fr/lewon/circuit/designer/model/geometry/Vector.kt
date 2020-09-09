package fr.lewon.circuit.designer.model.geometry

class Vector(x: Double, y:Double): Point(x, y) {

    fun getVectorMult(m: Double): Vector {
        return Vector(x * m, y * m)
    }

    fun getVectorSum(v: Vector): Vector {
        return Vector(x + v.x, y + v.y)
    }

    fun getOppositeVector(): Vector {
        return Vector(-x, -y)
    }

}