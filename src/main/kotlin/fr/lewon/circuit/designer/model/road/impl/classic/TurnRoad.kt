package fr.lewon.circuit.designer.model.road.impl.classic

import fr.lewon.circuit.designer.model.road.Obstacle
import fr.lewon.circuit.designer.model.road.RoadElement

class TurnRoad : RoadElement("Turn road") {
    override fun generateObstacles(): List<Obstacle> {
        return listOf(
            *generateCurveObstacle(0.0, 1.0, 0.0, Math.PI / 2, 0.2, 0.2, 10).toTypedArray(),
            *generateCurveObstacle(0.0, 1.0, 0.0, Math.PI / 2, 0.8, 0.8, 10).toTypedArray()
        )
    }
}