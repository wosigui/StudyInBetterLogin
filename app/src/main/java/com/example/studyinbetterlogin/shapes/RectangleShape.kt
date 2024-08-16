package com.example.studyinbetterlogin.shapes

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF

class RectangleShape(startX: Float, startY: Float, mPaint: Paint) : Shape(startX, startY, mPaint) {
    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        val left = Math.min(startX, endX)
        val right = Math.max(startX, endX)
        val top = Math.min(startY, endY)
        val bottom = Math.max(startY, endY)
        val rect = RectF(left, top, right, bottom)
        canvas.drawRect(rect, mPaint)
        canvas.restore()
    }

    override fun isInside(x: Float, y: Float): Boolean {
        val left = Math.min(startX, endX)
        val right = Math.max(startX, endX)
        val top = Math.min(startY, endY)
        val bottom = Math.max(startY, endY)
        return x >= left && x <= right && y >= top && y <= bottom
    }
}