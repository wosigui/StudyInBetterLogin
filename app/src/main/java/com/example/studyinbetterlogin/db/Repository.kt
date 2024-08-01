package com.example.studyinbetterlogin.db
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData

class Repository (context: Context){
    private val userDao: UserDao = UserDatabase.getDatabase(context).userDao()
    suspend fun saveUser(user: User){
        Log.d("Repository", "User saved: ${user.account}")
        Log.d("Repository", "User saved: ${user.password}")
        userDao.insertUser(user)
    }

    suspend fun deleteUser(user: User){
        userDao.deleteUser(user)
    }

    suspend fun deleteAll(){
        userDao.deleteAll()
    }

    suspend fun updateUser(user: User){
        userDao.updateUser(user)
    }

    fun loadUsers(): LiveData<List<User>>{
        return userDao.loadUsers()
    }
}