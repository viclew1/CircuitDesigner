package fr.lewon.circuit.designer.gui.visuals

import fr.lewon.circuit.designer.model.car.Car
import fr.lewon.circuit.designer.model.geometry.Polygon
import fr.lewon.circuit.designer.model.geometry.Vector
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import javafx.scene.shape.Rectangle
import javafx.scene.transform.Transform
import kotlin.math.min

class CarModel(private val tileSz: Double, private val car: Car) : VisualModel() {

    private val originalBounds: Rectangle
    private val originalRearLeftTire: Rectangle
    private val originalRearRightTire: Rectangle
    private val originalFrontLeftTire: Rectangle
    private val originalFrontRightTire: Rectangle
    private val originalGravityCenterH: Line
    private val originalGravityCenterV: Line
    private val originalHitbox: Polygon

    var hitbox = Polygon()
    private val bounds = Rectangle()
    private val rearLeftTire = Rectangle()
    private val rearRightTire = Rectangle()
    private val frontLeftTire = Rectangle()
    private val frontRightTire = Rectangle()
    private val gravityCenterH = Line()
    private val gravityCenterV = Line()

    private val detailsShapes = listOf(
        bounds, rearLeftTire, rearRightTire, frontLeftTire, frontRightTire, gravityCenterH, gravityCenterV
    )

    init {
        val cfg = car.carConfig
        originalBounds = Rectangle(
            -cfg.cgToRear,
            -cfg.halfWidth,
            cfg.cgToRear + cfg.cgToFront,
            cfg.halfWidth * 2.0
        )

        originalHitbox = Polygon(listOf(
            Vector(-cfg.cgToRear, -cfg.halfWidth),
            Vector(cfg.cgToFront, -cfg.halfWidth),
            Vector(cfg.cgToFront, cfg.halfWidth),
            Vector(-cfg.cgToRear, cfg.halfWidth),
            Vector(-cfg.cgToRear, -cfg.halfWidth)
        ))

        bounds.style = "-fx-stroke-width: 2;"
        bounds.stroke = cfg.color
        bounds.fill = Color(cfg.color.red, cfg.color.green, cfg.color.blue, 0.6)

        originalRearLeftTire = Rectangle(-cfg.wheelRadius, -cfg.wheelWidth / 2.0, cfg.wheelRadius * 2.0, cfg.wheelWidth)
        originalRearRightTire =
            Rectangle(-cfg.wheelRadius, -cfg.wheelWidth / 2.0, cfg.wheelRadius * 2.0, cfg.wheelWidth)
        originalFrontLeftTire =
            Rectangle(-cfg.wheelRadius, -cfg.wheelWidth / 2.0, cfg.wheelRadius * 2.0, cfg.wheelWidth)
        originalFrontRightTire =
            Rectangle(-cfg.wheelRadius, -cfg.wheelWidth / 2.0, cfg.wheelRadius * 2.0, cfg.wheelWidth)
        val gCenterCrossSz = min(cfg.cgToRearAxle, min(cfg.cgToFrontAxle, cfg.halfWidth)) / 5.0
        originalGravityCenterH = Line(-gCenterCrossSz, 0.0, gCenterCrossSz, 0.0)
        originalGravityCenterV = Line(0.0, -gCenterCrossSz, 0.0, gCenterCrossSz)
        gravityCenterH.strokeWidth = 3.0
        gravityCenterV.strokeWidth = 3.0
    }

    override fun updateHitbox() {
        hitbox = originalHitbox.copy()
        hitbox.translate(car.position.x, car.position.y)
        hitbox.rotateAround(car.position.x, car.position.y, -car.heading)
    }

    override fun render(gc: GraphicsContext) {
        gc.fill = bounds.fill
        fillRect(gc, bounds)
        gc.lineWidth = 3.0
        gc.stroke = car.carConfig.color
        drawRect(gc, bounds)
        gc.stroke = Color.BLACK
        drawLine(gc, gravityCenterH)
        drawLine(gc, gravityCenterV)
        gc.lineWidth = 1.0
        gc.fill = Color.BLACK
        fillRect(gc, rearLeftTire)
        fillRect(gc, rearRightTire)
        fillRect(gc, frontLeftTire)
        fillRect(gc, frontRightTire)
    }

    override fun updateVisual() {
        val cfg = car.carConfig
        val headingAngle = car.heading * 180.0 / Math.PI
        val wheelAngle = car.steerAngle * 180.0 / Math.PI

        detailsShapes.forEach {
            it.transforms.clear()
            it.transforms.add(Transform.rotate(headingAngle, car.position.x * tileSz, car.position.y * tileSz))
        }
        updateRectangle(bounds, originalBounds, tileSz)
        updateRectangle(
            rearLeftTire,
            originalRearLeftTire,
            tileSz,
            -cfg.cgToRearAxle,
            -cfg.wheelWidth / 2.0 - cfg.halfWidth
        )
        updateRectangle(
            rearRightTire,
            originalRearRightTire,
            tileSz,
            -cfg.cgToRearAxle,
            cfg.wheelWidth / 2.0 + cfg.halfWidth
        )
        updateRectangle(
            frontLeftTire,
            originalFrontLeftTire,
            tileSz,
            cfg.cgToRearAxle,
            -cfg.wheelWidth / 2.0 - cfg.halfWidth
        )
        updateRectangle(
            frontRightTire,
            originalFrontRightTire,
            tileSz,
            cfg.cgToRearAxle,
            cfg.wheelWidth / 2.0 + cfg.halfWidth
        )
        updateLine(gravityCenterH, originalGravityCenterH, tileSz)
        updateLine(gravityCenterV, originalGravityCenterV, tileSz)

        frontLeftTire.transforms.add(
            Transform.rotate(
                wheelAngle,
                frontLeftTire.x + frontLeftTire.width / 2.0,
                frontLeftTire.y + frontLeftTire.height / 2.0
            )
        )
        frontRightTire.transforms.add(
            Transform.rotate(
                wheelAngle,
                frontRightTire.x + frontRightTire.width / 2.0,
                frontRightTire.y + frontRightTire.height / 2.0
            )
        )
    }

    private fun updateLine(toUpdate: Line, original: Line, scale: Double, dx: Double = 0.0, dy: Double = 0.0) {
        toUpdate.startX = original.startX * scale
        toUpdate.startY = original.startY * scale
        toUpdate.endX = original.endX * scale
        toUpdate.endY = original.endY * scale
        toUpdate.transforms.add(Transform.translate((car.position.x + dx) * scale, (car.position.y + dy) * scale))
    }

    private fun updateRectangle(
        toUpdate: Rectangle,
        original: Rectangle,
        scale: Double,
        dx: Double = 0.0,
        dy: Double = 0.0
    ) {
        toUpdate.x = original.x * scale
        toUpdate.y = original.y * scale
        toUpdate.width = original.width * scale
        toUpdate.height = original.height * scale
        toUpdate.transforms.add(Transform.translate((car.position.x + dx) * scale, (car.position.y + dy) * scale))
    }

}