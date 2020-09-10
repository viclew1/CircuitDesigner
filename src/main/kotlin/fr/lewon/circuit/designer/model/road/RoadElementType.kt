package fr.lewon.circuit.designer.model.road

import javafx.scene.paint.Color

enum class RoadElementType(val color: Color) {

    STANDARD(Color.BLACK),
    START(Color.GREEN),
    CHECKPOINT(Color.LIGHTBLUE),
    FINISH(Color.RED),
    LAP(Color.MAGENTA)

}