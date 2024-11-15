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
import com.example.ema.ui.adapter.EventAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserActivity : AppCompatActivity() {
    private lateinit var eventDao: EventDao
    private lateinit var registrationDao: RegistrationDao
    private lateinit var recyclerView: RecyclerView
    private lateinit var eventAdapter: EventAdapter
    private val events = mutableListOf<Event>()
    private var currentUserId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        // Get the user ID from the intent
        currentUserId = intent.getIntExtra("USER_ID", 0)

        // Initialize the database DAO
        val db = AppDatabase.getDatabase(this)
        eventDao = db.eventDao()
        registrationDao = db.registrationDao()

        // Set up the RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize EventAdapter with required callbacks
        eventAdapter = EventAdapter(
            events,
            isAdmin = false,  // Set isAdmin to false for UserActivity
            onEventAction = { event -> showEventDetails(event) },
            onRegisterAction = { event -> toggleRegistration(event) }
        )

        recyclerView.adapter = eventAdapter

        // Fetch events from the database
        fetchEvents()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchEvents() {
        lifecycleScope.launch {
            // Observe the events LiveData on the main thread
            eventDao.getAllEvents().observe(this@UserActivity, Observer { fetchedEvents ->
                events.clear()
                events.addAll(fetchedEvents)
                eventAdapter.notifyDataSetChanged()  // Notify the adapter about data change
            })
        }
    }

    private fun toggleRegistration(event: Event) {
        CoroutineScope(Dispatchers.IO).launch {
            val isRegistered = registrationDao.isUserRegisteredForEvent(currentUserId, event.id)
            if (isRegistered) {
                // Deregister the user if already registered
                registrationDao.unregisterFromEvent(currentUserId, event.id)
                eventDao.deregisterFromEvent(event.id)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@UserActivity, "Unregistered from ${event.name}", Toast.LENGTH_SHORT).show()
                }
            } else if (event.availableSeats > 0) {
                // Register the user if seats are available
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
            fetchEvents()  // Refresh events list after registration/unregistration
        }
    }

    private fun showEventDetails(event: Event) {
        // Handle event click to show event details if needed
        // You can open a new activity or show a dialog here with event details
    }
}
