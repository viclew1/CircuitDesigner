package fr.lewon.circuit.designer.gui

import fr.lewon.circuit.designer.model.Circuit
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.scene.layout.Pane
import java.net.URL
import java.util.*
import kotlin.system.exitProcess

class CircuitDesignerController : Initializable {

    private var newTabCpt = 1

    @FXML
    private lateinit var circuitsTabPane: TabPane

    @FXML
    private lateinit var tabContentPane: Pane

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        if (circuitsTabPane.tabs.isEmpty()) {
            newCircuit()
        }
    }

    fun newCircuit() {
        val newTab = generateCircuitTab()
        circuitsTabPane.tabs.add(newTab)
        circuitsTabPane.selectionModel.select(newTab)
    }

    private fun generateCircuitTab(): Tab {
        val tab = Tab("Untitled ${newTabCpt++}")
        tab.setOnClosed { onCircuitClosed() }
        val circuitPane = CircuitPane(Circuit())
        tab.content = circuitPane
        return tab
    }

    fun openCircuit() {
        println("Open")
    }

    fun saveCircuit() {
        println("Save")
    }

    fun saveCircuitAs() {
        println("Save as")
    }

    fun exitApp() {
        exitProcess(0)
    }

    fun closeCircuit() {
        circuitsTabPane.tabs.remove(circuitsTabPane.selectionModel.selectedItem)
        onCircuitClosed()
    }

    private fun onCircuitClosed() {
        if (circuitsTabPane.tabs.isEmpty()) {
            newCircuit()
        }
    }

}