package fr.lewon.circuit.designer.model.road

import fr.lewon.circuit.designer.model.road.impl.classic.BottleNeckRoad
import fr.lewon.circuit.designer.model.road.impl.classic.SideCrunchedRoad
import fr.lewon.circuit.designer.model.road.impl.classic.StraightRoad
import fr.lewon.circuit.designer.model.road.impl.classic.TurnRoad
import fr.lewon.circuit.designer.model.road.impl.holed.HoledStraightRoad

enum class RoadElementCategory(val categoryName: String, val roadElements: List<RoadElement>) {

    CLASSIC_ROAD("Classic roads", listOf(StraightRoad, TurnRoad, BottleNeckRoad, SideCrunchedRoad)),
    HOLED_ROAD("Holed roads", listOf(HoledStraightRoad))

}