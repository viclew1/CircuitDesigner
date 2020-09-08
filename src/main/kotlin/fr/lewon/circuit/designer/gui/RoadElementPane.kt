package fr.lewon.circuit.designer.gui

import fr.lewon.circuit.designer.model.road.RoadElement
import javafx.scene.layout.Pane
import javafx.scene.shape.Line

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
        roadElement?.rotate(rotation)
        updateVisual()
    }

    private fun updateVisual() {
        children.clear()
        roadElement?.let { element ->
            for (obstacle in element.obstacles) {
                children.add(
                    Line().also {
                        it.startXProperty().bind(widthProperty().multiply(obstacle.xFrom))
                        it.startYProperty().bind(heightProperty().multiply(obstacle.yFrom))
                        it.endXProperty().bind(widthProperty().multiply(obstacle.xTo))
                        it.endYProperty().bind(heightProperty().multiply(obstacle.yTo))
                        it.strokeWidth = 3.0
                        it.stroke = element.color
                    }
                )
            }
        }
    }

    fun rotateLeft() {
        roadElement?.let {
            rotation += Math.PI / 2
            if (rotation > Math.PI * 2) rotation -= Math.PI * 2
            it.rotate(Math.PI / 2)
            updateVisual()
        }
    }

    fun rotateRight() {
        roadElement?.let {
            rotation -= Math.PI / 2
            if (rotation < 0) rotation += Math.PI * 2
            it.rotate(-Math.PI / 2)
            updateVisual()
        }
    }

}