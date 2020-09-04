package fr.lewon.circuit.designer.model.road.impl

import fr.lewon.circuit.designer.model.road.Obstacle
import fr.lewon.circuit.designer.model.road.RoadElement

class StraightRoad : RoadElement(
    100, 100, listOf(
        Obstacle(20, 0, 20, 100),
        Obstacle(80, 0, 80, 100)
    )
) {
}