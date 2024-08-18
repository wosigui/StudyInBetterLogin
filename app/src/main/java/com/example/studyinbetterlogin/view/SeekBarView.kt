package com.example.studyinbetterlogin.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * 手搓的进度条
 */
class SeekBarView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    var startX = 0f
    var endX = 10f
    var startY = 0f
    var endY = 100f
    var CircleY = startY
    var minSize = 1
    var maxSize = 50
    var thisSize = 1
    var size: String = thisSize.toString()

    // 圆的半径
    var radius = 30f

    private var mPaint: Paint = Paint().apply {
        color = Color.GRAY
        strokeWidth = 5f
        style = Paint.Style.FILL
    }

    private var circlePaint: Paint = Paint().apply {
        color = Color.RED
        strokeWidth = 5f
        style = Paint.Style.FILL
    }

    private var bluePaint = Paint().apply {
        color = Color.BLUE
        strokeWidth = 5f
        style = Paint.Style.FILL
    }

    private var textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 50f
        textAlign = Paint.Align.CENTER
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val left = (startX + endX) / 4
        val right = (startX + endX) / 4 * 3
        val top = Math.min(startY, endY)
        val bottom = Math.max(startY, endY)
        val rect = RectF(left, top, right, bottom)
        val rect1 = RectF(left, top, right, CircleY)
        canvas.drawRect(rect, mPaint)
        canvas.drawRect(rect1, bluePaint)
        val constrainedCircleY = CircleY.coerceIn(top + radius, bottom - radius)
        val circleX = (startX + endX) / 2
        canvas.drawCircle(circleX, constrainedCircleY, radius, circlePaint)
        canvas.drawText(size, circleX, constrainedCircleY - (textPaint.descent() + textPaint.ascent()) / 2, textPaint)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // 根据 View 的宽高来设置 startX, endX, startY, endY 的值
        startX = 0f
        endX = w.toFloat()
        startY = 0f
        endY = h.toFloat()
        CircleY = startY
    }

    private var needCircleMove = false

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                needCircleMove = inCircle(event.x, event.y)
            }
            MotionEvent.ACTION_MOVE -> {
                if (needCircleMove) {
                    if (event.y in startY..endY) {
                        CircleY = event.y
                        updateSize()
                        invalidate()
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                needCircleMove = false
            }
        }
        return true
    }

    fun inCircle(x: Float, y: Float): Boolean {
        val centerX = (startX + endX) / 2
        val centerY = CircleY.coerceIn(startY + radius, endY - radius)
        val distance = Math.hypot((x - centerX).toDouble(), (y - centerY).toDouble())
        return distance <= radius
    }

    private fun updateSize() {
        val proportion = (CircleY - startY) / (endY - startY)
        thisSize = (minSize + proportion * (maxSize - minSize)).toInt()
        size = thisSize.toString()
    }
}
