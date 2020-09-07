package fr.lewon.circuit.designer.gui

import fr.lewon.circuit.designer.model.geometry.Point
import fr.lewon.circuit.designer.model.road.RoadElement
import javafx.scene.layout.Pane
import javafx.scene.shape.Line
import kotlin.math.cos
import kotlin.math.sin

class RoadElementPane(val row: Int, val col: Int) : Pane() {

    private var roadElement: RoadElement? = null
    private var rotation = 0.0

    init {
        style = "-fx-border-color: gray; -fx-border-width: 1"
    }

    fun updateRoadElement(roadElement: RoadElement?) {
        if (this.roadElement == null || roadElement == null) {
            rotation = 0.0
        }
        this.roadElement = roadElement
        updateVisual()
    }

    private fun updateVisual() {
        children.clear()
        roadElement?.let { element ->
            for (obstacle in element.obstacles) {
                val transposedFrom = rotatePointAround(obstacle.xFrom, obstacle.yFrom, 0.5, 0.5, rotation)
                val transposedTo = rotatePointAround(obstacle.xTo, obstacle.yTo, 0.5, 0.5, rotation)
                children.add(
                    Line().also {
                        it.startXProperty().bind(widthProperty().multiply(transposedFrom.x))
                        it.startYProperty().bind(heightProperty().multiply(transposedFrom.y))
                        it.endXProperty().bind(widthProperty().multiply(transposedTo.x))
                        it.endYProperty().bind(heightProperty().multiply(transposedTo.y))
                        it.strokeWidth = 3.0
                        it.stroke = element.color
                    }
                )
            }
        }
    }

    private fun rotatePointAround(x: Double, y: Double, centerX: Double, centerY: Double, angle: Double): Point {
        val newX = cos(angle) * (x - centerX) + sin(angle) * (y - centerY) + centerX
        val newY = -sin(angle) * (x - centerX) + cos(angle) * (y - centerY) + centerY
        return Point(newX, newY)
    }

    fun rotateLeft() {
        rotation += Math.PI / 2
        if (rotation > Math.PI * 2) rotation -= Math.PI * 2
        updateVisual()
    }

    fun rotateRight() {
        rotation -= Math.PI / 2
        if (rotation < 0) rotation += Math.PI * 2
        updateVisual()
    }

}