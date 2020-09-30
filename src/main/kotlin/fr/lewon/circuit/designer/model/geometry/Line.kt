package fr.lewon.circuit.designer.model.geometry

class Line(from: Vector, to: Vector): Shape(listOf(from, to)) {

    override fun copy(): Line {
        return Line(from(), to())
    }

    fun from(): Vector {
        return points[0]
    }

    fun to(): Vector {
        return points[1]
    }
}
