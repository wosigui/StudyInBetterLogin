package com.example.studyinbetterlogin.fragment

import UserAdapter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studyinbetterlogin.R
import com.example.studyinbetterlogin.databinding.FragmentLoginByParrernBinding
import com.example.studyinbetterlogin.db.User
import com.example.studyinbetterlogin.viewmodel.MainViewModel
import com.example.studyinbetterlogin.viewmodel.MainViewModel.Companion.patternMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginByParrernFragment : BaseFragment<FragmentLoginByParrernBinding>() {
    private val mViewModel: MainViewModel by activityViewModels()

    override fun initBinding(): FragmentLoginByParrernBinding {
        return FragmentLoginByParrernBinding.inflate(layoutInflater)
    }

    override fun initView() {
        val recyclerView = mBinding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // 观察 userList
        mViewModel.userList.observe(viewLifecycleOwner, Observer { users ->
            if (users != null) {
                val adapter = UserAdapter(users, mViewModel,recyclerView)
                recyclerView.adapter = adapter
            } else {
                Log.e("LoginByParrernFragment", "User list is null")
            }
        })

        // 观察 userClickEvent
        mViewModel.userClickEvent.observe(viewLifecycleOwner, Observer { account ->
            account?.let {
                // 在这里处理点击事件，得到的账号是 account
                Toast.makeText(requireContext(), "Clicked on $account", Toast.LENGTH_SHORT).show()
            }
        })

        mBinding.textView11.setOnClickListener {
            findNavController().navigate(R.id.action_loginByParrernFragment_to_loginFragment)
        }

        mBinding.patternUnlockView.setTagChangeListener {
            var s = ""
            for ((i, j) in mBinding.patternUnlockView.selectedCircles) {
                s += patternMap[Pair(i, j)]
            }
            val account = mViewModel.LoginByParrern.value.toString()
            val password = s

            fun showAlert(title: String, message: String) {
                AlertDialog.Builder(requireContext())
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton("确定") { dialog, _ ->
                        mBinding.patternUnlockView.setCircles()
                        mBinding.patternUnlockView.outErrorView()
                        dialog.dismiss()
                    }
                    .show()
            }

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
                        return@setTagChangeListener
                    }

                    if (userList.isEmpty()) {
                        Log.e("LoginFragment", "User list is empty")
                        showAlert("错误", "用户列表为空，请稍后重试")
                        return@setTagChangeListener
                    }

                    for (user in userList) {
                        Log.d("LoginFragment", "User: ${user.id}, ${user.account}, ${user.password}, ${user.pattrenPassword}")
                        Log.d("LoginFragment", "$s ${user.pattrenPassword} ${user.account} $account")
                    }

                    if (userList.any { user -> user.account == account && user.pattrenPassword == password }) {
                        Toast.makeText(requireContext(), "登入成功", Toast.LENGTH_SHORT).show()
                    } else {
                        mBinding.patternUnlockView.inErrorView()
                        showAlert("提示", "请输入正确的账号和密码")
                    }
                }
            }
        }

        mViewModel.adapterShowOrNot.observe(viewLifecycleOwner) { isVisible ->
            if (!isVisible) {
                // 隐藏RecyclerView
                recyclerView.animate()
                    .translationY(recyclerView.height.toFloat())
                    .alpha(0.0f)
                    .setDuration(300)
                    .withEndAction {
                        recyclerView.visibility = View.GONE
                    }
            } else {
                // 显示RecyclerView
                recyclerView.visibility = View.VISIBLE
                recyclerView.alpha = 0.0f
                recyclerView.animate()
                    .translationY(0f)
                    .alpha(1.0f)
                    .setDuration(300)
            }
        }

        mBinding.displayAccount.setOnClickListener {
            val isVisible = mViewModel.adapterShowOrNot.value ?: true
            mViewModel.adapterShowOrNot.value = !isVisible
        }
    }
}
