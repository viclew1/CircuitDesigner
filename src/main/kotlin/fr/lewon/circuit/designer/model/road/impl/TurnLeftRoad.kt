package fr.lewon.circuit.designer.model.road.impl

import fr.lewon.circuit.designer.model.road.Obstacle
import fr.lewon.circuit.designer.model.road.RoadElement

class TurnLeftRoad : RoadElement(
    100, 100, listOf(
        Obstacle(0, 80, 20, 100),
        *Obstacle.generateCurve(0, 100, Math.PI / 2, Math.PI / 2, 100, 100, 10).toTypedArray()
    )
)