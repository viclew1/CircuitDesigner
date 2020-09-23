package fr.lewon.circuit.designer.model.road.impl.classic

import fr.lewon.circuit.designer.model.road.Obstacle
import fr.lewon.circuit.designer.model.road.RoadElement

class SideCrunchedRoad : RoadElement("Side crunched road") {
    override fun generateObstacles(): List<Obstacle> {
        return listOf(
            generateStraightObstacle(0.2, 0.0, 0.2, 0.2),
            generateStraightObstacle(0.2, 0.8, 0.2, 1.0),
            generateStraightObstacle(0.2, 0.2, 0.05, 0.3),
            generateStraightObstacle(0.05, 0.3, 0.05, 0.7),
            generateStraightObstacle(0.05, 0.7, 0.2, 0.8),
            generateStraightObstacle(0.8, 0.0, 0.8, 0.2),
            generateStraightObstacle(0.8, 0.8, 0.8, 1.0),
            generateStraightObstacle(0.8, 0.2, 0.4, 0.4),
            generateStraightObstacle(0.8, 0.8, 0.4, 0.6),
            generateStraightObstacle(0.4, 0.4, 0.4, 0.6)
        )
    }
}