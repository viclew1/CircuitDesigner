package fr.lewon.circuit.designer.model.car

import javafx.scene.paint.Color

data class CarConfig(
    val safeSteer: Boolean = true,
    val smoothSteer: Boolean = true,

    val color: Color = Color.BLUE,
    val globalMultiplier: Double = 0.1,
    val halfWidth: Double = 0.8,
    val cgToFront: Double = 2.25,
    val cgToRear: Double = 1.75,
    val cgToFrontAxle: Double = 2.0,
    val cgToRearAxle: Double = 1.0,
    val cgHeight: Double = 0.55,
    val mass: Double = 1200.0,
    val wheelRadius: Double = 0.3,
    val wheelWidth: Double = 0.2,
    val tireGrip: Double = 2.0,
    val lockGrip: Double = 0.7,
    val engineForce: Double = 8000.0,
    val brakeForce: Double = 12000.0,
    val eBrakeForce: Double = brakeForce / 2.5,
    val weightTransfer: Double = 0.2,
    val maxSteer: Double = 0.9,
    val cornerStiffnessFront: Double = 5.0,
    val cornerStiffnessRear: Double = 5.2,
    val airResist: Double = 2.5,
    val rollResist: Double = 8.0,
    val gravity: Double = 9.81,
    val inertiaScale: Double = 1.0
)