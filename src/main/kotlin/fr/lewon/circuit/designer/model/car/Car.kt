package fr.lewon.circuit.designer.model.car

import fr.lewon.circuit.designer.model.geometry.Point
import fr.lewon.circuit.designer.model.geometry.Vector
import javafx.scene.paint.Color
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class Car(private val mass: Double, frontSz: Double, sideSz: Double, val color: Color) {

    var angle = 0.0
    var wheelAngle = angle
    private var enginePower = 0.0
    private var position = Vector(0.0, 0.0)

    private val initialPosFrontRightWheel = Vector(sideSz / 3, frontSz / 2)
    private val initialPosFrontLeftWheel = Vector(sideSz / 3, - frontSz / 2)
    private val initialPosBackRightWheel = Vector(- sideSz / 3, frontSz / 2)
    private val initialPosBackLeftWheel = Vector(- sideSz / 3, - frontSz / 2)

    private val initialPosFrontRight = Vector(sideSz / 2, frontSz / 2)
    private val initialPosFrontLeft = Vector(sideSz / 2, - frontSz / 2)
    private val initialPosBackRight = Vector(- sideSz / 2, frontSz / 2)
    private val initialPosBackLeft = Vector(- sideSz / 2, - frontSz / 2)

    var posFrontRightWheel = initialPosFrontRightWheel
        private set
    var posFrontLeftWheel = initialPosFrontLeftWheel
        private set
    var posBackRightWheel = initialPosBackRightWheel
        private set
    var posBackLeftWheel = initialPosBackLeftWheel
        private set

    var posFrontRight = initialPosFrontRight
        private set
    var posFrontLeft = initialPosFrontLeft
        private set
    var posBackRight = initialPosBackRight
        private set
    var posBackLeft = initialPosBackLeft
        private set

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
        posFrontRight = rotateAround(position.getVectorSum(initialPosFrontRight), position, angle)
        posFrontLeft = rotateAround(position.getVectorSum(initialPosFrontLeft), position, angle)
        posBackLeft = rotateAround(position.getVectorSum(initialPosBackLeft), position, angle)
        posBackRight = rotateAround(position.getVectorSum(initialPosBackRight), position, angle)
        posFrontRightWheel = rotateAround(position.getVectorSum(initialPosFrontRightWheel), position, angle)
        posFrontLeftWheel = rotateAround(position.getVectorSum(initialPosFrontLeftWheel), position, angle)
        posBackLeftWheel = rotateAround(position.getVectorSum(initialPosBackLeftWheel), position, angle)
        posBackRightWheel = rotateAround(position.getVectorSum(initialPosBackRightWheel), position, angle)
    }

    private fun rotateAround(point: Point, center: Point, angle: Double): Vector {
        val newX = cos(angle) * (point.x - center.x) + sin(angle) * (point.y - center.y) + center.x
        val newY = -sin(angle) * (point.x - center.x) + cos(angle) * (point.y - center.y) + center.y
        return Vector(newX, newY)
    }

}