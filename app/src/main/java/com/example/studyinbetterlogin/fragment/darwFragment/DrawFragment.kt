package com.example.studyinbetterlogin.fragment.darwFragment

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
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
import com.example.studyinbetterlogin.shapes.TextShape
import com.example.studyinbetterlogin.shapes.TriangleShape
import com.example.studyinbetterlogin.utils.FileUtil
import com.example.studyinbetterlogin.view.ViewListener.OnDrawingViewTextChangeListener
import com.example.studyinbetterlogin.viewmodel.MainViewModel
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class DrawFragment : BaseFragment<FragmentDrawBinding>(){
    private val mViewModel: MainViewModel by activityViewModels()

    override fun initBinding(): FragmentDrawBinding {
        return  FragmentDrawBinding.inflate(layoutInflater)
    }

    override fun initView() {
        val colorPickerView=mBinding.colorPickerView
        val fileUtil=FileUtil()
        super.initView()
        val thisPaint: Paint = Paint().apply {
            color = Color.WHITE
            strokeWidth = 10f
            style = Paint.Style.STROKE
        }
        mBinding.drawBoard.loadPaint(thisPaint)
        mBinding.Back.setOnClickListener{
            findNavController().navigate(R.id.action_drawFragment_to_chooseBoardFragment)
        }
        var isMoved = false
        mBinding.selectDrawTools.setOnClickListener {
            if (colorPickerView.visibility == View.VISIBLE){
                colorPickerView.visibility = View.GONE
            }
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
            mBinding.drawBoard.previous()
        }
        mBinding.red.setOnClickListener{
            val backgroundColor = (mBinding.red.background as ColorDrawable).color
            thisPaint.color = backgroundColor
            mBinding.drawBoard.loadPaint(thisPaint)
        }
        mBinding.white.setOnClickListener{
            val backgroundColor = (mBinding.white.background as ColorDrawable).color
            thisPaint.color = backgroundColor
            mBinding.drawBoard.loadPaint(thisPaint)
        }
        mBinding.grey.setOnClickListener{
            val backgroundColor = (mBinding.grey.background as ColorDrawable).color
            thisPaint.color = backgroundColor
            mBinding.drawBoard.loadPaint(thisPaint)
        }
        mBinding.cyan.setOnClickListener{
            val backgroundColor = (mBinding.cyan.background as ColorDrawable).color
            thisPaint.color = backgroundColor
            mBinding.drawBoard.loadPaint(thisPaint)
        }
        mBinding.green.setOnClickListener{
            val backgroundColor = (mBinding.green.background as ColorDrawable).color
            thisPaint.color = backgroundColor
            mBinding.drawBoard.loadPaint(thisPaint)
        }
        mBinding.purple.setOnClickListener{
            val backgroundColor = (mBinding.purple.background as ColorDrawable).color
            thisPaint.color = backgroundColor
            mBinding.drawBoard.loadPaint(thisPaint)
        }
        val minStrokeWidth = 5f
        val maxStrokeWidth = 40f
        val customSeekBar= mBinding.customSeekBar
        customSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val strokeWidth = minStrokeWidth + (progress / 100f) * (maxStrokeWidth - minStrokeWidth)
                thisPaint.strokeWidth = strokeWidth
                mBinding.drawBoard.loadPaint(thisPaint)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // 可选：处理触摸开始
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // 可选：处理触摸结束
            }
        })
        mBinding.Fill.setOnClickListener{
            mBinding.drawBoard.setState("isFill")
        }
        mBinding.move.setOnClickListener{
            mBinding.drawBoard.setState("isMove")
        }
        mBinding.eraser.setOnClickListener{
            mBinding.drawBoard.setState("isEraser")
        }
        mBinding.save.setOnClickListener{
            context?.let { it1 -> fileUtil.saveDrawViewToUserFolder(it1,mBinding.drawBoard,mViewModel.Logged_user.value!!) }
        }
        mBinding.addLayout.setOnClickListener{
            mBinding.drawBoard.layerManager.addLayer()
        }
        mBinding.removeLayer.setOnClickListener{
            mBinding.drawBoard.layerManager.removeLayer()
            mBinding.drawBoard.invalidate()
        }
        mBinding.changeLayer.setOnClickListener{
            val inflater = LayoutInflater.from(context)
            val dialogView = inflater.inflate(R.layout.dialog_input, null)
            val editText1 = dialogView.findViewById<EditText>(R.id.editText1)
            val editText2 = dialogView.findViewById<EditText>(R.id.editText2)
            val textView=dialogView.findViewById<TextView>(R.id.LayoutCount)
            textView.text="当前有${mBinding.drawBoard.layerManager.mShapes.size}个图层"
            val dialog = AlertDialog.Builder(context)
                .setTitle("输入数据")
                .setView(dialogView)
                .setPositiveButton("确定") { _, _ ->
                    if(editText1.text.isEmpty()||editText2.text.isEmpty()){
                        Toast.makeText(context,"请输入每个框框，拜托了", Toast.LENGTH_LONG).show()
                    }else{
                        val input1 = editText1.text.toString().toInt()
                        val input2 = editText2.text.toString().toInt()
                        mBinding.drawBoard.invalidate()
                        if(!mBinding.drawBoard.layerManager.ChangeLayer(input1-1,input2-1)){
                            Toast.makeText(context,"输入的索引溢出", Toast.LENGTH_LONG).show()
                        }
                    }

                }
                .setNegativeButton("取消", null)
                .create()
            dialog.show()
        }
        colorPickerView.visibility = View.GONE
        mBinding.palette.setOnClickListener{
            if(colorPickerView.visibility == View.GONE){
                colorPickerView.visibility = View.VISIBLE
                colorPickerView.setColorListener(ColorEnvelopeListener { envelope, fromUser ->
                    thisPaint.color=envelope.color
                    mBinding.drawBoard.loadPaint(thisPaint)
                })
            }else if (colorPickerView.visibility == View.VISIBLE){
                colorPickerView.visibility = View.GONE
            }
        }
        mBinding.textShape.setOnClickListener{
            mBinding.drawBoard.setState("isText")
        }
        mBinding.drawBoard.setTextChangeListener(object :OnDrawingViewTextChangeListener{
            override fun onDrawingViewTextChanged(textTag: Boolean) {
                Log.d("DrawFragment","isdialog")
                val inflater = LayoutInflater.from(context)
                val dialogView = inflater.inflate(R.layout.dialog_input, null)
                val editText1 = dialogView.findViewById<EditText>(R.id.editText1)
                val editText2 = dialogView.findViewById<EditText>(R.id.editText2)
                editText2.visibility=View.GONE
                editText1.inputType=InputType.TYPE_CLASS_TEXT
                editText1.hint="输入你喜欢的文本吧！"
                val textView=dialogView.findViewById<TextView>(R.id.LayoutCount)
                textView.visibility=View.GONE
                val dialog = AlertDialog.Builder(context)
                    .setTitle("输入你要输入的文本")
                    .setView(dialogView)
                    .setPositiveButton("确定") { _, _ ->
                        val input1 = editText1.text.toString()
                        if(input1 == ""||input1.isEmpty()){
                            Toast.makeText(context,"输入为空，不改变", Toast.LENGTH_LONG).show()
                        }else{
                            (mBinding.drawBoard.thisTextShape!! as TextShape).text=input1
                            mBinding.drawBoard.invalidate()
                        }
                    }
                    .setNegativeButton("取消", null)
                    .create()
                dialog.show()
            }
        })
        mBinding.clickToSelect.setOnClickListener{
            mBinding.drawBoard.setState("clickToSelect")
        }
    }
}










