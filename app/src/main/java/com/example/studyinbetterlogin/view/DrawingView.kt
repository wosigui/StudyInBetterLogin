package com.example.studyinbetterlogin.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.studyinbetterlogin.shapes.CircleShape
import com.example.studyinbetterlogin.shapes.LayerManager
import com.example.studyinbetterlogin.shapes.OvalShape
import com.example.studyinbetterlogin.shapes.Shape
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs)  {
    val layerManager: LayerManager = LayerManager()

    private var currentPaint: Paint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 5f
        style = Paint.Style.STROKE
    }
    private var currentShape:Shape = CircleShape(-500f,-500f,currentPaint)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        layerManager.drawShape(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val shape = createShape(currentShape::class, event.x, event.y)
                layerManager.addShape(shape)
                performClick() // 调用 performClick 方法
            }
            MotionEvent.ACTION_MOVE -> {
                layerManager.endPoint(event.x, event.y)
                invalidate()
            }
        }
        return true
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    fun loadShape(shape: Shape){
        currentShape=shape
    }
    fun loadPaint(paint:Paint){
        currentPaint=paint
    }
     //创建一个新的 Shape 对象
    private fun <T : Shape> createShape(shapeClass: KClass<T>, startX: Float, startY: Float): Shape {
        return shapeClass.primaryConstructor?.call(startX, startY, currentPaint)
            ?: throw IllegalArgumentException("Cannot create instance of ${shapeClass.simpleName}")
    }
}