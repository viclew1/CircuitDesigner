package fr.lewon.circuit.designer.gui

import fr.lewon.circuit.designer.model.Circuit
import fr.lewon.circuit.designer.model.road.RoadElement
import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.control.Tooltip
import javafx.scene.input.MouseEvent
import javafx.scene.layout.GridPane
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import kotlin.math.max
import kotlin.math.min

class CircuitPane(val circuit: Circuit) : GridPane() {

    private var hoveredTile: RoadElementPane? = null
    private var selectedTiles = ArrayList<RoadElementPane>()
    private var lastSelectedTile: RoadElementPane? = null

    private var lastShiftSelectedTiles = ArrayList<RoadElementPane>()

    private val roadElements: MutableList<MutableList<RoadElementPane>> = ArrayList()
    private val colors: MutableList<MutableList<Rectangle>> = ArrayList()

    private val buttons = listOf(
        ButtonDescriptor("Rotate left", "rotate_left.png") { selectedTiles.forEach { it.rotateLeft() } },
        ButtonDescriptor("Rotate right", "rotate_right.png") { selectedTiles.forEach { it.rotateRight() } },
        ButtonDescriptor("Remove", "remove.png") { selectedTiles.forEach { it.updateRoadElement(null) } }
    )

    init {
        style = "-fx-border-color:black; -fx-background-color: #454c52;"
        padding = Insets(5.0)

        for (row in 0 until 16) {
            row.takeIf { it < buttons.size }?.let {
                Button().also {
                    it.setOnAction { buttons[row].action.invoke() }
                    Tooltip.install(it, Tooltip(buttons[row].name))
                    it.style = "-fx-background-image: url(${buttons[row].imagePath}); -fx-background-size: cover;"
                    it.minWidthProperty().bind(widthProperty().divide(17))
                    it.minHeightProperty().bind(heightProperty().divide(16))
                    it.maxWidthProperty().bind(widthProperty().divide(17))
                    it.maxHeightProperty().bind(heightProperty().divide(16))
                    add(it, 0, row)
                }
            }
        }
        for (row in 0 until 16) {
            roadElements.add(ArrayList())
            colors.add(ArrayList())
            for (col in 0 until 16) {
                val stack = StackPane()
                val roadElementPane = RoadElementPane(row, col)
                val colorRectangle = Rectangle()
                colorRectangle.fill = Color.TRANSPARENT
                roadElements[row].add(roadElementPane)
                colors[row].add(colorRectangle)

                stack.children.add(colorRectangle)
                stack.children.add(roadElementPane)

                stack.setOnMouseMoved { onHover(it, row, col) }
                stack.setOnMouseClicked { onClick(it, row, col) }

                roadElementPane.prefWidthProperty().bind(stack.widthProperty())
                roadElementPane.prefHeightProperty().bind(stack.heightProperty())
                colorRectangle.widthProperty().bind(stack.widthProperty())
                colorRectangle.heightProperty().bind(stack.heightProperty())

                stack.prefWidthProperty().bind(widthProperty().divide(17))
                stack.prefHeightProperty().bind(heightProperty().divide(16))
                add(stack, col + 1, row)
            }
        }
    }

    private fun resetColor(tile: RoadElementPane) {
        val color = when {
            lastSelectedTile == tile -> {
                val selected = lastSelectedTile
                if (selectedTiles.contains(selected))
                    Color.BLUE.darker()
                else Color.WHITE
            }
            selectedTiles.contains(tile) -> {
                Color.BLUE
            }
            hoveredTile == tile -> {
                Color.CYAN
            }
            else -> {
                Color.TRANSPARENT
            }
        }
        colors[tile.row][tile.col].fill = color
    }

    private fun resetColor(row: Int, col: Int) {
        resetColor(roadElements[row][col])
    }

    private fun onClick(e: MouseEvent, row: Int, col: Int) {
        if (e.isShiftDown && lastSelectedTile != null) {
            selectUntil(row, col)
        } else if (e.isControlDown && lastSelectedTile != null) {
            selectAlso(row, col)
        } else {
            selectNewTile(row, col)
        }
    }

    private fun selectAlso(row: Int, col: Int) {
        val selected = roadElements[row][col]
        val remove = selectedTiles.contains(selected)
        updateLastSelectedTile(selected)
        if (remove) unSelectTile(selected) else addToSelected(selected)
    }

    private fun selectUntil(row: Int, col: Int) {
        val remove = !selectedTiles.contains(lastSelectedTile)
        for (t in lastShiftSelectedTiles) {
            if (remove) addToSelected(t) else unSelectTile(t)
        }
        lastShiftSelectedTiles.clear()
        lastSelectedTile?.let {
            for (r in min(it.row, row)..max(it.row, row)) {
                for (c in min(it.col, col)..max(it.col, col)) {
                    val tile = roadElements[r][c]
                    lastShiftSelectedTiles.add(tile)
                    if (remove) unSelectTile(tile) else addToSelected(tile)
                }
            }
        }
        if (remove) {
            val oldLastSelectedTile = lastSelectedTile
            oldLastSelectedTile?.let { resetColor(it.row, it.col) }
        }
    }

    private fun selectNewTile(row: Int, col: Int) {
        clearSelectedTiles()
        updateLastSelectedTile(row, col)
    }

    private fun clearSelectedTiles() {
        val oldSelectedTiles = ArrayList(selectedTiles)
        selectedTiles.clear()
        oldSelectedTiles.forEach {
            resetColor(it.row, it.col)
        }
    }

    private fun addToSelected(tile: RoadElementPane) {
        if (!selectedTiles.contains(tile)) {
            selectedTiles.add(tile)
            resetColor(tile)
        }
    }

    private fun unSelectTile(tile: RoadElementPane) {
        selectedTiles.remove(tile)
        resetColor(tile)
    }

    private fun unSelectTile(row: Int, col: Int) {
        unSelectTile(roadElements[row][col])
    }

    private fun updateLastSelectedTile(newTile: RoadElementPane) {
        val oldLastSelectedTile = lastSelectedTile
        lastShiftSelectedTiles.clear()
        lastSelectedTile = newTile
        oldLastSelectedTile?.let { resetColor(it.row, it.col) }
        lastSelectedTile?.let {
            addToSelected(it)
            resetColor(it.row, it.col)
        }
    }

    private fun updateLastSelectedTile(row: Int, col: Int) {
        updateLastSelectedTile(roadElements[row][col])
    }

    private fun onHover(e: MouseEvent, row: Int, col: Int) {
        val oldHoveredTile = hoveredTile
        hoveredTile = roadElements[row][col]
        oldHoveredTile?.let { resetColor(it.row, it.col) }
        hoveredTile?.let { resetColor(it.row, it.col) }
    }

    fun updateRoadElement(element: RoadElement) {
        for (tile in selectedTiles) {
            tile.updateRoadElement(element)
        }
    }

}