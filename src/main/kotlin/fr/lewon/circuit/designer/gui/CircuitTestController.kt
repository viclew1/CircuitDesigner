package fr.lewon.circuit.designer.gui

import fr.lewon.Individual
import fr.lewon.SelectionProcessor
import fr.lewon.circuit.designer.model.Circuit
import fr.lewon.nn.impl.NeuralNetworkClassic
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.layout.AnchorPane

class CircuitTestController {

    @FXML
    private lateinit var mainPane: AnchorPane

    lateinit var circuitCanvas: CircuitCanvas

    fun initCircuit(circuit: Circuit) {
        circuitCanvas = CircuitCanvas(circuit)
        mainPane.children.clear()
        AnchorPane.setBottomAnchor(circuitCanvas, 0.0)
        AnchorPane.setTopAnchor(circuitCanvas, 0.0)
        AnchorPane.setLeftAnchor(circuitCanvas, 0.0)
        AnchorPane.setRightAnchor(circuitCanvas, 0.0)
        mainPane.children.add(circuitCanvas)

        val selectionProcessor = SelectionProcessor(circuitCanvas)
        val individuals = ArrayList<Individual>()
        for (i in 0 until 100) {
            individuals.add(NeuralNetworkClassic(8, 5))
        }
        Thread {
            val objectiveFitness = circuit.getAllElements().size * 10.0
            selectionProcessor.start(individuals, 1000, objectiveFitness, 0.1)
        }.start()
    }

}