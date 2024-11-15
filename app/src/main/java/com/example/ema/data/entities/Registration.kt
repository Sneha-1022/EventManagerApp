package com.example.ema.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "registrations",
    foreignKeys = [
        ForeignKey(entity = Event::class, parentColumns = ["id"], childColumns = ["eventId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["userId"], onDelete = ForeignKey.CASCADE)
    ])
data class Registration(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Int,
    val eventId: Int
)
