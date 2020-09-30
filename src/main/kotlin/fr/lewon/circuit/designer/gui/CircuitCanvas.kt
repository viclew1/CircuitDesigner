package fr.lewon.circuit.designer.gui

import fr.lewon.Individual
import fr.lewon.Trial
import fr.lewon.circuit.designer.gui.visuals.CarCaptorModel
import fr.lewon.circuit.designer.gui.visuals.CarModel
import fr.lewon.circuit.designer.gui.visuals.RoadModel
import fr.lewon.circuit.designer.model.Circuit
import fr.lewon.circuit.designer.model.car.Car
import fr.lewon.circuit.designer.model.car.CarConfig
import fr.lewon.circuit.designer.model.car.CarInputs
import fr.lewon.circuit.designer.model.geometry.IntersectionUtil
import fr.lewon.circuit.designer.model.geometry.Vector
import fr.lewon.circuit.designer.model.road.RoadElement
import fr.lewon.circuit.designer.model.road.RoadElementType
import fr.lewon.circuit.designer.nn.CarBrain
import fr.lewon.circuit.designer.nn.CarCaptor
import javafx.animation.AnimationTimer
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class CircuitCanvas(private val circuit: Circuit) : Trial, Canvas(1000.0, 1000.0) {

    val tileSz = 5.0
    val obstacleSz = 12.0
    val minSize = 3.0
    val maxSize = 25.0

    private val roadModelByRoadElement = Collections.synchronizedMap(HashMap<RoadElement, RoadModel>())
    private val captorModelsByCar = Collections.synchronizedMap(HashMap<Car, List<CarCaptorModel>>())
    private val carModelByCar = Collections.synchronizedMap(HashMap<Car, CarModel>())
    private val parcouredByCar = Collections.synchronizedMap(HashMap<Car, HashSet<RoadElement>>())

    init {
        for (row in 0 until circuit.size) {
            for (col in 0 until circuit.size) {
                circuit.getElement(row, col)?.let { road ->
                    val roadModel = RoadModel(row, col, road, obstacleSz, tileSz)
                    roadModelByRoadElement[road] = roadModel
                }
            }
        }
        object : AnimationTimer() {
            override fun handle(now: Long) {
                updateVisual(graphicsContext2D)
            }
        }.start()
    }

    private fun updateVisual(gc: GraphicsContext) {
        synchronized(this) {
            gc.clearRect(0.0, 0.0, width, height)
            for (road in ArrayList(roadModelByRoadElement.values)) {
                road.updateVisual()
                road.render(gc)
            }
            for (car in ArrayList(carModelByCar.values)) {
                car.updateVisual()
                car.render(gc)
            }
            val captors = ArrayList(captorModelsByCar.values)
            for (captor in captors.flatten()) {
                captor.updateVisual()
                captor.render(gc)
            }
        }
    }

    private fun updateCar(car: Car, inputs: CarInputs): Boolean {
        car.updateAll(0.01, inputs)
        carModelByCar[car]?.let {
            it.updateHitbox()
            val row = (car.position.y / obstacleSz).toInt()
            val col = (car.position.x / obstacleSz).toInt()
            val currentElement = circuit.getElement(row, col) ?: return false
            parcouredByCar[car]?.add(currentElement)
            val roadModel = roadModelByRoadElement[currentElement] ?: return false
            for (obs in roadModel.hitboxLines) {
                if (IntersectionUtil.intersects(it.hitbox, obs)) {
                    return false
                }
            }
            val obstaclesAround = circuit.getElementsAround(row, col, 1)
                .mapNotNull { r -> roadModelByRoadElement[r] }
                .map { m -> m.hitboxLines }
                .flatten()

            captorModelsByCar[car]?.forEach { captor ->
                captor.updateLength(obstaclesAround)
                captor.updateHitbox()
            }
        }
        return true
    }

    override fun execute(population: MutableList<Individual>) {
        val brains = population.map {
            val car = Car(CarConfig())
            CarBrain(
                car, this.circuit, it, List(9) { i ->
                    CarCaptor(car, -Math.PI / 2.0 + (Math.PI * i / 8.0), 10.0)
                }
            )
        }
        parcouredByCar.clear()
        carModelByCar.clear()
        captorModelsByCar.clear()

        val start = circuit.getAllElements()
            .first { it.type == RoadElementType.START || it.type == RoadElementType.LAP }
        val pos = circuit.getElementPos(start)
            ?.getVectorMult(obstacleSz)
            ?: Vector(obstacleSz / 2.0, obstacleSz / 2.0)

        brains.forEach {
            parcouredByCar[it.car] = HashSet()
            it.car.resetPosition(pos, -Math.PI / 2.0 - start.rotation)
            val carModel = CarModel(tileSz, it.car)
            captorModelsByCar[it.car] = it.captors.map { cc ->
                CarCaptorModel(tileSz, cc)
            }
            carModelByCar[it.car] = carModel
            updateCar(it.car, CarInputs())
        }

        var currentBrains = brains
        var cpt = 0
        while (currentBrains.isNotEmpty() && cpt < 10000) {
            val newCurrentBrains = ArrayList<CarBrain>()
            for (brain in currentBrains) {
                val captorModels = captorModelsByCar[brain.car] ?: emptyList()
                if (updateCar(brain.car, brain.generateInputs(captorModels.map { 1.0 - it.length / it.maxLength }))) {
                    newCurrentBrains.add(brain)
                }
            }
            currentBrains = newCurrentBrains
            cpt++
        }
        brains.forEach { it.brain.fitness = parcouredByCar[it.car]?.size?.times(10.0) ?: 0.0 }
    }

}