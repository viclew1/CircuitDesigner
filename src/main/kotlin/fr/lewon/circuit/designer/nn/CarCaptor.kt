package fr.lewon.circuit.designer.nn

import fr.lewon.circuit.designer.model.car.Car

data class CarCaptor(
    val car: Car,
    val angle: Double,
    val maxLength: Double
)