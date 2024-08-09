package com.example.studyinbetterlogin.fragment.darwFragment

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.studyinbetterlogin.R
import com.example.studyinbetterlogin.databinding.FragmentChooseBoardBinding
import com.example.studyinbetterlogin.databinding.FragmentLoginToWaitBinding
import com.example.studyinbetterlogin.fragment.BaseFragment
import com.example.studyinbetterlogin.viewmodel.MainViewModel
import java.io.File

class ChooseBoardFragment : BaseFragment<FragmentChooseBoardBinding>() {
    private val mViewModel: MainViewModel by activityViewModels()
    override fun initBinding(): FragmentChooseBoardBinding {
        return  FragmentChooseBoardBinding.inflate(layoutInflater)
    }

    override fun initView() {
        super.initView()
        mBinding.addBoard.setOnClickListener{
            findNavController().navigate(R.id.action_chooseBoardFragment_to_drawFragment)
        }
    }
}


fun displaySavedDrawView(context: Context, account: String, imageView: ImageView) {
    // 获取用户文件夹
    val userDir = getUserDirectory(context, account)

    // 找到保存的文件（例如最后保存的文件）
    val savedFile = File(userDir, "my.png") // 替换为实际文件名

    if (savedFile.exists()) {
        // 从文件中读取 Bitmap
        val bitmap = loadBitmapFromFile(savedFile.absolutePath)

        // 将 Bitmap 显示在 ImageView 中
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap)
        } else {
            Log.e("LoadBitmap", "Failed to load bitmap from file.")
        }
    } else {
        Log.e("LoadBitmap", "File does not exist.")
    }
}
fun loadBitmapFromFile(filePath: String): Bitmap? {
    return try {
        BitmapFactory.decodeFile(filePath)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}