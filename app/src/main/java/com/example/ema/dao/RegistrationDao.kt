// RegistrationDao.kt
package com.example.ema.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.ema.entity.Registration

@Dao
interface RegistrationDao {
    @Insert
    suspend fun register(registration: Registration)

    @Query("SELECT * FROM registrations WHERE userId = :userId")
    suspend fun getRegistrationsForUser(userId: Int): List<Registration>
}