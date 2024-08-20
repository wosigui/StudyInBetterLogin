package com.example.studyinbetterlogin.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.studyinbetterlogin.db.Repository
import com.example.studyinbetterlogin.db.User


class LoginViewModel(private val repository: Repository) : ViewModel() {

    private val _loggedInUser = MutableLiveData<String>()
    val loggedInUser: LiveData<String> get() = _loggedInUser

    private val _userList = MediatorLiveData<List<User>>()

    init {
        _userList.addSource(repository.loadUsers()) { users ->
            _userList.value = users
            Log.d("LoginViewModel", "Users loaded: $users")
        }
        _userList.observeForever {
            // 观察用户列表
            Log.d("LoginViewModel", "Observed userList: $_userList")
        }
    }
    fun login(account: String, password: String) {
        val userList=_userList.value
        if (userList.isNullOrEmpty()) {
            // Handle empty or null user list
            return
        }

        val user = userList.find { it.account == account && it.password == password }
        if (user != null) {
            _loggedInUser.value = user.account
        } else {
            _loggedInUser.value=""
        }
    }

    fun loginByPattern(){}
}

class LoginViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}