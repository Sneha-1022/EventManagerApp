package com.example.ema.data.dao

import com.example.ema.data.entities.Registration
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RegistrationDao {
    @Insert
    suspend fun registerForEvent(registration: Registration)

    @Query("SELECT COUNT(*) > 0 FROM registrations WHERE userId = :userId AND eventId = :eventId")
    suspend fun isUserRegisteredForEvent(userId: Int, eventId: Int): Boolean

    @Query("DELETE FROM registrations WHERE userId = :userId AND eventId = :eventId")
    suspend fun unregisterFromEvent(userId: Int, eventId: Int)

    @Query("SELECT * FROM registrations WHERE userId = :userId")
    suspend fun getUserRegistrations(userId: Int): List<Registration>
}
