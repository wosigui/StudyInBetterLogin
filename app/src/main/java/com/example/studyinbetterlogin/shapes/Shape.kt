package com.example.studyinbetterlogin.shapes

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import kotlin.math.abs

abstract class Shape(
    var startX: Float,
    var startY: Float,
    var mPaint: Paint
) {
    open var endX: Float = startX
    open var endY: Float = startY
    var isFrameShow:Boolean = false

    /**
     * 建议在继承的时候在此方法开头使用
     * super.draw(canvas)
     * 如果使用了上面的方法，一定要在此方法的末尾添加
     * canvas.restore()
     */
    open fun draw(canvas: Canvas){
        canvas.save()
        canvas.rotate(rotationAngle, (startX+endX)/ 2, (startY+endY) / 2)
    }
    abstract fun isInside(x:Float,y:Float):Boolean
    var framePaint:Paint = Paint().apply {
        color = Color.GRAY
        strokeWidth = 5f
        style=Paint.Style.STROKE
        pathEffect = DashPathEffect(floatArrayOf(10f, 20f), 0f)
    }
    var cornerHandlePaint:Paint=Paint().apply {
        color = Color.WHITE
        strokeWidth = 10f
        style = Paint.Style.FILL
    }
    var left:Float = 0f
    var right:Float = 0f
    var top :Float = 0f
    var bottom :Float = 0f
    var cornerHandleSize:Float = 16f
    var rotationAngle = 0f
    lateinit var rect:RectF
    lateinit var rect1:RectF
    lateinit var rect2:RectF
    lateinit var rect3:RectF
    lateinit var rect4:RectF
    lateinit var rectRotate:RectF
    lateinit var thisrect:RectF
    fun drawFrame(canvas: Canvas){
        left = Math.min(startX, endX)
        right = Math.max(startX, endX)
        top = Math.min(startY, endY)
        bottom = Math.max(startY, endY)
        rect = RectF(left, top, right, bottom)
        rect1 = RectF(left-cornerHandleSize,top-cornerHandleSize,left+cornerHandleSize,top+cornerHandleSize)
        rect2 = RectF(right-cornerHandleSize,top-cornerHandleSize,right+cornerHandleSize,top+cornerHandleSize)
        rect3 = RectF(left-cornerHandleSize,bottom-cornerHandleSize,left+cornerHandleSize,bottom+cornerHandleSize)
        rect4 = RectF(right-cornerHandleSize,bottom-cornerHandleSize,right+cornerHandleSize,bottom+cornerHandleSize)
        rectRotate = RectF((left+right)/2-cornerHandleSize,top-4*cornerHandleSize,(left+right)/2+cornerHandleSize,top-2*cornerHandleSize)
        canvas.drawRect(rect, framePaint)
        canvas.drawRect(rect1, cornerHandlePaint)
        canvas.drawRect(rect2, cornerHandlePaint)
        canvas.drawRect(rect3, cornerHandlePaint)
        canvas.drawRect(rect4, cornerHandlePaint)
        canvas.drawRect(rectRotate,cornerHandlePaint)
    }
    fun isInsideCornerHandle(x:Float,y:Float):Boolean{
        for( i in listOf(rect1,rect2,rect3,rect4,rectRotate)){
            if(i.contains(x,y)){
                thisrect=i
                return true
            }
        }
        return false
    }
}