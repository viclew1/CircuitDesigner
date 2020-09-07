package fr.lewon.circuit.designer.gui

import fr.lewon.circuit.designer.model.Circuit
import fr.lewon.circuit.designer.model.geometry.Point
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.layout.VBox
import java.net.URL
import java.util.*
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
        RoadElementListPane { circuitsTabPane.selectionModel.selectedItem.content as CircuitPane }
    private val buttons = listOf(
        ButtonDescriptor("Rotate left", "rotate_left.png")
        { (circuitsTabPane.selectionModel.selectedItem.content as CircuitPane).rotateLeft() },
        ButtonDescriptor("Rotate right", "rotate_right.png")
        { (circuitsTabPane.selectionModel.selectedItem.content as CircuitPane).rotateRight() },
        ButtonDescriptor("Export", "export.png")
        { (circuitsTabPane.selectionModel.selectedItem.content as CircuitPane).exportCircuit() },
        ButtonDescriptor("Remove", "remove.png")
        { (circuitsTabPane.selectionModel.selectedItem.content as CircuitPane).removeRoad() }
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
            val circuitPane = circuitsTabPane.selectionModel.selectedItem.content as CircuitPane
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
            val circuitPane = circuitsTabPane.selectionModel.selectedItem.content as CircuitPane
            circuitPane.translate(
                circuitPane.dx + it.sceneX - dragStartPoint.x,
                circuitPane.dy + it.sceneY - dragStartPoint.y
            )
            dragStartPoint = Point(it.sceneX, it.sceneY)
            adaptTranslate(circuitPane)
        }
    }

    private fun adaptTranslate(circuitPane: CircuitPane) {
        val minTranslateX = min(0.0, circuitsTabPane.width - circuitPane.getRealWidth() - 10)
        val minTranslateY = min(0.0, circuitsTabPane.height - circuitPane.getRealHeight() - 10)
        val maxTranslateX = max(0.0, circuitsTabPane.width - circuitPane.getRealWidth() - 10)
        val maxTranslateY = max(0.0, circuitsTabPane.height - circuitPane.getRealHeight() - 10)
        val dx = max(minTranslateX, min(maxTranslateX, circuitPane.dx))
        val dy = max(minTranslateY, min(maxTranslateY, circuitPane.dy))
        circuitPane.translate(dx, dy)
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