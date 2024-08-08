package com.example.studyinbetterlogin.fragment.darwFragment

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.studyinbetterlogin.R
import com.example.studyinbetterlogin.databinding.FragmentDrawBinding
import com.example.studyinbetterlogin.databinding.FragmentLoginToWaitBinding
import com.example.studyinbetterlogin.fragment.BaseFragment
import com.example.studyinbetterlogin.shapes.CircleShape
import com.example.studyinbetterlogin.shapes.FreehandShape
import com.example.studyinbetterlogin.shapes.LayerManager
import com.example.studyinbetterlogin.shapes.LineShape
import com.example.studyinbetterlogin.shapes.OvalShape
import com.example.studyinbetterlogin.shapes.RectangleShape
import com.example.studyinbetterlogin.shapes.TriangleShape
import com.example.studyinbetterlogin.viewmodel.MainViewModel

class DrawFragment : BaseFragment<FragmentDrawBinding>() {
    private val mViewModel: MainViewModel by activityViewModels()

    override fun initBinding(): FragmentDrawBinding {
        return  FragmentDrawBinding.inflate(layoutInflater)
    }

    override fun initView() {
        super.initView()
        var thisPaint: Paint = Paint().apply {
            color = Color.WHITE
            strokeWidth = 10f
            style = Paint.Style.STROKE
        }

        mBinding.Back.setOnClickListener{
            findNavController().navigate(R.id.action_drawFragment_to_chooseBoardFragment)
        }
        var isMoved = false
        mBinding.selectDrawTools.setOnClickListener {
            if (isMoved) {
                mBinding.constraintLayout.animate().translationX(0f).setDuration(400).start()
            } else {
                mBinding.constraintLayout.animate().translationX(mBinding.constraintLayout.width - mBinding.selectDrawTools.width.toFloat()).setDuration(300).start()
            }
            isMoved = !isMoved
        }
        mBinding.drawCircle.setOnClickListener{
            mBinding.drawBoard.loadShape(OvalShape(-100f,-100f,thisPaint))
        }
        mBinding.clear.setOnClickListener{
            mBinding.drawBoard.layerManager.mShapes= mutableListOf(mutableListOf())
            mBinding.drawBoard.invalidate()
        }
        mBinding.RectangleShape.setOnClickListener{
            mBinding.drawBoard.loadShape(RectangleShape(-100f,-100f, thisPaint))
        }
        mBinding.TriangleShape.setOnClickListener{
            mBinding.drawBoard.loadShape(TriangleShape(-100f,-100f, thisPaint))
        }
        mBinding.LinePaint.setOnClickListener{
            mBinding.drawBoard.loadShape(LineShape(-100f,-100f, thisPaint))
        }
        mBinding.FreeHandShape.setOnClickListener{
            mBinding.drawBoard.loadShape(FreehandShape(-100f,-100f,thisPaint))
        }
        mBinding.previous.setOnClickListener{
            for (i in mBinding.drawBoard.layerManager.mShapes.size-1 downTo 0){
                if(mBinding.drawBoard.layerManager.mShapes[i].size!=0){
                    mBinding.drawBoard.layerManager.mShapes[i].removeLast()
                    mBinding.drawBoard.invalidate()
                    break
                }
            }
        }
    }

}