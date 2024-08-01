package com.example.studyinbetterlogin.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.studyinbetterlogin.R
import com.example.studyinbetterlogin.databinding.FragmentLoginBinding
import com.example.studyinbetterlogin.viewmodel.MainViewModel

class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    private val mViewModel: MainViewModel by activityViewModels()

    override fun initBinding(): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(layoutInflater)
    }

    override fun initView() {
        // 观察 userList 的变化
        mViewModel.userList.observe(viewLifecycleOwner, Observer { users ->
            if (users != null) {
                Log.d("LoginFragment", "Observed users data: $users")
            } else {
                Log.e("LoginFragment", "User list is null")
            }
        })

        mBinding.register.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        mBinding.ToPatternPassword.setOnClickListener{
            //mViewModel.deleteAll()
            findNavController().navigate(R.id.action_loginFragment_to_loginByParrernFragment)
        }
        mBinding.ToLoginIn.setOnClickListener {
            val account = mBinding.Account.text.toString()
            val password = mBinding.editTextTextPassword.text.toString()

            when {
                account.isEmpty() -> {
                    showAlert("提示", "请输入账号")
                }
                password.isEmpty() -> {
                    showAlert("提示", "请输入密码")
                }
                else -> {
                    val userList = mViewModel.userList.value
                    if (userList == null) {
                        Log.e("LoginFragment", "User list is null")
                        showAlert("错误", "用户列表为空，请稍后重试")
                        return@setOnClickListener
                    }

                    if (userList.isEmpty()) {
                        Log.e("LoginFragment", "User list is empty")
                        showAlert("错误", "用户列表为空，请稍后重试")
                        return@setOnClickListener
                    }

                    for (user in userList) {
                        Log.d("LoginFragment", "User: ${user.id}, ${user.account}, ${user.password}, ${user.pattrenPassword}")
                    }

                    if (userList.any { user -> user.account == account && user.password == password }) {
                        Toast.makeText(requireContext(), "登入成功", Toast.LENGTH_SHORT).show()
                    } else {
                        showAlert("提示", "请输入正确的账号和密码")
                    }
                }
            }
        }
    }

    private fun showAlert(title: String, message: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("确定") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}

