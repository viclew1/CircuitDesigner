package fr.lewon.circuit.designer.nn

import fr.lewon.Individual
import fr.lewon.circuit.designer.model.Circuit
import fr.lewon.circuit.designer.model.car.Car
import fr.lewon.circuit.designer.model.car.CarInputs

class CarBrain(val car: Car, val circuit: Circuit, val brain: Individual, val captors: List<CarCaptor>) {

    fun generateInputs(captorValues: List<Double>): CarInputs {
        val nnOutputs = brain.getOutputs(listOf(*captorValues.toTypedArray()))
        val inputs = CarInputs()
        inputs.left = nnOutputs[0]
        inputs.right = nnOutputs[1]
        inputs.throttle = nnOutputs[2]
        inputs.brake = nnOutputs[3]
        inputs.eBrake = nnOutputs[4]
        return inputs
    }

}