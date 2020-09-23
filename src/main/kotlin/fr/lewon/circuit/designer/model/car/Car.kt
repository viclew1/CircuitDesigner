package fr.lewon.circuit.designer.model.car

import fr.lewon.circuit.designer.model.geometry.Vector
import kotlin.math.*

class Car(val carConfig: CarConfig) {


    var heading = 0.0
    var steerAngle = 0.0
    var position = Vector()
    private var velocity = Vector()
    private var velocityC = Vector()
    private var accel = Vector()
    private var accelC = Vector()
    private var absVel = 0.0
    private var yawRate = 0.0
    private var steer = 0.0
    private var throttle = 0.0
    private var brake = 0.0
    private var inertia = carConfig.mass * carConfig.inertiaScale
    private var wheelBase = carConfig.cgToFrontAxle + carConfig.cgToRearAxle
    private var axleWeightRatioFront = carConfig.cgToRearAxle / wheelBase
    private var axleWeightRatioRear = carConfig.cgToFrontAxle / wheelBase
    private var inputs = CarInputs()

    val stats = HashMap<String, Double>()

    fun resetPosition(pos: Vector, angle: Double) {
        position = pos
        heading = angle
        velocity = Vector()
        velocityC = Vector()
        accel = Vector()
        accelC = Vector()
        absVel = 0.0
        yawRate = 0.0
        steer = 0.0
        steerAngle = 0.0
        throttle = 0.0
        brake = 0.0
    }

    fun updateAll(dtSec: Double, inputs: CarInputs) {
        this.inputs = inputs
        throttle = this.inputs.throttle
        brake = this.inputs.brake

        val steerInput = this.inputs.right - this.inputs.left

        steer = if (carConfig.smoothSteer) {
            applySmoothSteer(steerInput, dtSec)
        } else {
            steerInput
        }

        if (carConfig.safeSteer) {
            steer = applySafeSteer(steer)
        }

        steerAngle = steer * carConfig.maxSteer

        doPhysics(dtSec)
    }

    private fun applySmoothSteer(steerInput: Double, dtSec: Double): Double {
        if (abs(steerInput) > 0.001) {
            return min(1.0, max(-1.0, this.steer + steerInput * dtSec * 2.0))
        }
        if (this.steer > 0.0) {
            return max(this.steer - dtSec * 1.0, 0.0)
        }
        else if (this.steer < 0.0) {
            return min(this.steer + dtSec * 1.0, 0.0)
        }
        return 0.0
    }

    private fun applySafeSteer(steerInput: Double): Double {
        val avel = min(absVel, 250.0)
        return steerInput * (1.0 - (avel / 280.0))
    }

    private fun doPhysics(dtSec: Double) {
        // Shorthand
        val cfg = this.carConfig

        // Pre-calc heading vector
        val sn = sin(this.heading)
        val cs = cos(this.heading)

        // Get velocity in local car coordinates
        val velocityCx = cs * this.velocity.x + sn * this.velocity.y
        val velocityCy = cs * this.velocity.y - sn * this.velocity.x
        velocityC = Vector(velocityCx, velocityCy)

        // Weight on axles based on centre of gravity and weight shift due to forward/reverse acceleration
        val axleWeightFront = cfg.mass * (this.axleWeightRatioFront * cfg.gravity - cfg.weightTransfer * this.accelC.x * cfg.cgHeight / this.wheelBase)
        val axleWeightRear = cfg.mass * (this.axleWeightRatioRear * cfg.gravity + cfg.weightTransfer * this.accelC.x * cfg.cgHeight / this.wheelBase)

        // Resulting velocity of the wheels as result of the yaw rate of the car body.
        // v = yawrate * r where r is distance from axle to CG and yawRate (angular velocity) in rad/s.
        val yawSpeedFront = cfg.cgToFrontAxle * this.yawRate
        val yawSpeedRear = -cfg.cgToRearAxle * this.yawRate

        // Calculate slip angles for front and rear wheels (a.k.a. alpha)
        val slipAngleFront = atan2(this.velocityC.y + yawSpeedFront, abs(this.velocityC.x)) - sign(this.velocityC.x) * this.steerAngle
        val slipAngleRear  = atan2(this.velocityC.y + yawSpeedRear,  abs(this.velocityC.x))

        val tireGripFront = cfg.tireGrip
        val tireGripRear = cfg.tireGrip * (1.0 - this.inputs.eBrake * (1.0 - cfg.lockGrip)) // reduce rear grip when ebrake is on

        val frictionForceFrontCy = clamp(-cfg.cornerStiffnessFront * slipAngleFront, -tireGripFront, tireGripFront) * axleWeightFront
        val frictionForceRearCy = clamp(-cfg.cornerStiffnessRear * slipAngleRear, -tireGripRear, tireGripRear) * axleWeightRear

        //  Get amount of brake/throttle from our inputs
        val brake = (this.inputs.brake * cfg.brakeForce + this.inputs.eBrake * cfg.eBrakeForce).coerceAtMost(cfg.brakeForce)
        val throttle = this.inputs.throttle * cfg.engineForce

        //  Resulting force in local car coordinates.
        //  This is implemented as a RWD car only.
        val tractionForceCx = throttle - brake * sign(this.velocityC.x)
        val tractionForceCy = 0

        val dragForceCx = -cfg.rollResist * this.velocityC.x - cfg.airResist * this.velocityC.x * abs(this.velocityC.x)
        val dragForceCy = -cfg.rollResist * this.velocityC.y - cfg.airResist * this.velocityC.y * abs(this.velocityC.y)

        // total force in car coordinates
        val totalForceCx = dragForceCx + tractionForceCx
        val totalForceCy = dragForceCy + tractionForceCy + cos(this.steerAngle) * frictionForceFrontCy + frictionForceRearCy

        // acceleration along car axes
        val accelCx = totalForceCx / cfg.mass  // forward/reverse accel
        val accelCy = totalForceCy / cfg.mass  // sideways accel
        accelC = Vector(accelCx, accelCy)

        // acceleration in world coordinates
        val accelX = cs * this.accelC.x - sn * this.accelC.y
        val accelY = sn * this.accelC.x + cs * this.accelC.y
        accel = Vector(accelX, accelY)

        // update velocity
        val velocityDx = this.accel.x * dtSec
        val velocityDy = this.accel.y * dtSec
        this.velocity = this.velocity.getVectorSum(Vector(velocityDx, velocityDy))

        this.absVel = this.velocity.length()

        // calculate rotational forces
        var angularTorque = (frictionForceFrontCy + tractionForceCy) * cfg.cgToFrontAxle - frictionForceRearCy * cfg.cgToRearAxle

        //  Sim gets unstable at very slow speeds, so just stop the car
        if( abs(this.absVel) < 0.5 && throttle == 0.0 ) {
            this.velocity = Vector()
            this.absVel = 0.0
            angularTorque = 0.0
            this.yawRate = 0.0
        }

        val angularAccel = angularTorque / this.inertia

        this.yawRate += angularAccel * dtSec
        this.heading += this.yawRate * dtSec

        //  finally we can update position
        val positionDx = this.velocity.x * dtSec
        val positionDy = this.velocity.y * dtSec
        this.position = this.position.getVectorSum(Vector(positionDx, positionDy))

        //  Display some data
        this.stats.clear()
        this.stats["speed"] = this.velocityC.x * 3600 / 1000
        this.stats["accleration"] = this.accelC.x
        this.stats["yawRate"] = this.yawRate
        this.stats["weightFront"] = axleWeightFront
        this.stats["weightRear"] = axleWeightRear
        this.stats["slipAngleFront"] = slipAngleFront
        this.stats["slipAngleRear"] = slipAngleRear
        this.stats["frictionFront"] = frictionForceFrontCy
        this.stats["frictionRear"] = frictionForceRearCy
    }

    private fun clamp(n: Double, min: Double, max: Double): Double {
        return min(max, max(min, n))
    }

}