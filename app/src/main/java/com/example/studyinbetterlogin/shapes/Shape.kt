package com.example.studyinbetterlogin.shapes

import android.graphics.Canvas
import android.graphics.Paint

abstract class Shape(
    var startX: Float,
    var startY: Float,
    var mPaint: Paint
) {
    var endX: Float = startX
    var endY: Float = startY
    abstract fun draw(canvas: Canvas)
    abstract fun isInside(x:Float,y:Float):Boolean

}