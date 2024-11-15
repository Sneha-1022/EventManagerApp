package com.example.ema.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.ema.entity.User

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    suspend fun getUser(username: String, password: String): User?
}