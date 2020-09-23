package fr.lewon.circuit.designer.gui.visuals

import fr.lewon.circuit.designer.nn.CarCaptor
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import javafx.scene.transform.Transform

class CarCaptorVisual(private val tileSz: Double, private val carCaptor: CarCaptor) : Visual() {

    private val originalLine = Line(0.0, 0.0, carCaptor.maxLength, 0.0)

    private val hitboxLine = Line()
    private val visualLine = Line()

    override fun updateVisual() {
        visualLine.endX = (originalLine.endX) * tileSz
        val angle = 180.0 * (carCaptor.angle + carCaptor.car.heading) / Math.PI
        visualLine.transforms.clear()
        visualLine.transforms.add(
            Transform.rotate(angle, carCaptor.car.position.x * tileSz, carCaptor.car.position.y * tileSz)
        )
        visualLine.transforms.add(
            Transform.translate(carCaptor.car.position.x * tileSz, carCaptor.car.position.y * tileSz)
        )
    }

    override fun updateHitbox() {
        hitboxLine.endX = originalLine.endX
        val angle = 180.0 * (carCaptor.angle + carCaptor.car.heading) / Math.PI
        hitboxLine.transforms.clear()
        hitboxLine.transforms.add(Transform.rotate(angle, carCaptor.car.position.x, carCaptor.car.position.y))
        hitboxLine.transforms.add(Transform.translate(carCaptor.car.position.x, carCaptor.car.position.y))
    }

    override fun render(gc: GraphicsContext) {
        gc.stroke = Color(Color.WHITE.red, Color.WHITE.green, Color.WHITE.blue, 0.2)
        gc.lineWidth = 1.0
        drawLine(gc, visualLine)
        gc.stroke = Color.BLACK
    }

}