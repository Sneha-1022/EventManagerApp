package com.example.ema.data

import android.content.Context
import com.example.ema.data.entities.Event
import com.example.ema.data.dao.EventDao
import com.example.ema.data.entities.User
import com.example.ema.data.dao.UserDao
import com.example.ema.data.entities.UserEvent
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ema.data.dao.RegistrationDao
import com.example.ema.data.dao.UserEventDao
import com.example.ema.data.entities.Registration

@Database(entities = [Event::class, User::class, UserEvent::class, Registration::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun userDao(): UserDao
    abstract fun userEventDao(): UserEventDao
    abstract fun registrationDao(): RegistrationDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "event_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
