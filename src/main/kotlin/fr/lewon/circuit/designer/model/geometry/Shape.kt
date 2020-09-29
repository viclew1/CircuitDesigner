package fr.lewon.circuit.designer.model.geometry

abstract class Shape(var points: List<Vector>) {

    fun rotateAround(rx: Double, ry: Double, angle: Double) {
        points = points.map { it.getRotatedVector(rx, ry, angle) }
    }

    fun translate(dx: Double, dy: Double) {
        points = points.map { it.getVectorSum(Vector(dx, dy)) }
    }

    abstract fun copy(): Shape

}