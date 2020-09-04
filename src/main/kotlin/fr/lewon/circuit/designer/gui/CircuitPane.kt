package fr.lewon.circuit.designer.gui

import fr.lewon.circuit.designer.model.Circuit
import javafx.geometry.Insets
import javafx.scene.layout.GridPane

class CircuitPane(val circuit: Circuit) : GridPane() {

    init {
        style = "-fx-border-color:black;"
        padding = Insets(5.0)
    }

}