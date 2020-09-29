package fr.lewon.circuit.designer.model.geometry

class Line(val from: Vector, val to: Vector): Shape(listOf(from, to)) {

    override fun copy(): Line {
        return Line(points[0], points[1])
    }
}
