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
import com.example.studyinbetterlogin.shapes.TriangleShape
import com.example.studyinbetterlogin.viewmodel.MainViewModel
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class DrawFragment : BaseFragment<FragmentDrawBinding>() {
    private val mViewModel: MainViewModel by activityViewModels()

    override fun initBinding(): FragmentDrawBinding {
        return  FragmentDrawBinding.inflate(layoutInflater)
    }

    override fun initView() {
        val colorPickerView=mBinding.colorPickerView

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
            context?.let { it1 -> saveDrawViewToUserFolder(it1,mBinding.drawBoard,mViewModel.Logged_user.value!!) }
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
                    val input1 = editText1.text.toString().toInt()
                    val input2 = editText2.text.toString().toInt()
                    mBinding.drawBoard.invalidate()
                    if(!mBinding.drawBoard.layerManager.ChangeLayer(input1-1,input2-1)){
                        Toast.makeText(context,"输入的索引溢出", Toast.LENGTH_LONG).show()
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


    }

}





// 获取用户的文件夹
fun getUserDirectory(context: Context, account: String): File {
    val userDir = File(context.getExternalFilesDir(null), account)
    if (!userDir.exists()) {
        userDir.mkdirs()  // 如果文件夹不存在，则创建
    }
    return userDir
}

// 生成唯一的文件名
fun generateUniqueFileName(): String {
    // 获取当前时间戳
    val timestamp = System.currentTimeMillis()

    // 将时间戳转换为日期格式 yyyyMMdd_HHmmss
    val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
    val date = Date(timestamp)
    val formattedDate = sdf.format(date)

    // 使用格式化的日期生成唯一文件名
    return "my$formattedDate.png"
}

// 将 DrawView 保存到指定路径
fun saveDrawViewToUserFolder(context: Context, drawView: View, account: String) {
    val userDir = getUserDirectory(context, account)
    val fileName = generateUniqueFileName()
    val filePath = File(userDir, fileName).absolutePath

    val bitmap = getBitmapFromView(drawView)
    saveBitmapToFile(bitmap, filePath)

    Log.d("SaveDrawView", "DrawView saved to $filePath")
}

// 获取 View 的 Bitmap
fun getBitmapFromView(view: View): Bitmap {
    val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    view.draw(canvas)
    return bitmap
}

// 保存 Bitmap 到文件
fun saveBitmapToFile(bitmap: Bitmap, filePath: String) {
    val file = File(filePath)
    try {
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
    } catch (e: Exception) {
        e.printStackTrace()
        Log.e("SaveBitmap", "Failed to save bitmap: ${e.message}")
    }
}





