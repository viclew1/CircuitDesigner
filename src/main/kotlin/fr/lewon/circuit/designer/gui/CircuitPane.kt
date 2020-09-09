package fr.lewon.circuit.designer.gui

import fr.lewon.circuit.designer.model.Circuit
import javafx.scene.layout.Pane
import javafx.scene.shape.Line

class CircuitPane(private val circuit: Circuit): Pane() {

    var tileSz = 32.0
    val minSize = 16.0
    val maxSize = 96.0

    init {
        for (row in 0 until circuit.size) {
            for (col in 0 until circuit.size) {
                circuit.getElement(row, col)?.let { road ->
                    for (obstacle in road.obstacles) {
                        children.add(
                            Line().also {
                                it.startX = tileSz * col + tileSz * obstacle.xFrom
                                it.startY = tileSz * row + tileSz * obstacle.yFrom
                                it.endX = tileSz * col + tileSz * obstacle.xTo
                                it.endX = tileSz * row + tileSz * obstacle.y    To
                                it.strokeWidth = 3.0
                                it.stroke = road.color
                            }
                        )
                    }
                }
            }
        }
    }

}