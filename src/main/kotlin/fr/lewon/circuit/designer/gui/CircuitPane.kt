package fr.lewon.circuit.designer.gui

import fr.lewon.circuit.designer.model.Circuit
import fr.lewon.circuit.designer.model.car.Car
import fr.lewon.circuit.designer.model.geometry.Point
import fr.lewon.circuit.designer.model.geometry.Vector
import javafx.application.Platform
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Line

class CircuitPane(private val circuit: Circuit) : Pane() {

    var tileSz = 32.0
    val minSize = 16.0
    val maxSize = 96.0

    var moveUp = false

    val circuitLines = ArrayList<Line>()
    val carLinesByCar = HashMap<Car, Array<Line>>()

    init {
        for (row in 0 until circuit.size) {
            for (col in 0 until circuit.size) {
                circuit.getElement(row, col)?.let { road ->
                    for (obstacle in road.obstacles) {
                        val from = Point(tileSz * col + tileSz * obstacle.xFrom, tileSz * row + tileSz * obstacle.yFrom)
                        val to = Point(tileSz * col + tileSz * obstacle.xTo, tileSz * row + tileSz * obstacle.yTo)
                        val line = Line()
                        buildLine(line, from, to, 3.0, road.color)
                        children.add(line)
                        circuitLines.add(line)
                    }
                }
            }
        }
        val car = Car(30.0, 0.1, 0.2, Color.RED).also {
            val lines = Array(4) { Line() }
            it.resetPosition(Vector(4.5, 4.5), 0.0)
            carLinesByCar[it] = lines
            updateCar(it)
            children.addAll(lines)
        }
        Thread {
            while (true) {
                Thread.sleep(10)
                Platform.runLater {
                    car.updateAll(if (moveUp) 300.0 else 0.0, 0.01)
                    updateCar(car)
                }
            }
        }.start()
        setOnMousePressed {
            moveUp = true
            println("MOVE")
        }
        setOnMouseReleased {
            moveUp = false
            println("STOP")
        }
    }

    fun updateCar(car: Car) {
        val backLeftMult = car.posBackLeft.getVectorMult(tileSz)
        val backRightMult = car.posBackRight.getVectorMult(tileSz)
        val frontLeftMult = car.posFrontLeft.getVectorMult(tileSz)
        val frontRightMult = car.posFrontRight.getVectorMult(tileSz)
        carLinesByCar[car]?.let {
            buildLine(it[0], backLeftMult, frontLeftMult, 2.0, car.color)
            buildLine(it[1], frontLeftMult, frontRightMult, 1.0, car.color)
            buildLine(it[2], frontRightMult, backRightMult, 1.0, car.color)
            buildLine(it[3], backRightMult, backLeftMult, 1.0, car.color)
        }
    }

    private fun buildLine(line: Line, from: Point, to: Point, strokeWidth: Double, color: Color) {
        line.startX = from.x
        line.startY = from.y
        line.endX = to.x
        line.endY = to.y
        line.strokeWidth = strokeWidth
        line.stroke = color
    }

}