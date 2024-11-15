package com.example.ema.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ema.R
import com.example.ema.data.entities.Event
import com.example.ema.data.dao.RegistrationDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class EventAdapter(
    private val events: List<Event>,
    private val isAdmin: Boolean,
    private val currentUserId: Int? = null, // Make currentUserId optional for admin
    private val onEventAction: (Event) -> Unit,  // Handle event click
    private val onRegisterAction: (Event) -> Unit  // Handle register/deregister action
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    private lateinit var registrationDao: RegistrationDao

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.bind(event)

        // Handle button clicks for registering/deregistering
        holder.registerButton.setOnClickListener {
            onRegisterAction(event)
        }

        // Handle event click (if needed)
        holder.itemView.setOnClickListener {
            onEventAction(event)
        }
    }

    override fun getItemCount(): Int = events.size

    inner class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val eventName: TextView = view.findViewById(R.id.eventName)
        private val eventDate: TextView = view.findViewById(R.id.eventDate)
        private val eventSeats: TextView = view.findViewById(R.id.eventSeats)
        val registerButton: Button = view.findViewById(R.id.registerButton)

        @SuppressLint("SetTextI18n")
        fun bind(event: Event) {
            eventName.text = event.name
            eventDate.text = event.date
            eventSeats.text = "Seats Available: ${event.availableSeats}"

            if (isAdmin) {
                // For admins, show "Delete" button
                registerButton.text = "Delete"
                registerButton.setBackgroundColor(itemView.context.getColor(R.color.colorDelete))  // Optional red background
            } else {
                // For users, check registration status and set button text accordingly
                checkUserRegistration(event) { isRegistered ->
                    registerButton.text = if (isRegistered) "Deregister" else "Register"
                }
            }
        }

        private fun checkUserRegistration(event: Event, callback: (Boolean) -> Unit) {
            // Make sure currentUserId is available for non-admin users
            currentUserId?.let { userId ->
                CoroutineScope(Dispatchers.IO).launch {
                    // Query RegistrationDao to check if the current user is registered for the event
                    val isRegistered = registrationDao.isUserRegisteredForEvent(userId, event.id)
                    withContext(Dispatchers.Main) {
                        callback(isRegistered)
                    }
                }
            }
        }
    }
}
