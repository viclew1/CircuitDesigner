package fr.lewon.circuit.designer.model.car

import fr.lewon.circuit.designer.model.geometry.Point
import fr.lewon.circuit.designer.model.geometry.Vector
import javafx.scene.paint.Color
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class Car(private val mass: Double, private val frontSz: Double, private val sideSz: Double, val color: Color) {

    var angle = Math.PI / 2
    var wheelAngle = angle
    private var enginePower = 0.0
    private var position = Vector(0.0, 0.0)
    val initialPosFrontRight = Vector(frontSz / 2, -sideSz / 2)
    val initialPosFrontLeft = Vector(-frontSz / 2, -sideSz / 2)
    val initialPosBackRight = Vector(frontSz / 2, sideSz / 2)
    val initialPosBackLeft = Vector(-frontSz / 2, sideSz / 2)
    var posFrontRight = initialPosFrontRight
    var posFrontLeft = initialPosFrontLeft
    var posBackRight = initialPosBackRight
    var posBackLeft = initialPosBackLeft

    private var acceleration = Vector(0.0, 0.0)
    private var velocityVector = Vector(0.0, 0.0)
    private var tractionForce = Vector(0.0, 0.0)
    private var dragForce = Vector(0.0, 0.0)
    private var rollingResistance = Vector(0.0, 0.0)
    private var longitudinalForce = Vector(0.0, 0.0)

    fun updateAll(enginePower: Double, dtSec: Double) {
        this.enginePower = enginePower
        updateTraction()
        updateDrag()
        updateRollingResistance()
        updateLongitudinalForce()
        updateAcceleration()
        updateVelocity(dtSec)
        updatePosition(dtSec)
    }

    fun resetPosition(pos: Vector, angle: Double) {
        this.position = pos
        this.angle = angle
        this.wheelAngle = angle
        acceleration = Vector(0.0, 0.0)
        velocityVector = Vector(0.0, 0.0)
        tractionForce = Vector(0.0, 0.0)
        dragForce = Vector(0.0, 0.0)
        rollingResistance = Vector(0.0, 0.0)
        longitudinalForce = Vector(0.0, 0.0)
        enginePower = 0.0
        updatePosition(0.0)
    }

    private fun directionVector(): Vector {
        return Vector(cos(angle), -sin(angle))
    }

    private fun getSpeed(): Double {
        return sqrt(velocityVector.x * velocityVector.x + velocityVector.y * velocityVector.y)
    }

    private fun updateTraction() {
        tractionForce = directionVector().getVectorMult(enginePower)
    }

    private fun updateDrag() {
        dragForce = velocityVector.getVectorMult(-PhysicsConstants.C_DRAG * getSpeed())
    }

    private fun updateRollingResistance() {
        rollingResistance = velocityVector.getVectorMult(-PhysicsConstants.C_RR)
    }

    private fun updateLongitudinalForce() {
        longitudinalForce = tractionForce.getVectorSum(dragForce).getVectorSum(rollingResistance)
    }

    private fun updateAcceleration() {
        acceleration = longitudinalForce.getVectorMult(1.0 / mass)
    }

    private fun updateVelocity(dtSec: Double) {
        velocityVector = velocityVector.getVectorSum(acceleration.getVectorMult(dtSec))
    }

    private fun updatePosition(dtSec: Double) {
        position = position.getVectorSum(velocityVector.getVectorMult(dtSec))
        posFrontRight = rotateAround(
            Vector(position.x + initialPosFrontRight.x, position.y + initialPosFrontRight.y),
            position,
            angle
        )
        posFrontLeft = rotateAround(
            Vector(position.x + initialPosFrontLeft.x, position.y + initialPosFrontLeft.y),
            position,
            angle
        )
        posBackLeft = rotateAround(
            Vector(position.x + initialPosBackLeft.x, position.y + initialPosBackLeft.y),
            position,
            angle
        )
        posBackRight = rotateAround(
            Vector(position.x + initialPosBackRight.x, position.y + initialPosBackRight.y),
            position,
            angle
        )
    }

    private fun rotateAround(point: Point, center: Point, angle: Double): Vector {
        val newX = cos(angle) * (point.x - center.x) + sin(angle) * (point.y - center.y) + center.x
        val newY = -sin(angle) * (point.x - center.x) + cos(angle) * (point.y - center.y) + center.y
        return Vector(newX, newY)
    }

}