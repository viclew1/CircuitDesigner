package fr.lewon.circuit.designer.model.road.impl.classic

import fr.lewon.circuit.designer.model.road.Obstacle
import fr.lewon.circuit.designer.model.road.RoadElement

class SideCrunchedRoad : RoadElement(
    "Side crunched road", 1, 1, listOf(
        Obstacle(0.2, 0.0, 0.2, 0.2),
        Obstacle(0.2, 0.8, 0.2, 1.0),
        Obstacle(0.2, 0.2, 0.05, 0.3),
        Obstacle(0.05, 0.3, 0.05, 0.7),
        Obstacle(0.05, 0.7, 0.2, 0.8),
        Obstacle(0.8, 0.0, 0.8, 0.2),
        Obstacle(0.8, 0.8, 0.8, 1.0),
        Obstacle(0.8, 0.2, 0.4, 0.4),
        Obstacle(0.8, 0.8, 0.4, 0.6),
        Obstacle(0.4, 0.4, 0.4, 0.6)
    )
)