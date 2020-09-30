package fr.lewon.circuit.designer.model.geometry

import kotlin.math.max
import kotlin.math.min


object IntersectionUtil {

    private fun shapeToLines(s: Shape): List<Line> {
        val lines = ArrayList<Line>()
        for (i in 0 until s.points.size - 1) {
            lines.add(Line(s.points[i], s.points[i + 1]))
        }
        return lines
    }

    fun intersects(s1: Shape, s2: Shape): Boolean {
        val lines1 = shapeToLines(s1)
        val lines2 = shapeToLines(s2)
        for (l1 in lines1) {
            for (l2 in lines2) {
                if (intersects(l1, l2)) {
                    return true
                }
            }
        }
        return false
    }

    private fun onSegment(p: Vector, q: Vector, r: Vector): Boolean {
        return q.x <= max(p.x, r.x) && q.x >= min(p.x, r.x) &&
            q.y <= max(p.y, r.y) && q.y >= min(p.y, r.y)
    }

    private fun orientation(p: Vector, q: Vector, r: Vector): Int {
        val value = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y)
        if (value == 0.0) return 0
        else if (value < 0.0) return 1
        return 2
    }

    fun intersects(l1: Line, l2: Line): Boolean {
        val p1 = l1.from()
        val q1 = l1.to()
        val p2 = l2.from()
        val q2 = l2.to()

        val o1 = orientation(p1, q1, p2)
        val o2 = orientation(p1, q1, q2)
        val o3 = orientation(p2, q2, p1)
        val o4 = orientation(p2, q2, q1)

        if (o1 != o2 && o3 != o4) return true

        if (o1 == 0 && onSegment(p1, p2, q1)) return true
        if (o2 == 0 && onSegment(p1, q2, q1)) return true
        if (o3 == 0 && onSegment(p2, p1, q2)) return true
        return o4 == 0 && onSegment(p2, q1, q2)
    }

}