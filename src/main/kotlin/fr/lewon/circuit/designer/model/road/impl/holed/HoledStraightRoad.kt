package fr.lewon.circuit.designer.model.road.impl.holed

import fr.lewon.circuit.designer.model.road.Obstacle
import fr.lewon.circuit.designer.model.road.RoadElement

class HoledStraightRoad : RoadElement("Holed straight road") {
    override fun generateObstacles(): List<Obstacle> {
        return listOf(
            generateStraightObstacle(0.2, 0.0, 0.2, 1.0),
            generateStraightObstacle(0.8, 0.0, 0.8, 1.0),
            *generateCurveObstacle(0.5, 0.5, 0.0, 2 * Math.PI, 0.05, 0.05, 15).toTypedArray()
        )
    }
}