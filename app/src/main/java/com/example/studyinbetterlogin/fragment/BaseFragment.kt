package com.example.studyinbetterlogin.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding


abstract class BaseFragment<T: ViewBinding>: Fragment() {
    //这个属性只能在当前类里面访问
    //子类无法继承
    private var _binding: T? = null
    //提供给外部或者子类一个不可变的类型
    val mBinding: T get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = initBinding()
        initView()
        return _binding!!.root
    }

    abstract fun initBinding():T
    open fun initView(){}
}