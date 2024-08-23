package com.example.studyinbetterlogin.db
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.studyinbetterlogin.api.MovieApiInstance
import com.example.studyinbetterlogin.model.Movies
import retrofit2.Response

class Repository (private val userDao: UserDao){
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
        Log.d("Repository", "Loading users from database")
        return userDao.loadUsers()
    }

    suspend fun getMovies(index:Int): Response<Movies> {
        return MovieApiInstance.api.getMovies(index)
    }
}