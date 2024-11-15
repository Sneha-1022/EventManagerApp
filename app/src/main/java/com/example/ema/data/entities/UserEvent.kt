package com.example.ema.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_events")
data class UserEvent(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val eventId: Int
)
