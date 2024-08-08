package com.example.studyinbetterlogin.shapes

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import kotlin.math.abs

class FreehandShape(startX: Float, startY: Float, mPaint: Paint) : Shape(startX, startY, mPaint) {
    private val points: MutableList<Pair<Float, Float>> = mutableListOf(Pair(startX, startY))

    fun addPoint(x: Float, y: Float) {
        points.add(Pair(x, y))
    }

    override fun draw(canvas: Canvas) {
        addPoint(endX,endY)
        if (points.size < 2) return

        val path = Path()
        val firstPoint = points.first()
        path.moveTo(firstPoint.first, firstPoint.second)

        for (i in 1 until points.size) {
            val point = points[i]
            path.lineTo(point.first, point.second)
        }

        canvas.drawPath(path, mPaint)
    }

    override fun isInside(x: Float, y: Float): Boolean {
        val tolerance = mPaint.strokeWidth / 2
        for (i in 0 until points.size - 1) {
            val (x1, y1) = points[i]
            val (x2, y2) = points[i + 1]
            if (isPointNearLine(x1, y1, x2, y2, x, y, tolerance)) {
                return true
            }
        }
        return false
    }

    private fun isPointNearLine(x1: Float, y1: Float, x2: Float, y2: Float, px: Float, py: Float, tolerance: Float): Boolean {
        val lineLen = distance(x1, y1, x2, y2)
        val d1 = distance(px, py, x1, y1)
        val d2 = distance(px, py, x2, y2)
        return abs(d1 + d2 - lineLen) <= tolerance
    }

    private fun distance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        return kotlin.math.sqrt(((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)).toDouble()).toFloat()
    }
}
