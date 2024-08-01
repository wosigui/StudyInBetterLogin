package com.example.studyinbetterlogin.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.studyinbetterlogin.R
import com.example.studyinbetterlogin.databinding.FragmentLoginBinding
import com.example.studyinbetterlogin.databinding.FragmentPattrenragmentBinding
import com.example.studyinbetterlogin.databinding.FragmentSurePatternBinding
import com.example.studyinbetterlogin.viewmodel.MainViewModel
import com.example.studyinbetterlogin.viewmodel.MainViewModel.Companion.patternMap

class Pattrenragment : BaseFragment<FragmentPattrenragmentBinding>() {
    private val mViewModel: MainViewModel by activityViewModels()

    override fun initBinding(): FragmentPattrenragmentBinding {
        return FragmentPattrenragmentBinding.inflate(layoutInflater)
    }

    override fun initView() {
        Log.d("Pattrenragment", "onViewCreated called")
        mBinding.patternUnlockView.setTagChangeListener { newTag ->
            Log.d("Pattrenragment", "Tag changed: $newTag")
            var s =""
            for((i,j) in mBinding.patternUnlockView.selectedCircles)
            {
                s+= patternMap[Pair(i,j)]
            }
            Log.d("MainViewModel1", s)
            val currentList = mViewModel.inSaveUser.value!!
            currentList.add(s)
            mViewModel.inSaveUser.value = currentList
            findNavController().navigate(R.id.action_pattrenragment_to_surePatternFragment)
        }
    }
}
