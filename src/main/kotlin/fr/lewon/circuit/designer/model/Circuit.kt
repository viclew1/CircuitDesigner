package fr.lewon.circuit.designer.model

import fr.lewon.circuit.designer.model.geometry.Vector
import fr.lewon.circuit.designer.model.road.RoadElement

class Circuit(val size: Int) {

    private val roadElements: ArrayList<ArrayList<RoadElement?>> = ArrayList()

    init {
        for (row in 0 until size) {
            roadElements.add(ArrayList())
            for (col in 0 until size) {
                roadElements[row].add(null)
            }
        }
    }

    fun getElement(row: Int, col: Int): RoadElement? {
        return roadElements[row][col]
    }

    fun setElement(row: Int, col: Int, element: RoadElement?) {
        roadElements[row][col] = element
    }

    fun getAllElements(): List<RoadElement> {
        val elements = ArrayList<RoadElement>()
        for (row in 0 until size) {
            for (col in 0 until size) {
                roadElements[row][col]?.let {
                    elements.add(it)
                }
            }
        }
        return elements
    }

    fun getElementPos(element: RoadElement): Vector? {
        for (row in 0 until size) {
            for (col in 0 until size) {
                if (getElement(row, col) == element) {
                    return Vector(col.toDouble() + element.width / 2.0, row.toDouble() + element.height / 2.0)
                }
            }
        }
        return null
    }

}