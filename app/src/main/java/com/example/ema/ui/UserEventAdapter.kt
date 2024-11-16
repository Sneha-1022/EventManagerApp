package com.example.ema.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ema.R
import com.example.ema.data.dao.RegistrationDao
import com.example.ema.data.entities.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserEventAdapter(
    private val events: List<Event>,
    private val currentUserId: Int,
    private val registrationDao: RegistrationDao,
    private val onToggleRegistration: (Event, Boolean) -> Unit // Handle registration toggle
) : RecyclerView.Adapter<UserEventAdapter.UserEventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserEventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return UserEventViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserEventViewHolder, position: Int) {
        val event = events[position]
        holder.bind(event)
        holder.registerButton.setOnClickListener {
            // Toggle registration based on current status
            CoroutineScope(Dispatchers.IO).launch {
                val isRegistered = registrationDao.isUserRegisteredForEvent(currentUserId, event.id)
                withContext(Dispatchers.Main) {
                    onToggleRegistration(event, isRegistered)
                }
            }
        }
    }

    override fun getItemCount(): Int = events.size

    inner class UserEventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val eventName: TextView = view.findViewById(R.id.eventName)
        private val eventDate: TextView = view.findViewById(R.id.eventDate)
        private val eventSeats: TextView = view.findViewById(R.id.eventSeats)
        val registerButton: Button = view.findViewById(R.id.registerButton)

        @SuppressLint("SetTextI18n")
        fun bind(event: Event) {
            eventName.text = event.name
            eventDate.text = event.date
            eventSeats.text = "Seats Available: ${event.availableSeats}"

            // Check registration status and set button text
            CoroutineScope(Dispatchers.IO).launch {
                val isRegistered = registrationDao.isUserRegisteredForEvent(currentUserId, event.id)
                withContext(Dispatchers.Main) {
                    registerButton.text = if (isRegistered) "Deregister" else "Register"
                }
            }
        }
    }
}
