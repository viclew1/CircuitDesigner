package fr.lewon.circuit.designer.gui

import fr.lewon.Individual
import fr.lewon.Trial
import fr.lewon.circuit.designer.gui.visuals.CarCaptorVisual
import fr.lewon.circuit.designer.gui.visuals.CarVisual
import fr.lewon.circuit.designer.gui.visuals.RoadVisual
import fr.lewon.circuit.designer.model.Circuit
import fr.lewon.circuit.designer.model.car.Car
import fr.lewon.circuit.designer.model.car.CarConfig
import fr.lewon.circuit.designer.model.car.CarInputs
import fr.lewon.circuit.designer.model.geometry.Vector
import fr.lewon.circuit.designer.model.road.RoadElement
import fr.lewon.circuit.designer.model.road.RoadElementType
import fr.lewon.circuit.designer.nn.CarBrain
import fr.lewon.circuit.designer.nn.CarCaptor
import javafx.animation.AnimationTimer
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.shape.Path
import javafx.scene.shape.Shape
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class CircuitCanvas(private val circuit: Circuit) : Trial, Canvas(1000.0, 1000.0) {

    val tileSz = 5.0
    val obstacleSz = 12.0
    val minSize = 3.0
    val maxSize = 25.0

    private val roadVisualByRoadElement = Collections.synchronizedMap(HashMap<RoadElement, RoadVisual>())
    private val captorVisualsByCar = Collections.synchronizedMap(HashMap<Car, List<CarCaptorVisual>>())
    private val carVisualByCar = Collections.synchronizedMap(HashMap<Car, CarVisual>())
    private val parcouredByCar = Collections.synchronizedMap(HashMap<Car, HashSet<RoadElement>>())

    init {
        for (row in 0 until circuit.size) {
            for (col in 0 until circuit.size) {
                circuit.getElement(row, col)?.let { road ->
                    val roadVisual = RoadVisual(row, col, road, obstacleSz, tileSz)
                    roadVisualByRoadElement[road] = roadVisual
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
            for (road in ArrayList(roadVisualByRoadElement.values)) {
                road.updateVisual()
                road.render(gc)
            }
            for (car in ArrayList(carVisualByCar.values)) {
                car.updateVisual()
                car.render(gc)
            }
            val captors = ArrayList(captorVisualsByCar.values)
            for (captor in captors.flatten()) {
                captor.updateVisual()
                captor.render(gc)
            }
        }
    }

    private fun updateCar(car: Car, inputs: CarInputs): Boolean {
        car.updateAll(0.01, inputs)
        carVisualByCar[car]?.let {
            it.updateHitbox()
            val row = (car.position.y / obstacleSz).toInt()
            val col = (car.position.x / obstacleSz).toInt()
            val currentElement = circuit.getElement(row, col) ?: return false
            parcouredByCar[car]?.add(currentElement)
            val visual = roadVisualByRoadElement[currentElement] ?: return false
            for (obs in visual.hitboxLines) {
                if ((Shape.intersect(it.hitbox, obs) as Path).elements.isNotEmpty()) {
                    return false
                }
            }
            captorVisualsByCar[car]?.forEach { captor -> captor.updateHitbox() }
        }
        return true
    }

    override fun execute(population: MutableList<Individual>) {
        val brains = population.map {
            val car = Car(CarConfig())
            CarBrain(
                car, this.circuit, it, listOf(
                    CarCaptor(car, -Math.PI / 2.0 + (Math.PI * 0.0 / 4.0), 10.0),
                    CarCaptor(car, -Math.PI / 2.0 + (Math.PI * 1.0 / 4.0), 10.0),
                    CarCaptor(car, -Math.PI / 2.0 + (Math.PI * 2.0 / 4.0), 10.0),
                    CarCaptor(car, -Math.PI / 2.0 + (Math.PI * 3.0 / 4.0), 10.0),
                    CarCaptor(car, -Math.PI / 2.0 + (Math.PI * 4.0 / 4.0), 10.0)
                )
            )
        }
        parcouredByCar.clear()
        carVisualByCar.clear()
        captorVisualsByCar.clear()

        val start = circuit.getAllElements()
            .first { it.type == RoadElementType.START || it.type == RoadElementType.LAP }
        val pos = circuit.getElementPos(start)
            ?.getVectorMult(obstacleSz)
            ?: Vector(obstacleSz / 2.0, obstacleSz / 2.0)

        brains.forEach {
            parcouredByCar[it.car] = HashSet()
            it.car.resetPosition(pos, -Math.PI / 2.0 - start.rotation)
            val visual = CarVisual(tileSz, it.car)
            captorVisualsByCar[it.car] = it.captors.map { cc ->
                CarCaptorVisual(tileSz, cc)
            }
            carVisualByCar[it.car] = visual
            updateCar(it.car, CarInputs())
        }

        var currentBrains = brains
        var cpt = 0
        while (currentBrains.isNotEmpty() && cpt < 2000) {
            val newCurrentBrains = ArrayList<CarBrain>()
            for (brain in currentBrains) {
                if (updateCar(brain.car, brain.generateInputs())) {
                    newCurrentBrains.add(brain)
                }
            }
            currentBrains = newCurrentBrains
            cpt++
        }
        brains.forEach { it.brain.fitness = parcouredByCar[it.car]?.size?.times(10.0) ?: 0.0 }
    }

}