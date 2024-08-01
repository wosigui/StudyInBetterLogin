package com.example.studyinbetterlogin.db
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("DELETE FROM User")
    suspend fun deleteAll()

    @Update
    suspend fun updateUser(user: User)

    /**
     * 希望数据库数据跟新之后 第一时间被外部感知到
     * 选择LiveData
     *    Flow
     */
    @Query("SELECT * FROM User")
    fun loadUsers(): LiveData<List<User>>

}