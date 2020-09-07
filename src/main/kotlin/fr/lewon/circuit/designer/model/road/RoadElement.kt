package fr.lewon.circuit.designer.model.road

import javafx.scene.paint.Color

abstract class RoadElement(
    val name: String,
    val width: Int,
    val height: Int,
    val obstacles: List<Obstacle>,
    val color: Color = Color.BLACK
)