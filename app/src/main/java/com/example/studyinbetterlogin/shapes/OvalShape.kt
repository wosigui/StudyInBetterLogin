package com.example.studyinbetterlogin.shapes

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import kotlin.math.abs

class OvalShape(startX: Float, startY: Float, mPaint: Paint) : Shape(startX, startY, mPaint) {
    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        val left = Math.min(startX, endX)
        val right = Math.max(startX, endX)
        val top = Math.min(startY, endY)
        val bottom = Math.max(startY, endY)
        val rect = RectF(left, top, right, bottom)
        canvas.drawOval(rect, mPaint)
        canvas.restore()
    }
    override fun isInside(x: Float, y: Float): Boolean {
        val dx = (x - (startX+endX)/2) / (abs(endX-startX)/2)
        val dy = (y - (startY+endY)/2) / (abs(endY-startY)/2)
        return dx * dx + dy * dy <= 1
    }
}
