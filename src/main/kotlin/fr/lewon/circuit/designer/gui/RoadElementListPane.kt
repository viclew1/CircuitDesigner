package fr.lewon.circuit.designer.gui

import fr.lewon.circuit.designer.model.road.RoadElementCategory
import javafx.geometry.Orientation
import javafx.scene.control.Label
import javafx.scene.control.Separator
import javafx.scene.control.Tooltip
import javafx.scene.layout.GridPane

class RoadElementListPane(val circuitPaneSupplier: () -> CircuitPane) : GridPane() {

    init {
        style = "-fx-background-color: #454c52;"
        hgap = 5.0
        vgap = 5.0
        initElements(3)
    }

    private fun initElements(colCount: Int) {
        children.clear()
        var row = 0
        var col: Int
        for (category in RoadElementCategory.values()) {
            col = 0
            add(Separator(Orientation.HORIZONTAL), col, row++, 3, 1)
            add(Label(category.categoryName), col, row++, 3, 1)
            add(Separator(Orientation.HORIZONTAL), col, row++, 3, 1)
            for (elementBuilder in category.roadElementBuilders) {
                val element = elementBuilder.invoke()
                add(RoadElementPane(row, col).also {
                    it.updateRoadElement(element)
                    it.prefWidthProperty().bind(widthProperty().divide(colCount))
                    it.prefHeightProperty().bind(it.prefWidthProperty())
                    it.minHeightProperty().bind(it.prefWidthProperty())
                    Tooltip.install(it, Tooltip(element.name))
                    it.setOnMouseClicked { circuitPaneSupplier.invoke().updateRoadElement(elementBuilder.invoke()) }
                }, col, row, 1, 1)
                col++
                if (col == colCount) {
                    col = 0
                    row++
                }
            }
            row++
        }
    }

}