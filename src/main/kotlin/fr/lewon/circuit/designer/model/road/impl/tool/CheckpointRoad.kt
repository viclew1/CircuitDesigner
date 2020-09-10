package fr.lewon.circuit.designer.model.road.impl.tool

import fr.lewon.circuit.designer.model.road.Obstacle
import fr.lewon.circuit.designer.model.road.RoadElement
import fr.lewon.circuit.designer.model.road.RoadElementType
import javafx.scene.paint.Color

class CheckpointRoad : RoadElement(
    "Checkpoint road", 1.0, 1.0, listOf(
        Obstacle(0.2, 0.0, 0.2, 0.3),
        Obstacle(0.2, 0.7, 0.2, 1.0),
        Obstacle(0.8, 0.0, 0.8, 0.3),
        Obstacle(0.8, 0.7, 0.8, 1.0),
        *Obstacle.generateCurve(0.2, 0.5, -Math.PI / 2, Math.PI, 0.2, 0.2, 10).toTypedArray(),
        *Obstacle.generateCurve(0.8, 0.5, Math.PI / 2, Math.PI, 0.2, 0.2, 10).toTypedArray()
    ), RoadElementType.CHECKPOINT
)