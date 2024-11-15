package com.example.ema.data.dao

import com.example.ema.data.entities.Event
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: Event)

    @Query("SELECT * FROM events")
    fun getAllEvents(): LiveData<List<Event>>

    @Query("UPDATE events SET availableSeats = availableSeats - 1 WHERE id = :eventId AND availableSeats > 0")
    suspend fun registerForEvent(eventId: Int): Int

    @Query("UPDATE events SET availableSeats = availableSeats + 1 WHERE id = :eventId")
    suspend fun deregisterFromEvent(eventId: Int)

    @Query("DELETE FROM events WHERE id = :eventId")
    suspend fun deleteEvent(eventId: Int)
}