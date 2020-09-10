package fr.lewon.circuit.designer.model.road.impl.classic

import fr.lewon.circuit.designer.model.road.Obstacle
import fr.lewon.circuit.designer.model.road.RoadElement

class BottleNeckRoad : RoadElement(
    "Bottleneck road", 1.0, 1.0, listOf(
        Obstacle(0.2, 0.0, 0.2, 0.2),
        Obstacle(0.2, 0.8, 0.2, 1.0),
        Obstacle(0.8, 0.0, 0.8, 0.2),
        Obstacle(0.8, 0.8, 0.8, 1.0),
        Obstacle(0.2, 0.2, 0.35, 0.5),
        Obstacle(0.2, 0.8, 0.35, 0.5),
        Obstacle(0.8, 0.2, 0.65, 0.5),
        Obstacle(0.8, 0.8, 0.65, 0.5)
    )
)