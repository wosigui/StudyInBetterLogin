package com.example.studyinbetterlogin.fragment.drawFragment

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.studyinbetterlogin.R
import com.example.studyinbetterlogin.adapter.DrawingAdapter
import com.example.studyinbetterlogin.databinding.FragmentChooseBoardBinding
import com.example.studyinbetterlogin.fragment.BaseFragment
import com.example.studyinbetterlogin.utils.FileUtil
import com.example.studyinbetterlogin.viewmodel.MainViewModel
import java.io.File

class ChooseBoardFragment : BaseFragment<FragmentChooseBoardBinding>() {
    private val mViewModel: MainViewModel by activityViewModels()

    override fun initBinding(): FragmentChooseBoardBinding {
        return FragmentChooseBoardBinding.inflate(layoutInflater)
    }

    override fun initView() {
        super.initView()
        val fileUtil=FileUtil()
        val recyclerView = mBinding.DrawableList
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // 加载用户目录中的所有 PNG 文件并转换为 Drawable 列表
        val drawableList = context?.let { fileUtil.getPngDrawablesInAccountDir(it, mViewModel.Logged_user.value!!) }
        if(!drawableList.isNullOrEmpty()){
            mBinding.helloWorld.visibility=View.INVISIBLE
        }
        val adapter = drawableList?.let { DrawingAdapter(it, mViewModel, recyclerView) }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val mid = recyclerView.width / 2.0f

                for (i in 0 until layoutManager.childCount) {
                    val child = layoutManager.getChildAt(i)
                    val childMid = (child!!.left + child.right) / 2.0f
                    val d = Math.abs(mid - childMid)
                    val scale = 1 - 0.2f * (d / mid)
                    child.scaleX = scale
                    child.scaleY = scale
                }
            }
        })

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)
        mBinding.addBoard.setOnClickListener {
            findNavController().navigate(R.id.action_chooseBoardFragment_to_drawFragment)
        }

        mBinding.shareBoard.setOnClickListener {
            mViewModel.Logged_user.value?.let { it1 ->
                context?.let { it2 ->
                    fileUtil.displaySavedDrawView(it2, it1, mBinding.savedImageView)
                }
            }
        }
        mBinding.deleteBoard.setOnClickListener{
            if(drawableList.isNullOrEmpty()){
                Toast.makeText(requireContext(), "没有照片用于删除", Toast.LENGTH_SHORT).show()
            }else{
                showAlert("警告","你确定要删除这张照片吗？",recyclerView)
            }
        }
        mBinding.downloadBoard.setOnClickListener{
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val adapter = recyclerView.adapter as DrawingAdapter
            val drawableItem = adapter.getItem(firstVisibleItemPosition) // 获取顶部的 DrawableItem
            val savedUri = context?.let { it1 -> fileUtil.saveFilePathToGallery(it1, drawableItem.filePath, "new_image.jpg") }
            savedUri?.let { context?.let { it1 -> fileUtil.notifyGallery(it1, it) } }
            Toast.makeText(requireContext(), "保存成功", Toast.LENGTH_SHORT).show()
        }
        mBinding.shareBoard.setOnClickListener{
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val adapter = recyclerView.adapter as DrawingAdapter
            val drawableItem = adapter.getItem(firstVisibleItemPosition) // 获取顶部的 DrawableItem

            // 先保存文件到相册
            val savedUri = context?.let { it1 -> fileUtil.saveFilePathToGallery(it1, drawableItem.filePath, "new_image.jpg") }

            if (savedUri != null) {
                // 确保图片已保存到相册
                Log.d("ShareImage", "Image saved to gallery: $savedUri")

                context?.let { it1 ->
                    fileUtil.notifyGallery(it1, savedUri)
                    fileUtil.shareImage(it1, savedUri) // 调用分享方法
                }
            } else {
                Log.e("ShareImage", "Failed to save image to gallery")
            }
        }
    }
    private fun showAlert(title: String, message: String, recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        if (firstVisibleItemPosition != RecyclerView.NO_POSITION) {
            val adapter = recyclerView.adapter as DrawingAdapter
            val drawableItem = adapter.getItem(firstVisibleItemPosition) // 获取顶部的 DrawableItem

            AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton("取消") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("确定") { dialog, _ ->
                    // 删除文件
                    val file = File(drawableItem.filePath)
                    if (file.exists()) {
                        file.delete()
                        Toast.makeText(requireContext(), "图片已删除", Toast.LENGTH_SHORT).show()
                        adapter.notifyItemRemoved(firstVisibleItemPosition)
                        adapter.removeItem(firstVisibleItemPosition)
                    } else {
                        Toast.makeText(requireContext(), "文件不存在", Toast.LENGTH_SHORT).show()
                    }
                    dialog.dismiss()
                }
                .show()
        }
    }

}

