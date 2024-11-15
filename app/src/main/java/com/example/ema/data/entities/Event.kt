package com.example.ema.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val date: String,
    val location: String,
    val totalSeats: Int,
    val availableSeats: Int
)
