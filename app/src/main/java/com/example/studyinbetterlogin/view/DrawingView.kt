package com.example.studyinbetterlogin.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.studyinbetterlogin.shapes.CircleShape
import com.example.studyinbetterlogin.shapes.EraserShape
import com.example.studyinbetterlogin.shapes.FreehandShape
import com.example.studyinbetterlogin.shapes.LayerManager
import com.example.studyinbetterlogin.shapes.Shape
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs)  {
    val layerManager: LayerManager = LayerManager()
    private val states: MutableMap<String, Boolean> = mutableMapOf(
        "isFill" to false,
        "isMove" to false,
        "isDraw" to true ,// 默认状态
        "isEraser" to false
    )
    fun setState(key: String) {
        // 遍历所有键，将所有值设为 false
        for (k in states.keys) {
            states[k] = false
        }
        // 将指定键的值设为 true
        states[key] = true
    }
    private lateinit var isMovingShape:Shape
    private lateinit var nextMovingShape:Shape
    private var isMovingX:Float?=null
    private var isMovingY:Float?=null
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
                if(states["isFill"] == true){
                    fillPaint(event.x,event.y)
                    invalidate()
                    return true
                }else if (states["isMove"] == true){
                    selectMoveShape(event.x,event.y)
                    invalidate()
                    return true
                }else if(states["isEraser"]==true){
                    eraserToClean(event.x,event.y)
                    invalidate()
                }
                else if(states["isDraw"]==true){
                    val shape = createShape(currentShape::class, event.x, event.y)
                    layerManager.addShape(shape)
                    performClick() // 调用 performClick 方法
                    invalidate()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if(states["isFill"] == true){
                    return true
                }else if(states["isMove"] == true){
                    moveSelectShape(event.x,event.y)
                    invalidate()
                    return true
                }else if(states["isEraser"]==true){

                    eraserToMove(event.x,event.y)
                    invalidate()
                }else if(states["isDraw"]==true){
                    layerManager.endPoint(event.x, event.y)
                    invalidate()
                }
            }
            MotionEvent.ACTION_UP ->{
                if(states["isDraw"]==true){
                    eraserToUp()
                }
            }
        }
        return true
    }
    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    fun loadShape(shape: Shape){
        setState("isDraw")
        currentShape=shape
    }
    fun loadPaint(paint:Paint){
        currentPaint=Paint(paint)
    }
     //创建一个新的 Shape 对象
    private fun <T : Shape> createShape(shapeClass: KClass<T>, startX: Float, startY: Float): Shape {
        return shapeClass.primaryConstructor?.call(startX, startY, currentPaint)
            ?: throw IllegalArgumentException("Cannot create instance of ${shapeClass.simpleName}")
    }
    private fun fillPaint(x: Float, y: Float) {
        setState("isFill")
        for (layout in layerManager.mShapes) {
            for (shape in layout) {
                if (shape.isInside(x, y)&&shape.mPaint.style!=Paint.Style.FILL) {
                    // 为每个 Shape 创建一个新的 Paint 实例
                    val newPaint = Paint(shape.mPaint).apply {
                        style = Paint.Style.FILL
                    }
                    shape.mPaint = newPaint
                    return
                }
            }
        }
    }
    private fun selectMoveShape(x:Float,y:Float){
        setState("isMove")
        for (layoutIndex in layerManager.mShapes.size-1 downTo 0) {
            for (shapeIndex in layerManager.mShapes[layoutIndex].size-1 downTo 0) {
                if (layerManager.mShapes[layoutIndex][shapeIndex].isInside(x, y)) {
                    isMovingShape=layerManager.mShapes[layoutIndex][shapeIndex]
                    isMovingX=x
                    isMovingY=y
                    if(shapeIndex+1<layerManager.mShapes[layoutIndex].size){
                        nextMovingShape=layerManager.mShapes[layoutIndex][shapeIndex+1]
                    }
                    return
                }
            }
        }
    }
    private fun moveSelectShape(x:Float,y:Float){
        // 检查 isMovingShape 是否已初始化
        if (!::isMovingShape.isInitialized) {
            Log.e("moveSelectShape", "isMovingShape is not initialized")
            return
        }

        val deltaX = x - (isMovingX ?: return)
        val deltaY = y - (isMovingY ?: return)

        // 检查 nextMovingShape 是否为 EraserShape 并已初始化
        if (::nextMovingShape.isInitialized && nextMovingShape is EraserShape) {
            (nextMovingShape as EraserShape).movePoint(deltaX, deltaY)
        }
        if(isMovingShape is FreehandShape){
            (isMovingShape as FreehandShape).movePoint(deltaX,deltaY)
        }else {
            isMovingShape.startX += deltaX
            isMovingShape.startY += deltaY
            isMovingShape.endX += deltaX
            isMovingShape.endY += deltaY

        }
        // 更新当前的x和y坐标
        isMovingX = x
        isMovingY = y
    }

    private fun eraserToClean(x:Float,y: Float){
        for (layoutIndex in layerManager.mShapes.size-1 downTo 0) {
            for (shapeIndex in layerManager.mShapes[layoutIndex].size-1 downTo 0) {
                if (layerManager.mShapes[layoutIndex][shapeIndex].isInside(x, y)&&layerManager.mShapes[layoutIndex][shapeIndex] !is EraserShape) {
                    if(shapeIndex+1<=layerManager.mShapes[layoutIndex].size-1){
                        Log.d("xyn","成功添加$shapeIndex")
                        layerManager.mShapes[layoutIndex].add(shapeIndex+1,EraserShape(x,y,currentPaint))
                    }else{
                        Log.d("xyn","成功添加$shapeIndex")
                        layerManager.mShapes[layoutIndex].add(EraserShape(x,y,currentPaint))
                    }
                }
            }
        }
    }
    private fun eraserToMove(x:Float,y:Float){
        for (layoutIndex in layerManager.mShapes.size-1 downTo 0) {
            for (shapeIndex in layerManager.mShapes[layoutIndex].size-1 downTo 0) {
                if(layerManager.mShapes[layoutIndex][shapeIndex] is EraserShape){
                    layerManager.mShapes[layoutIndex][shapeIndex].endX=x
                    layerManager.mShapes[layoutIndex][shapeIndex].endY=y
                    return
                }
            }
        }
    }
    fun previous(){
        for (i in layerManager.mShapes.size-1 downTo 0){
            if(layerManager.mShapes[i].size!=0){
                layerManager.mShapes[i].removeLast()
                invalidate()
                break
            }
        }
    }
    private fun eraserToUp(){}

}