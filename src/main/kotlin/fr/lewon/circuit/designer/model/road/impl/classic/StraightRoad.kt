package fr.lewon.circuit.designer.model.road.impl.classic

import fr.lewon.circuit.designer.model.road.Obstacle
import fr.lewon.circuit.designer.model.road.RoadElement

class StraightRoad : RoadElement("Straight road") {
    override fun generateObstacles(): List<Obstacle> {
        return listOf(
            generateStraightObstacle(0.2, 0.0, 0.2, 1.0),
            generateStraightObstacle(0.8, 0.0, 0.8, 1.0)
        )
    }
}