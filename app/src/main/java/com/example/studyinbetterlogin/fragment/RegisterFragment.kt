package com.example.studyinbetterlogin.fragment

import android.accounts.Account
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.studyinbetterlogin.R
import com.example.studyinbetterlogin.databinding.FragmentRegisterBinding
import com.example.studyinbetterlogin.viewmodel.MainViewModel
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController

class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {
    private val mViewModel: MainViewModel by activityViewModels()

    override fun initBinding(): FragmentRegisterBinding {
        return FragmentRegisterBinding.inflate(layoutInflater)
    }

    override fun initView() {
        mBinding.register.setOnClickListener {
            val account = mBinding.Account.text.toString()
            val password = mBinding.Password.text.toString()
            val surePassword = mBinding.surePassword.text.toString()

            when {
                account.isEmpty() -> {
                    // 显示提示框
                    AlertDialog.Builder(requireContext())
                        .setTitle("提示")
                        .setMessage("请输入账号")
                        .setPositiveButton("确定") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
                password.isEmpty() -> {
                    AlertDialog.Builder(requireContext())
                        .setTitle("提示")
                        .setMessage("请输入密码")
                        .setPositiveButton("确定") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
                surePassword.isEmpty() -> {
                    AlertDialog.Builder(requireContext())
                        .setTitle("提示")
                        .setMessage("请输入确认密码")
                        .setPositiveButton("确定") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
                else -> {
                    if(password==surePassword){
                        Toast.makeText(requireContext(), "注册成功", Toast.LENGTH_SHORT).show()
                        val currentList = mViewModel.inSaveUser.value ?: mutableListOf()
                        currentList.add(account)
                        currentList.add(password)
                        mViewModel.inSaveUser.value=currentList
                        findNavController().navigate(R.id.action_registerFragment_to_pattrenragment)
                    }else{
                        AlertDialog.Builder(requireContext())
                            .setTitle("提示")
                            .setMessage("请确保你的密码喝确认密码相等")
                            .setPositiveButton("确定") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .show()
                    }
                }
            }
        }
    }
}
