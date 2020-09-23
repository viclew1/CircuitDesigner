package fr.lewon.circuit.designer.gui

import fr.lewon.circuit.designer.model.Circuit
import fr.lewon.circuit.designer.model.geometry.Point
import fr.lewon.circuit.designer.model.road.RoadElementType
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.VBox
import javafx.stage.Modality
import javafx.stage.Stage
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.max
import kotlin.math.min
import kotlin.system.exitProcess

class CircuitDesignerController : Initializable {


    @FXML
    private lateinit var roadElementsScrollPane: ScrollPane

    @FXML
    private lateinit var toolsPane: VBox

    @FXML
    private lateinit var circuitsTabPane: TabPane

    private lateinit var dragStartPoint: Point

    private val roadElementListPane =
        RoadElementListPane { circuitsTabPane.selectionModel.selectedItem.content as CircuitEditorPane }
    private val buttons = listOf(
        ButtonDescriptor("Rotate left", "rotate_left.png")
        { (circuitsTabPane.selectionModel.selectedItem.content as CircuitEditorPane).rotateLeft() },
        ButtonDescriptor("Rotate right", "rotate_right.png")
        { (circuitsTabPane.selectionModel.selectedItem.content as CircuitEditorPane).rotateRight() },
        ButtonDescriptor("Export", "export.png")
        { (circuitsTabPane.selectionModel.selectedItem.content as CircuitEditorPane).exportCircuit() },
        ButtonDescriptor("Remove", "remove.png")
        { (circuitsTabPane.selectionModel.selectedItem.content as CircuitEditorPane).removeRoad() },
        ButtonDescriptor("Test circuit", "test_circuit.png")
        { openTest() }
    )

    private var newTabCpt = 1

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        if (circuitsTabPane.tabs.isEmpty()) {
            newCircuit()
        }
        roadElementsScrollPane.content = roadElementListPane

        val tileSz = toolsPane.minWidth - toolsPane.insets.left - toolsPane.insets.right
        for (btnDesc in buttons) {
            val btn = Button()
            btn.setOnAction { btnDesc.action.invoke() }
            Tooltip.install(btn, Tooltip(btnDesc.name))
            btn.style = "-fx-background-image: url(${btnDesc.imagePath});" +
                    "-fx-background-size: ${tileSz * 4 / 5} ${tileSz * 4 / 5};" +
                    "-fx-background-repeat: no-repeat;" +
                    "-fx-background-position: center;"
            btn.minWidth = tileSz
            btn.minHeight = tileSz
            btn.maxWidth = tileSz
            btn.maxHeight = tileSz
            toolsPane.children.add(btn)
        }

        circuitsTabPane.setOnScroll {
            val circuitPane = circuitsTabPane.selectionModel.selectedItem.content as CircuitEditorPane
            if (it.deltaY > 0) {
                circuitPane.tileSz += 8
            } else {
                circuitPane.tileSz -= 8
            }
            circuitPane.tileSz = max(circuitPane.tileSz, circuitPane.minSize)
            circuitPane.tileSz = min(circuitPane.tileSz, circuitPane.maxSize)
            adaptTranslate(circuitPane)
            circuitPane.updateVisual()
        }
        circuitsTabPane.setOnMousePressed {
            dragStartPoint = Point(it.sceneX, it.sceneY)
        }
        circuitsTabPane.setOnMouseDragged {
            val circuitPane = circuitsTabPane.selectionModel.selectedItem.content as CircuitEditorPane
            circuitPane.translate(
                circuitPane.dx + it.sceneX - dragStartPoint.x,
                circuitPane.dy + it.sceneY - dragStartPoint.y
            )
            dragStartPoint = Point(it.sceneX, it.sceneY)
            adaptTranslate(circuitPane)
        }
    }

    private fun adaptTranslate(circuitEditorPane: CircuitEditorPane) {
        val minTranslateX = min(0.0, circuitsTabPane.width - circuitEditorPane.getRealWidth() - 10)
        val minTranslateY = min(0.0, circuitsTabPane.height - circuitEditorPane.getRealHeight() - 10)
        val maxTranslateX = max(0.0, circuitsTabPane.width - circuitEditorPane.getRealWidth() - 10)
        val maxTranslateY = max(0.0, circuitsTabPane.height - circuitEditorPane.getRealHeight() - 10)
        val dx = max(minTranslateX, min(maxTranslateX, circuitEditorPane.dx))
        val dy = max(minTranslateY, min(maxTranslateY, circuitEditorPane.dy))
        circuitEditorPane.translate(dx, dy)
    }

    fun newCircuit() {
        val newTab = generateCircuitTab()
        circuitsTabPane.tabs.add(newTab)
        circuitsTabPane.selectionModel.select(newTab)
    }

    private fun generateCircuitTab(): Tab {
        val tab = Tab("Untitled ${newTabCpt++}")
        tab.setOnClosed { onCircuitClosed() }
        val circuitPane = CircuitEditorPane(Circuit(16))
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

    private fun openTest() {
        val currentCircuitEditorPane = circuitsTabPane.selectionModel.selectedItem.content as CircuitEditorPane
        val circuit = currentCircuitEditorPane.circuit
        validateCircuit(circuit)
            .takeIf { it.isNotEmpty() }
            ?.let {
                displayError("Circuit validation", it)
                return
            }
        val loader = FXMLLoader(javaClass.getResource("/scenes/circuit_scene.fxml"))
        val root = loader.load() as VBox
        val controller = loader.getController() as CircuitTestController
        controller.initCircuit(circuit)
        val stage = Stage()
        stage.initModality(Modality.WINDOW_MODAL)
        stage.initOwner(circuitsTabPane.scene.window as Stage)
        stage.scene = Scene(root)
        stage.show()
        controller.circuitCanvas.requestFocus()
    }

    private fun validateCircuit(circuit: Circuit): List<String> {
        val errors = ArrayList<String>()
        val allElements = circuit.getAllElements()
        val starts = allElements.filter { it.type == RoadElementType.START }
        val finishes = allElements.filter { it.type == RoadElementType.FINISH }
        val laps = allElements.filter { it.type == RoadElementType.LAP }

        if (starts.isEmpty() && laps.isEmpty()) {
            errors.add("A circuit needs a start")
        }
        if (starts.size + laps.size > 1) {
            errors.add("A circuit can't have more than one start")
        }
        if (finishes.isEmpty() && laps.isEmpty()) {
            errors.add("A circuit needs a finish line")
        }
        if (finishes.size + laps.size > 1) {
            errors.add("A circuit can't have more than one finish line")
        }
        return errors
    }

    private fun displayError(title: String, errors: List<String>) {
        val alert = Alert(Alert.AlertType.ERROR)
        alert.title = title
        alert.headerText = null
        alert.initOwner(circuitsTabPane.scene.window as Stage)
        alert.contentText = errors.joinToString("\n")
        alert.showAndWait()
    }

}