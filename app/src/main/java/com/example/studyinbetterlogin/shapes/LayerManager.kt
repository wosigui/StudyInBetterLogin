package com.example.studyinbetterlogin.shapes

import android.graphics.Bitmap
import android.graphics.Canvas

class LayerManager{
    private lateinit var mBitmap: Bitmap
    var mShapes:MutableList<MutableList<Shape>> = mutableListOf(mutableListOf())
    fun addShape(shape:Shape){
        mShapes.last().add(shape)
    }
    fun endPoint(x:Float,y:Float){
        if (mShapes.isNotEmpty() && mShapes.last().isNotEmpty()) {
            val shape = mShapes.last().last()
            shape.endX = x
            shape.endY = y
        }
    }
    fun drawShape(mCanvas: Canvas){
        for (layer in mShapes) {
            for (shape in layer) {
                shape.draw(mCanvas)
            }
        }
    }
    fun addLayer(){
        mShapes.add(mutableListOf())
    }
    fun removeLayer(){
        if(mShapes.size>1){
            mShapes.removeLast()
        }
    }

}