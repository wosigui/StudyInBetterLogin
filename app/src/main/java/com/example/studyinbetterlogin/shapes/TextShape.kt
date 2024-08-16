package com.example.studyinbetterlogin.shapes

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF

class TextShape(startX: Float, startY: Float, mPaint: Paint) : Shape(startX, startY, mPaint) {
    var text: String = "T e x t"
    var isShowFrame:Boolean = true
    var thisPaint=Paint(mPaint)
    var textSize: Float = 60f
        set(value) {
            field = value
            mPaint.textSize = value
        }
    var TextXCenter:Float = (startX+endX)/2
    var TextYCenter:Float = (startY+endY)/2
    init {
        // 初始化时设置 Paint 的 textSize
        thisPaint.strokeWidth=10f
        thisPaint.textSize = textSize
    }
    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        val left = Math.min(startX, endX)
        val right = Math.max(startX, endX)
        val top = Math.min(startY, endY)
        val bottom = Math.max(startY, endY)
        val rect = RectF(left, top, right, bottom)
        if(isShowFrame){
            thisPaint.style = Paint.Style.STROKE
            canvas.drawRect(rect, thisPaint)
        }
        val textBounds = Rect()
        thisPaint.getTextBounds(text, 0, text.length, textBounds)
        thisPaint.style = Paint.Style.FILL
        TextXCenter=startX/2+endX/2- textBounds.width() / 2f
        TextYCenter=startY/2+endY/2+textBounds.height()/2
        canvas.drawText(text, startX/2+endX/2- textBounds.width() / 2f, startY/2+endY/2+textBounds.height()/2, thisPaint)
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