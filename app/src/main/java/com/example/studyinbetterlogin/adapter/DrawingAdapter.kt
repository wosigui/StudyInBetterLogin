package com.example.studyinbetterlogin.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.studyinbetterlogin.databinding.CustomLinearLayoutBinding
import com.example.studyinbetterlogin.viewmodel.MainViewModel

class DrawingAdapter(
    private val drawingList: MutableList<DrawableItem>,
    private val viewModel: MainViewModel,
    private val recyclerView: RecyclerView
) : RecyclerView.Adapter<DrawingAdapter.MyViewHolder>() {

    class MyViewHolder(val binding: CustomLinearLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(drawing: DrawableItem, viewModel: MainViewModel, recyclerView: RecyclerView) {
            binding.imageView.background = drawing.drawable  // 绑定 Drawable 作为背景到 ImageView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // 使用 LayoutInflater 来加载自定义的布局
        val binding = CustomLinearLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        // 返回列表项的数量
        return drawingList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // 获取当前位置的项，并绑定到 ViewHolder
        val drawing = drawingList[position]
        holder.bind(drawing, viewModel, recyclerView)
    }
    fun getItem(position: Int): DrawableItem {
        return drawingList[position]
    }
    fun removeItem(position: Int) {
        drawingList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, drawingList.size)  // 通知 RecyclerView 更新
    }
}
