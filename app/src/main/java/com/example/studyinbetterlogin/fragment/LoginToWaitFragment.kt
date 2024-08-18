package com.example.studyinbetterlogin.fragment

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.studyinbetterlogin.R
import com.example.studyinbetterlogin.databinding.FragmentLoginToWaitBinding
import com.example.studyinbetterlogin.viewmodel.MainViewModel

class loginToWaitFragment : BaseFragment<FragmentLoginToWaitBinding>() {
    private val mViewModel: MainViewModel by activityViewModels()
    override fun initBinding(): FragmentLoginToWaitBinding {
        return  FragmentLoginToWaitBinding.inflate(layoutInflater)
    }

    override fun initView() {
        super.initView()
        mBinding.WelcomeText.text="欢迎回来，"+mViewModel.Logged_user.value
        mBinding.magicDraw.setOnClickListener{
            findNavController().navigate(R.id.action_loginToWaitFragment_to_chooseBoardFragment)
        }
    }
}