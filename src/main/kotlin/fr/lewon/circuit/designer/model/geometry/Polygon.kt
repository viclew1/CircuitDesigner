package fr.lewon.circuit.designer.model.geometry

class Polygon(points: List<Vector> = ArrayList()) : Shape(points) {

    override fun copy(): Polygon {
        return Polygon(points)
    }

}