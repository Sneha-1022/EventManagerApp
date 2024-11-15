package com.example.ema.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.ema.data.entities.UserEvent

@Dao
interface UserEventDao {

    // Insert a UserEvent into the database
    @Insert
    suspend fun insert(userEvent: UserEvent)

    // Update a UserEvent in the database
    @Update
    suspend fun update(userEvent: UserEvent)

    // Delete a UserEvent from the database
    @Delete
    suspend fun delete(userEvent: UserEvent)

    // Get all UserEvents for a specific user (can be adjusted based on your requirements)
    @Query("SELECT * FROM user_events WHERE userId = :userId")
    fun getUserEvents(userId: Int): LiveData<List<UserEvent>>

    // Get a specific UserEvent by eventId (can be adjusted as needed)
    @Query("SELECT * FROM user_events WHERE eventId = :eventId")
    fun getEventUser(eventId: Int): LiveData<UserEvent>
}
