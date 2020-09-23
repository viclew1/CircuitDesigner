package fr.lewon.circuit.designer.model.road.impl.tool

import fr.lewon.circuit.designer.model.road.Obstacle
import fr.lewon.circuit.designer.model.road.RoadElement
import fr.lewon.circuit.designer.model.road.RoadElementType

class StartRoad : RoadElement("Start road", RoadElementType.START) {
    override fun generateObstacles(): List<Obstacle> {
        return listOf(
            generateStraightObstacle(0.2, 0.5, 0.2, 0.0),
            generateStraightObstacle(0.8, 0.5, 0.8, 0.0),
            *generateCurveObstacle(0.5, 0.5, Math.PI, Math.PI, 0.3, 0.3, 10).toTypedArray()
        )
    }
}