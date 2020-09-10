package fr.lewon.circuit.designer.model.road.impl.holed

import fr.lewon.circuit.designer.model.road.Obstacle
import fr.lewon.circuit.designer.model.road.RoadElement

class HoledStraightRoad : RoadElement(
    "Holed straight road", 1.0, 1.0, listOf(
        Obstacle(0.2, 0.0, 0.2, 1.0),
        Obstacle(0.8, 0.0, 0.8, 1.0),
        *Obstacle.generateCurve(0.5, 0.5, 0.0, 2 * Math.PI, 0.05, 0.05, 15).toTypedArray()
    )
)