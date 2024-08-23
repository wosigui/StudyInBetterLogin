package com.example.studyinbetterlogin.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.studyinbetterlogin.db.Repository
import com.example.studyinbetterlogin.db.User
import com.example.studyinbetterlogin.db.UserDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {
    // 数据仓库
    var adapterShowOrNot:MutableLiveData<Boolean> = MutableLiveData(true)
    val repository = Repository(UserDatabase.getDatabase(application).userDao())
    // 保存数据
    val userList : LiveData<List<User>> = repository.loadUsers()
    var inSaveUser: MutableLiveData<MutableList<String>?> = MutableLiveData(mutableListOf())
    private val _userClickEvent = MutableLiveData<String>()
    val userClickEvent: MutableLiveData<String> get() = _userClickEvent
    val LoginByParrern:MutableLiveData<String>  = MutableLiveData()
    val Logged_user:MutableLiveData<String>  = MutableLiveData()
    private val _loggedInUser = MutableLiveData<String>()
    val loggedInUser: LiveData<String> get() = _loggedInUser
    private val thisUserList= MediatorLiveData<List<User>>()
    init {
        thisUserList.addSource(repository.loadUsers()){ users ->
            thisUserList.value = users
            Log.d("MainViewModel", "Users loaded: $users")
        }
        thisUserList.observeForever{
            Log.d("MainViewModel", "Observed userList: $thisUserList")
        }
    }
    companion object {
        val patternMap: MutableMap<Pair<Int, Int>, String> = mutableMapOf(
            Pair(0, 0) to "1",
            Pair(0, 1) to "2",
            Pair(0, 2) to "3",
            Pair(1, 0) to "4",
            Pair(1, 1) to "5",
            Pair(1, 2) to "6",
            Pair(2, 0) to "7",
            Pair(2, 1) to "8",
            Pair(2, 2) to "9"
        )
    }
    fun login(account: String, password: String) {
        val userList=thisUserList.value
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

    fun loginByPattern(account: String, patternPassword: String) {
        val userList=thisUserList.value
        if (userList.isNullOrEmpty()) {
            // Handle empty or null user list
            return
        }

        val user = userList.find { it.account == account && it.pattrenPassword == patternPassword }
        if (user != null) {
            _loggedInUser.value = user.account
        } else {
            _loggedInUser.value=""
        }
    }

    fun updatePatternData(data: List<Pair<Int, Int>>) {
        var s =""
        for((i,j) in data)
        {
            s+= patternMap[Pair(i,j)]
        }
        Log.d("MainViewModel1", s)
        val currentList = inSaveUser.value!!
        currentList.add(s)
        inSaveUser.value = currentList
        for(i in currentList)
        {
            Log.d("MainViewModel", "Updated pattern data: $i")
        }
    }

    fun saveUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveUser(user)
            Log.d("MainViewModel", "User saved: ${user.account}")
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteUser(user)
            Log.d("MainViewModel", "User deleted: ${user.account}")
        }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
            Log.d("MainViewModel", "All users deleted")
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUser(user)
            Log.d("MainViewModel", "User updated: ${user.account}")
        }
    }
}
