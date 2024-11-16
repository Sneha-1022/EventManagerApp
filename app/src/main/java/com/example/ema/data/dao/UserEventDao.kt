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

    @Insert
    suspend fun insert(userEvent: UserEvent)

    @Update
    suspend fun update(userEvent: UserEvent)

    @Delete
    suspend fun delete(userEvent: UserEvent)

    @Query("SELECT * FROM user_events WHERE userId = :userId")
    fun getUserEvents(userId: Int): LiveData<List<UserEvent>>

    @Query("SELECT * FROM user_events WHERE eventId = :eventId")
    fun getEventUser(eventId: Int): LiveData<UserEvent>
}
