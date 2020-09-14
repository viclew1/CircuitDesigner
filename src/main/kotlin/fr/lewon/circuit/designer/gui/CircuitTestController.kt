package fr.lewon.circuit.designer.gui

import fr.lewon.circuit.designer.model.Circuit
import javafx.fxml.FXML
import javafx.scene.layout.AnchorPane

class CircuitTestController {

    @FXML
    private lateinit var mainPane: AnchorPane

    lateinit var circuitPane: CircuitPane

    fun initCircuit(circuit: Circuit) {
        circuitPane = CircuitPane(circuit)
        mainPane.children.clear()
        AnchorPane.setBottomAnchor(circuitPane, 0.0)
        AnchorPane.setTopAnchor(circuitPane, 0.0)
        AnchorPane.setLeftAnchor(circuitPane, 0.0)
        AnchorPane.setRightAnchor(circuitPane, 0.0)
        mainPane.children.add(circuitPane)
    }

}