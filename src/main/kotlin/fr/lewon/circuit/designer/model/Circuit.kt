package fr.lewon.circuit.designer.model

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

}