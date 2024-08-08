package com.example.studyinbetterlogin.shapes


import android.graphics.Canvas
import android.graphics.Paint
import kotlin.math.abs

class LineShape(startX: Float, startY: Float, mPaint: Paint) : Shape(startX, startY, mPaint) {
    override fun draw(canvas: Canvas) {
        canvas.drawLine(startX, startY, endX, endY, mPaint)
    }

    override fun isInside(x: Float, y: Float): Boolean {
        // Use the paint's stroke width as tolerance
        val tolerance = mPaint.strokeWidth
        val d1 = distance(startX, startY, x, y)
        val d2 = distance(endX, endY, x, y)
        val lineLen = distance(startX, startY, endX, endY)
        return abs(d1 + d2 - lineLen) <= tolerance
    }

    private fun distance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        return Math.sqrt(((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)).toDouble()).toFloat()
    }
}
