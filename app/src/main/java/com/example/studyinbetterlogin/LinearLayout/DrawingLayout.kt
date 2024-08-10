package com.example.studyinbetterlogin.LinearLayout

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.studyinbetterlogin.R


class CustomLinearLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        // 初始化布局
        initView(context)
    }

    private fun initView(context: Context) {
        // 将自定义布局文件加载到这个 LinearLayout 中
        LayoutInflater.from(context).inflate(R.layout.custom_linear_layout, this, true)

        // 在这里可以进行进一步的初始化操作，如设置背景色、添加事件监听器等
    }

    // 你可以根据需要重写更多方法，例如 onLayout、onDraw 等，进行更深层次的定制
}