package fr.lewon.circuit.designer.model.road.impl.tool

import fr.lewon.circuit.designer.model.road.Obstacle
import fr.lewon.circuit.designer.model.road.RoadElement
import fr.lewon.circuit.designer.model.road.RoadElementType
import javafx.scene.paint.Color

class CheckpointRoad : RoadElement("Checkpoint road", RoadElementType.CHECKPOINT) {
    override fun generateObstacles(): List<Obstacle> {
        return listOf(
            generateStraightObstacle(0.2, 0.0, 0.2, 0.3),
            generateStraightObstacle(0.2, 0.7, 0.2, 1.0),
            generateStraightObstacle(0.8, 0.0, 0.8, 0.3),
            generateStraightObstacle(0.8, 0.7, 0.8, 1.0),
            *generateCurveObstacle(0.2, 0.5, -Math.PI / 2, Math.PI, 0.2, 0.2, 10).toTypedArray(),
            *generateCurveObstacle(0.8, 0.5, Math.PI / 2, Math.PI, 0.2, 0.2, 10).toTypedArray()
        )
    }
}