package fr.lewon.circuit.designer.model.road.impl.tool

import fr.lewon.circuit.designer.model.road.Obstacle
import fr.lewon.circuit.designer.model.road.RoadElement
import javafx.scene.paint.Color

object FinishRoad : RoadElement(
    "Finish road", 1, 1, listOf(
        Obstacle(0.2, 0.5, 0.2, 1.0),
        Obstacle(0.8, 0.5, 0.8, 1.0),
        *Obstacle.generateCurve(0.5, 0.5, 0.0, Math.PI, 0.3, 0.3, 10).toTypedArray()
    ), Color.RED
)