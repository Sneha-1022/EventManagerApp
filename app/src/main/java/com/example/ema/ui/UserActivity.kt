package com.example.ema.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ema.R
import com.example.ema.data.AppDatabase
import com.example.ema.data.dao.EventDao
import com.example.ema.data.dao.RegistrationDao
import com.example.ema.data.entities.Event
import com.example.ema.data.entities.Registration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserActivity : AppCompatActivity() {

    private lateinit var eventDao: EventDao
    private lateinit var registrationDao: RegistrationDao
    private lateinit var recyclerView: RecyclerView
    private lateinit var eventAdapter: UserEventAdapter
    private val events = mutableListOf<Event>()
    private var currentUserId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        currentUserId = intent.getIntExtra("USER_ID", 0)

        val db = AppDatabase.getDatabase(this)
        eventDao = db.eventDao()
        registrationDao = db.registrationDao()

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        eventAdapter = UserEventAdapter(
            events = events,
            currentUserId = currentUserId,
            registrationDao = registrationDao,
            onToggleRegistration = { event, isRegistered ->
                if (isRegistered) {
                    deregisterUser(event)
                } else {
                    registerUser(event)
                }
            }
        )

        recyclerView.adapter = eventAdapter
        fetchEvents()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchEvents() {
        lifecycleScope.launch {
            eventDao.getAllEvents().observe(this@UserActivity, Observer { fetchedEvents ->
                events.clear()
                events.addAll(fetchedEvents)
                eventAdapter.notifyDataSetChanged()
            })
        }
    }

    private fun registerUser(event: Event) {
        CoroutineScope(Dispatchers.IO).launch {
            if (event.availableSeats > 0) {
                registrationDao.registerForEvent(Registration(userId = currentUserId, eventId = event.id))
                eventDao.registerForEvent(event.id)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@UserActivity, "Registered for ${event.name}", Toast.LENGTH_SHORT).show()
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@UserActivity, "No seats available for ${event.name}", Toast.LENGTH_SHORT).show()
                }
            }
            fetchEvents()
        }
    }

    private fun deregisterUser(event: Event) {
        CoroutineScope(Dispatchers.IO).launch {
            registrationDao.unregisterFromEvent(currentUserId, event.id)
            eventDao.deregisterFromEvent(event.id)
            withContext(Dispatchers.Main) {
                Toast.makeText(this@UserActivity, "Unregistered from ${event.name}", Toast.LENGTH_SHORT).show()
            }
            fetchEvents()
        }
    }
}
