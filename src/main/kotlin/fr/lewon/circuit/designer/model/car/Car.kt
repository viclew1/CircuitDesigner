package fr.lewon.circuit.designer.model.car

import fr.lewon.circuit.designer.model.geometry.Vector
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class Car(private val mass: Double) {

    var angle = Math.PI/2
    var wheelAngle = angle
    private var enginePower = 0.0
    private var position = Vector(0.0, 0.0)
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
        val spdRound = BigDecimal(getSpeed()).setScale(2, RoundingMode.HALF_EVEN)
        val xRound = BigDecimal(position.x).setScale(2, RoundingMode.HALF_EVEN)
        val yRound = BigDecimal(position.y).setScale(2, RoundingMode.HALF_EVEN)
        println("speed: $spdRound ; x: $xRound ; y: $yRound")
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
        dragForce = velocityVector.getVectorMult(- PhysicsConstants.C_DRAG * getSpeed())
    }

    private fun updateRollingResistance() {
        rollingResistance = velocityVector.getVectorMult(- PhysicsConstants.C_RR)
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
    }

}
fun main() {
    val car = Car(800.0)
    println("-----")

    for (i in 0..10000) {
        car.updateAll(20000.0, 0.001)
    }

}
