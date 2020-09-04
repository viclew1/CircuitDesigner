package fr.lewon.circuit.designer.gui

import fr.lewon.circuit.designer.model.Circuit
import fr.lewon.circuit.designer.model.road.impl.TurnLeftRoad
import javafx.geometry.Insets
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import javafx.scene.shape.Line

class CircuitPane(val circuit: Circuit) : Pane() {

    init {
        style = "-fx-border-color:black;"
        padding = Insets(5.0)
        val road = TurnLeftRoad()
        for (obstacle in road.obstacles) {
            children.add(Line(obstacle.xFrom.toDouble(), obstacle.yFrom.toDouble(), obstacle.xTo.toDouble(), obstacle.yTo.toDouble()))
        }
    }

}