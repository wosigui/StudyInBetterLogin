package com.example.studyinbetterlogin.fragment

import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.example.studyinbetterlogin.R
import com.example.studyinbetterlogin.api.Constants
import com.example.studyinbetterlogin.api.MovieApiInterface
import com.example.studyinbetterlogin.databinding.FragmentLoginBinding
import com.example.studyinbetterlogin.databinding.MovieItemLayoutBinding
import com.example.studyinbetterlogin.viewmodel.LoginViewModel
import com.example.studyinbetterlogin.viewmodel.MainViewModel
import com.example.studyinbetterlogin.db.Repository
import com.example.studyinbetterlogin.db.UserDatabase
import com.example.studyinbetterlogin.model.Data
import com.example.studyinbetterlogin.viewmodel.LoginViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    private val mViewModel: MainViewModel by activityViewModels()

    /**
     * 这个变量用于未来我在创建类似的数据的时候我可以复用
     */
    private val loginViewModel: LoginViewModel by viewModels {
        val userDao = UserDatabase.getDatabase(requireContext()).userDao()
        val repository = Repository(userDao)
        LoginViewModelFactory(repository)
    }
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
                    mViewModel.login(account, password)

                }
            }
        }
        mViewModel.loggedInUser.observe(viewLifecycleOwner, Observer { loggedInUser ->
            if (loggedInUser != "") {
                Toast.makeText(requireContext(), "登入成功", Toast.LENGTH_SHORT).show()
                mViewModel.Logged_user.value = loggedInUser
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.loginFragment, true)  // 清除之前的所有Fragment
                    .build()
                findNavController().navigate(R.id.action_loginFragment_to_loginToWaitFragment,null,navOptions)
            } else {
                showAlert("提示", "请输入正确的账号和密码")
            }
        })




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

