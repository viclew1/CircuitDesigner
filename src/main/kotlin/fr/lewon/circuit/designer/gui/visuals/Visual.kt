package fr.lewon.circuit.designer.gui.visuals

import javafx.scene.canvas.GraphicsContext
import javafx.scene.shape.Line
import javafx.scene.shape.Rectangle

abstract class Visual {

    abstract fun updateVisual()

    abstract fun updateHitbox()

    abstract fun render(gc: GraphicsContext)

    protected fun drawLine(gc: GraphicsContext, line: Line) {
        val from = line.localToParent(line.startX, line.startY)
        val to = line.localToParent(line.endX, line.endY)
        gc.strokeLine(from.x, from.y, to.x, to.y)
    }

    protected fun drawRect(gc: GraphicsContext, rectangle: Rectangle) {
        val points = rectToPoints(rectangle)
        gc.strokePolygon(points.first, points.second, points.first.size)
    }

    protected fun fillRect(gc: GraphicsContext, rectangle: Rectangle) {
        val points = rectToPoints(rectangle)
        gc.fillPolygon(points.first, points.second, points.first.size)
    }

    private fun rectToPoints(rectangle: Rectangle): Pair<DoubleArray, DoubleArray> {
        val points = listOf(
            Pair(rectangle.x, rectangle.y),
            Pair(rectangle.x + rectangle.width, rectangle.y),
            Pair(rectangle.x + rectangle.width, rectangle.y + rectangle.height),
            Pair(rectangle.x, rectangle.y + rectangle.height)
        ).map { p -> rectangle.localToParent(p.first, p.second) }
        return Pair(
            points.map { it.x }.toDoubleArray(),
            points.map { it.y }.toDoubleArray()
        )
    }

}