package com.example.ema.data.dao

import com.example.ema.data.entities.User
import com.example.ema.data.entities.UserEvent
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    suspend fun authenticate(username: String, password: String): User?

    @Query("SELECT * FROM user_events WHERE userId = :userId AND eventId = :eventId")
    suspend fun isUserRegistered(userId: Int, eventId: Int): UserEvent?

    @Insert
    suspend fun registerUserEvent(userEvent: UserEvent)

    @Query("DELETE FROM user_events WHERE userId = :userId AND eventId = :eventId")
    suspend fun deregisterUserEvent(userId: Int, eventId: Int)

    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getUserByUsername(username: String): User?

}
