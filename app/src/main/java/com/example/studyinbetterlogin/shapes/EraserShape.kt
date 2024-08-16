package com.example.studyinbetterlogin.shapes

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import kotlin.math.abs

class EraserShape(startX: Float, startY: Float, mPaint: Paint) : Shape(startX, startY, mPaint) {
    val eraserPaint=Paint(mPaint).apply {
        color=0xFF1E2226.toInt()
        strokeWidth=mPaint.strokeWidth+10
    }
    private var points: MutableList<MutableList<Float>> = mutableListOf(mutableListOf(startX,startY))
    override var endX: Float = startX
        set(value) {
            field = value
            addPoint(value, endY)
        }


    private fun addPoint(x: Float, y: Float) {
        points.add(mutableListOf(x,y))
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        if (points.size < 2) return
        val path = Path()
        val firstPoint = points.first()
        path.moveTo(firstPoint[0], firstPoint[1])
        for (i in 1 until points.size) {
            val point = points[i]
            path.lineTo(point[0], point[1])
        }
        canvas.drawPath(path, eraserPaint)
        canvas.restore()
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

    fun movePoint(x:Float,y:Float){
        for (i in 0 until points.size) {
            points[i][0]+=x
            points[i][1]+=y
        }
    }
}
