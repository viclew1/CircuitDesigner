package fr.lewon.circuit.designer.gui.visuals

import javafx.scene.canvas.GraphicsContext

abstract class Visual {

    abstract fun updateVisual()

    abstract fun updateHitbox()

    abstract fun render(gc: GraphicsContext)

}