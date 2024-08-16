package com.example.studyinbetterlogin.shapes

import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log

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
                if (shape.isFrameShow){
                    shape.drawFrame(mCanvas)
                }
            }
        }
    }
    fun addLayer(){
        Log.d("AddLayer","yes")
        mShapes.add(mutableListOf())
    }
    fun removeLayer(){
        Log.d("removeLayer","yes")
        if(mShapes.size>1){
            mShapes.removeLast()
        }else{
            mShapes[0]=mutableListOf()
        }
    }

    /**
     * 必须穿从零开始的数组的索引，别跟我搞
     */
    fun ChangeLayer(thisLayerIndex:Int,thatLayerIndex:Int):Boolean{
        if(thisLayerIndex in 0..mShapes.size-1&&thatLayerIndex in 0..mShapes.size-1){
            val tem =mShapes[thisLayerIndex]
            mShapes[thisLayerIndex]=mShapes[thatLayerIndex]
            mShapes[thatLayerIndex]=tem
            return true
        }else{
            return false
        }
    }
}