package fr.lewon.circuit.designer.model.road.impl.tool

import fr.lewon.circuit.designer.model.road.Obstacle
import fr.lewon.circuit.designer.model.road.RoadElement
import fr.lewon.circuit.designer.model.road.RoadElementType
import javafx.scene.paint.Color

class FinishRoad : RoadElement("Finish road", RoadElementType.FINISH) {
    override fun generateObstacles(): List<Obstacle> {
        return listOf(
            generateStraightObstacle(0.2, 0.5, 0.2, 1.0),
            generateStraightObstacle(0.8, 0.5, 0.8, 1.0),
            *generateCurveObstacle(0.5, 0.5, 0.0, Math.PI, 0.3, 0.3, 10).toTypedArray()
        )
    }
}