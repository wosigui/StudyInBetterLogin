package com.example.studyinbetterlogin.shapes

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import kotlin.math.abs

abstract class Shape(
    var startX: Float,
    var startY: Float,
    var mPaint: Paint
) {
    open var endX: Float = startX
    open var endY: Float = startY
    abstract fun draw(canvas: Canvas)
    abstract fun isInside(x:Float,y:Float):Boolean
}