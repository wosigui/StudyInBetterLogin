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
import com.example.studyinbetterlogin.databinding.FragmentRegisterBinding
import com.example.studyinbetterlogin.databinding.FragmentSurePatternBinding
import com.example.studyinbetterlogin.db.User
import com.example.studyinbetterlogin.viewmodel.MainViewModel
import com.example.studyinbetterlogin.viewmodel.MainViewModel.Companion.patternMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

var sum=0
class SurePatternFragment : BaseFragment<FragmentSurePatternBinding>() {
    private val mViewModel: MainViewModel by activityViewModels()

    override fun initBinding(): FragmentSurePatternBinding {
        return FragmentSurePatternBinding.inflate(layoutInflater)
    }

    override fun initView() {

            mBinding.patternUnlockView.setTagChangeListener {
                var s = ""
                for ((i, j) in mBinding.patternUnlockView.selectedCircles) {
                    s += patternMap[Pair(i, j)]
                }
                fun showAlert(title: String, message: String) {
                    AlertDialog.Builder(requireContext())
                        .setTitle(title)
                        .setMessage(message)
                        .setPositiveButton("确定") { dialog, _ ->
                            mBinding.patternUnlockView.setCircles()
                            dialog.dismiss()
                        }
                        .show()
                }
                val currentList = mViewModel.inSaveUser.value ?: mutableListOf()
                currentList.add(s)
                mViewModel.inSaveUser.value = currentList
                if (mViewModel.inSaveUser.value!![2] == mViewModel.inSaveUser.value!![3]) {
                    val user = User(0, mViewModel.inSaveUser.value!![0], mViewModel.inSaveUser.value!![1], mViewModel.inSaveUser.value!![2])
                    mViewModel.saveUser(user)
                    Toast.makeText(requireContext(), "注册成功", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_surePatternFragment_to_loginFragment)
                } else {
                    showAlert("提示","请输入正确的图案密码")
                    mBinding.patternUnlockView.inErrorView()
                    currentList.removeLast()
                }
            }

    }
}
