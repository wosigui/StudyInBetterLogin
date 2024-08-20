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
import com.example.studyinbetterlogin.shapes.TextShape
import com.example.studyinbetterlogin.view.ViewListener.OnDrawingViewTextChangeListener
import kotlin.math.atan
import kotlin.math.atan2
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs)  {
    val layerManager: LayerManager = LayerManager()
    companion object{
        val LEFTTOP=0
        val RIGHTTOP=1
        val LEFTBOTTOM=2
        val RIGHTBOTTOM=3
        val ROTATE=4
        val NONE=-1
    }
    var thisCornerHandleType:Int = 0
    private var TextTag :Boolean = false
        set(value) {
            field = value
            drawingViewTextChangeListener?.onDrawingViewTextChanged(field)
        }
    private var isTextNeedMove:Boolean = true
    private var drawingViewTextChangeListener:OnDrawingViewTextChangeListener?=null
    fun setTextChangeListener(listener: OnDrawingViewTextChangeListener) {
        this.drawingViewTextChangeListener = listener
    }
    private val states: MutableMap<String, Boolean> = mutableMapOf(
        "isFill" to false,
        "isMove" to false,
        "isDraw" to true ,// 默认状态
        "isEraser" to false,
        "isText" to false,
        "clickToSelect" to false
    )
    fun thisState():String?{
        for (k in states.keys){
            if (states[k]==true)
                return k
        }
        return null
    }
    fun setState(key: String) {
        // 遍历所有键，将所有值设为 false
        if(thisState()=="clickToSelect"){
            clearShapeFrame()
            invalidate()
        }
        for (k in states.keys) {
            states[k] = false
        }
        // 将指定键的值设为 true
        states[key] = true
    }
    private lateinit var isMovingShape:Shape
    private lateinit var nextMovingShape:Shape
    private lateinit var isSelectingClickShape:Shape
    private lateinit var isSelectingClickToMoveShape:Shape
    private var isSelectingClickNeedToMoveShape:Boolean=false
    private var isNeedMove:Boolean = false
    private var isMovingX:Float?=null
    private var isMovingY:Float?=null
    var thisTextShape:Shape? = null
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
                if(states["isText"]==true){
                    val pairOfBooleanAndShape=isEventAtText(event.x,event.y)
                    if(pairOfBooleanAndShape.first){
                        TextTag=!TextTag
                        thisTextShape=pairOfBooleanAndShape.second
                        isTextNeedMove=false
                    }else{
                        layerManager.addShape(TextShape(event.x,event.y,currentPaint))
                        isTextNeedMove=true
                    }
                }else if(states["isFill"] == true){
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
                }else if(states["clickToSelect"]==true){
                    selectClickShape(event.x,event.y)
                    invalidate()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if(states["isText"]==true){
                    if(isTextNeedMove){
                        layerManager.endPoint(event.x,event.y)
                        invalidate()
                    }
                    return true
                }else if(states["isFill"] == true){
                    return true
                }else if(states["isMove"] == true){
                    if(isNeedMove){
                        moveSelectShape(event.x,event.y)
                        invalidate()
                    }
                    return true
                }else if(states["isEraser"]==true){
                    eraserToMove(event.x,event.y)
                    invalidate()
                }else if(states["isDraw"]==true){
                    layerManager.endPoint(event.x, event.y)
                    invalidate()
                }else if(states["clickToSelect"]==true){
                    if(isSelectingClickNeedToMoveShape){
                        if(thisCornerHandleType== ROTATE){
                            val CenterX = (isSelectingClickToMoveShape.startX + isSelectingClickToMoveShape.endX) / 2
                            val CenterY = (isSelectingClickToMoveShape.startY + isSelectingClickToMoveShape.endY) / 2
                            val deltaX = CenterX - event.x
                            val deltaY = CenterY - event.y
                            isSelectingClickToMoveShape.rotationAngle =
                                (Math.toDegrees(atan2(deltaY, deltaX).toDouble())).toFloat()-90f
                        }else{
                            moveSelectingClickNeedToMoveShape(event.x,event.y)
                        }
                        invalidate()
                    }
                }
            }
            MotionEvent.ACTION_UP ->{
                if(states["isDraw"]==true){
                    eraserToUp()
                }else if(states["isText"]==true){
                    if(isTextNeedMove){
                        TextTag=!TextTag
                        val pairOfBooleanAndShape=isEventAtText(event.x,event.y)
                        thisTextShape=pairOfBooleanAndShape.second
                        (layerManager.mShapes.last().last() as TextShape).isShowFrame=false
                        invalidate()
                    }
                }else if(states["clickToSelect"]==true){
                    thisCornerHandleType= NONE
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
        for (layoutIndex in layerManager.mShapes.size-1 downTo 0) {
            val layout=layerManager.mShapes[layoutIndex]
            for (shapeIndex in layout.size-1 downTo 0) {
                val shape=layout[shapeIndex]
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
        for (layoutIndex in layerManager.mShapes.size-1 downTo 0) {
            for (shapeIndex in layerManager.mShapes[layoutIndex].size-1 downTo 0) {
                if (layerManager.mShapes[layoutIndex][shapeIndex].isInside(x, y)) {
                    isMovingShape=layerManager.mShapes[layoutIndex][shapeIndex]
                    isMovingX=x
                    isMovingY=y
                    if(shapeIndex+1<layerManager.mShapes[layoutIndex].size){
                        nextMovingShape=layerManager.mShapes[layoutIndex][shapeIndex+1]
                    }
                    isNeedMove=true
                    return
                }
            }
        }
        isNeedMove=false
    }
    private fun moveSelectShape(x:Float,y:Float){
        if (!::isMovingShape.isInitialized) {
            Log.e("moveSelectShape", "isMovingShape is not initialized")
            return
        }
        val deltaX = x - (isMovingX ?: return)
        val deltaY = y - (isMovingY ?: return)
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
    fun isEventAtText(x:Float,y:Float):Pair<Boolean,Shape?>{
        for (layoutIndex in layerManager.mShapes.size-1 downTo 0) {
            for (shapeIndex in layerManager.mShapes[layoutIndex].size-1 downTo 0) {
                if(layerManager.mShapes[layoutIndex][shapeIndex] is TextShape&&layerManager.mShapes[layoutIndex][shapeIndex].isInside(x,y)){
                    return Pair(true,layerManager.mShapes[layoutIndex][shapeIndex])
                }
            }
        }
        return Pair(false,null)
    }
    fun selectClickShape(x:Float,y:Float):Boolean{
        for (layoutIndex in layerManager.mShapes.size-1 downTo 0) {
            for (shapeIndex in layerManager.mShapes[layoutIndex].size-1 downTo 0) {
                if(layerManager.mShapes[layoutIndex][shapeIndex].isFrameShow){
                    if(layerManager.mShapes[layoutIndex][shapeIndex].isInsideCornerHandle(x,y)){
                        isSelectingClickToMoveShape=layerManager.mShapes[layoutIndex][shapeIndex]
                        isSelectingClickNeedToMoveShape=true
                        when(isSelectingClickToMoveShape.thisrect){
                            isSelectingClickToMoveShape.rect1->{thisCornerHandleType= LEFTTOP}
                            isSelectingClickToMoveShape.rect2->{thisCornerHandleType= RIGHTTOP}
                            isSelectingClickToMoveShape.rect3->{thisCornerHandleType= LEFTBOTTOM}
                            isSelectingClickToMoveShape.rect4->{thisCornerHandleType= RIGHTBOTTOM}
                            isSelectingClickToMoveShape.rectRotate->{thisCornerHandleType= ROTATE}
                        }
                        break
                    }
                }
                if (layerManager.mShapes[layoutIndex][shapeIndex].isInside(x, y)) {
                    isSelectingClickShape=layerManager.mShapes[layoutIndex][shapeIndex]
                    layerManager.mShapes[layoutIndex][shapeIndex].isFrameShow=!layerManager.mShapes[layoutIndex][shapeIndex].isFrameShow
                    return true
                }
            }
        }
        return false
    }
    fun clearShapeFrame() {
        for (layoutIndex in layerManager.mShapes.size - 1 downTo 0) {
            for (shapeIndex in layerManager.mShapes[layoutIndex].size - 1 downTo 0) {
                isSelectingClickShape = layerManager.mShapes[layoutIndex][shapeIndex]
                layerManager.mShapes[layoutIndex][shapeIndex].isFrameShow = false
            }
        }
    }
    fun moveSelectingClickNeedToMoveShape(x:Float,y:Float){
        when(thisCornerHandleType){
            LEFTTOP->{
                if(x<isSelectingClickToMoveShape.endX&&y<isSelectingClickToMoveShape.endY){
                    isSelectingClickToMoveShape.startX=x
                    isSelectingClickToMoveShape.startY=y
                }
            }
            RIGHTTOP->{
                if(x>isSelectingClickToMoveShape.startX&&y<isSelectingClickToMoveShape.endY){
                    isSelectingClickToMoveShape.endX=x
                    isSelectingClickToMoveShape.startY=y
                }
            }
            LEFTBOTTOM->{
                if(x<isSelectingClickToMoveShape.endX&&y>isSelectingClickToMoveShape.startY) {
                    isSelectingClickToMoveShape.startX=x
                    isSelectingClickToMoveShape.endY=y
                }
            }
            RIGHTBOTTOM->{
                if(x>isSelectingClickToMoveShape.startX&&y>isSelectingClickToMoveShape.startY) {
                    isSelectingClickToMoveShape.endX = x
                    isSelectingClickToMoveShape.endY = y
                }
            }

        }

    }

    private fun eraserToUp(){}


}