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
        if (row < 0 || row >= size || col < 0 || col >= size) {
            return null
        }
        return roadElements[row][col]
    }

    fun setElement(row: Int, col: Int, element: RoadElement?) {
        if (row < 0 || row >= size || col < 0 || col >= size) {
            return
        }
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
                    return Vector(col.toDouble() + 0.5, row.toDouble() + 0.5)
                }
            }
        }
        return null
    }

    fun getElementsAround(row: Int, col: Int, range: Int): List<RoadElement> {
        val elements = ArrayList<RoadElement>()
        for (r in row - range..row + range) {
            for (c in col - range..col + range) {
                getElement(r, c)?.let { elements.add(it) }
            }
        }
        return elements
    }

}