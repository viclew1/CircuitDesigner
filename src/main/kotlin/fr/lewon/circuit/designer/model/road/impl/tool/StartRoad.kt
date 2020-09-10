package fr.lewon.circuit.designer.model.road.impl.tool

import fr.lewon.circuit.designer.model.road.Obstacle
import fr.lewon.circuit.designer.model.road.RoadElement
import fr.lewon.circuit.designer.model.road.RoadElementType

class StartRoad : RoadElement(
    "Start road", 1.0, 1.0, listOf(
        Obstacle(0.2, 0.5, 0.2, 0.0),
        Obstacle(0.8, 0.5, 0.8, 0.0),
        *Obstacle.generateCurve(0.5, 0.5, Math.PI, Math.PI, 0.3, 0.3, 10).toTypedArray()
    ), RoadElementType.START
)