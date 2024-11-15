package com.example.ema

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ema.dao.EventDao
import com.example.ema.dao.RegistrationDao
import com.example.ema.dao.UserDao
import com.example.ema.entity.Event
import com.example.ema.entity.Registration
import com.example.ema.entity.User

@Database(entities = [Event::class, User::class, Registration::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun userDao(): UserDao
    abstract fun registrationDao(): RegistrationDao
}