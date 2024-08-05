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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.example.studyinbetterlogin.viewmodel.MainViewModel
import com.example.studyinbetterlogin.viewmodel.MainViewModel.Companion.patternMap
import kotlin.math.min
class PatternUnlockView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val paint = Paint()
    private var circles = Array(3) { Array(3) { Circle(0f, 0f, false) } }
    val selectedCircles = mutableListOf<Pair<Int, Int>>()
    private var currentX = 0f
    private var currentY = 0f
    private var isDrawing = false
    private var linePaint = Paint()
    private var linePaint1 = Paint()
    private var circleRadius = 0f
    private lateinit var viewModel: MainViewModel
    data class Circle(var cx: Float, var cy: Float, var isSelected: Boolean)
    private var sum=0
    private var tagChangeListener: ((Any?) -> Unit)? = null
    private val errorPaint=Paint()
    init {
        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5f
        linePaint.color = Color.BLUE
        linePaint.style = Paint.Style.STROKE
        linePaint.strokeWidth = 10f
        linePaint1.color = Color.BLUE
        linePaint1.style = Paint.Style.STROKE
        linePaint1.strokeWidth = 10f
        errorPaint.color = Color.RED
        errorPaint.style = Paint.Style.STROKE
        errorPaint.strokeWidth = 15f
    }
    fun setTagChangeListener(listener: (Any?) -> Unit) {
        tagChangeListener = listener
    }
    override fun setTag(tag: Any?) {
        super.setTag(tag)
        tagChangeListener?.invoke(tag)
    }
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        findViewTreeViewModelStoreOwner()?.let {
            viewModel = ViewModelProvider(it).get(MainViewModel::class.java)
            Log.d("PatternUnlockView", "ViewModel initialized")
        } ?: run {
            Log.e("PatternUnlockView", "ViewModelStoreOwner is null")
        }
    }
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        circleRadius = min(w, h) / 10f
        for (i in 0..2) {
            for (j in 0..2) {
                val cx = w / 4 * (i + 1).toFloat()
                val cy = h / 4 * (j + 1).toFloat()
                circles[i][j] = Circle(cx, cy, false)
            }
        }
    }
    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (i in 0..2) {
            for (j in 0..2) {
                val circle = circles[i][j]
                canvas.drawCircle(circle.cx, circle.cy, circleRadius, paint)
                if (circle.isSelected) {
                    canvas.drawCircle(circle.cx, circle.cy, circleRadius, linePaint)
                }
            }
        }
        if (isDrawing && selectedCircles.isNotEmpty()) {
            var (lastX, lastY) = selectedCircles.first().let { circles[it.first][it.second].cx to circles[it.first][it.second].cy }
            for ((i, j) in selectedCircles) {
                val circle = circles[i][j]
                canvas.drawLine(lastX, lastY, circle.cx, circle.cy, linePaint)
                lastX = circle.cx
                lastY = circle.cy
            }
            canvas.drawLine(lastX, lastY, currentX, currentY, linePaint)
        }
    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        currentX = event.x
        currentY = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                selectedCircles.clear()
                isDrawing = true
                resetCircles()
                checkSelectedCircle(currentX, currentY)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                if (isDrawing) {
                    checkSelectedCircle(currentX, currentY)
                    invalidate()
                }
            }
            MotionEvent.ACTION_UP -> {
                if (isDrawing) {
                    for((i,j) in selectedCircles)
                    {
                        Log.d("aaad","$i $j")
                    }
                    if(sum%2==0){
                        tag="1"
                    }else
                    {
                        tag="2"
                    }
                    sum++
                    isDrawing = false
                    viewModel.updatePatternData(selectedCircles.toList())
                    selectedCircles.clear()
                    invalidate()
                }
            }
        }
        return true
    }
    fun inErrorView() {
        linePaint=errorPaint
    }
    fun outErrorView(){
        Log.d("LoginByParrernFragment1", "Delay finished, calling outErrorView")

        linePaint=paint
        linePaint=linePaint1
        invalidate()

    }
    fun setCircles(){
        for(i in 0..2){
            for(j in 0..2){
                circles[i][j].isSelected=false
            }
        }
        invalidate()
    }
    private fun checkSelectedCircle(currentX: Float, currentY: Float) {
        for (i in 0..2) {
            for (j in 0..2) {
                val circle = circles[i][j]
                if (!circle.isSelected && Math.hypot((currentX - circle.cx).toDouble(), (currentY - circle.cy).toDouble()) <= circleRadius) {
                    circle.isSelected = true
                    selectedCircles.add(Pair(i, j))
                }
            }
        }
    }
    fun resetCircles() {
        for (i in 0..2) {
            for (j in 0..2) {
                circles[i][j].isSelected = false
            }
        }
        selectedCircles.clear()
        invalidate()
    }
}
