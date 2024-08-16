package com.example.studyinbetterlogin.shapes

import android.graphics.Canvas
import android.graphics.Paint

/**
 * 废弃对象，仍然可用
 */
class CircleShape(startX: Float, startY: Float, mPaint: Paint
):Shape(startX, startY, mPaint) {
    val radius = Math.sqrt(((endX - startX) * (endX - startX) + (endY - startY) * (endY - startY)).toDouble()).toFloat()
    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        canvas.drawCircle(startX, startY, radius, mPaint)
        canvas.restore()
    }

    override fun isInside(x: Float, y: Float): Boolean {
        return (x-startX)*(x-startX)+(y-startY)*(y-startY)<=radius*radius
    }
}