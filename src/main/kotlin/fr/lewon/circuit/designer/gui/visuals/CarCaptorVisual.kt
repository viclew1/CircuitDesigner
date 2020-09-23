package fr.lewon.circuit.designer.gui.visuals

import fr.lewon.circuit.designer.nn.CarCaptor
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import javafx.scene.transform.Transform

class CarCaptorVisual(private val tileSz: Double, private val carCaptor: CarCaptor): Visual() {

    private val originalLine = Line(0.0, 0.0, carCaptor.maxLength, 0.0)

    private val hitboxLine = Line()
    private val visualLine = Line()

    override fun updateVisual() {
        visualLine.endX = (originalLine.endX) * tileSz
        visualLine.endY = (originalLine.endY) * tileSz
        val angle = 180.0 * (carCaptor.angle + carCaptor.car.heading) / Math.PI
        visualLine.transforms.clear()
        visualLine.transforms.add(Transform.rotate(angle, 0.0, 0.0))
        visualLine.transforms.add(Transform.translate(carCaptor.car.position.x * tileSz, carCaptor.car.position.y * tileSz))
    }

    override fun updateHitbox() {
        hitboxLine.endX = originalLine.endX
        hitboxLine.endY = originalLine.endY
        val angle = 180.0 * (carCaptor.angle + carCaptor.car.heading) / Math.PI
        hitboxLine.transforms.clear()
        hitboxLine.transforms.add(Transform.rotate(angle, 0.0, 0.0))
        hitboxLine.transforms.add(Transform.translate(carCaptor.car.position.x, carCaptor.car.position.y))
    }

    override fun render(gc: GraphicsContext) {
        gc.stroke = Color.BLACK
        gc.lineWidth = 1.0
        gc.strokeLine(visualLine.startX, visualLine.startY, visualLine.endX, visualLine.endY)
    }

}