package com.example.studyinbetterlogin.shapes

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import kotlin.math.abs

class TriangleShape(startX: Float, startY: Float, mPaint: Paint) : Shape(startX, startY, mPaint) {
    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        val path = Path()
        path.moveTo(startX, startY)
        path.lineTo(endX, endY)
        path.lineTo(2 * startX - endX, endY)
        path.close()
        canvas.drawPath(path, mPaint)
        canvas.restore()
    }

    override fun isInside(x: Float, y: Float): Boolean {
        // 使用面积法判断点是否在三角形内部
        val areaOrig = area(startX, startY, endX, endY, 2 * startX - endX, endY)
        val area1 = area(x, y, endX, endY, 2 * startX - endX, endY)
        val area2 = area(startX, startY, x, y, 2 * startX - endX, endY)
        val area3 = area(startX, startY, endX, endY, x, y)

        // 设置一个容差值，避免浮点数精度问题
        val tolerance = 0.02f

        return abs(areaOrig - (area1 + area2 + area3)) < tolerance
    }

    private fun area(x1: Float, y1: Float, x2: Float, y2: Float, x3: Float, y3: Float): Float {
        return abs((x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2)) / 2.0f)
    }
}
