package com.example.ema.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ema.R
import com.example.ema.data.entities.Event

class AdminEventAdapter(
    private val events: List<Event>,
    private val onDeleteEvent: (Event) -> Unit  // Action to delete an event
) : RecyclerView.Adapter<AdminEventAdapter.AdminEventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminEventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return AdminEventViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdminEventViewHolder, position: Int) {
        val event = events[position]
        holder.bind(event)
        holder.deleteButton.setOnClickListener {
            onDeleteEvent(event)  // Trigger delete action
        }
    }

    override fun getItemCount(): Int = events.size

    inner class AdminEventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val eventName: TextView = view.findViewById(R.id.eventName)
        private val eventDate: TextView = view.findViewById(R.id.eventDate)
        private val eventSeats: TextView = view.findViewById(R.id.eventSeats)
        val deleteButton: Button = view.findViewById(R.id.registerButton)  // Repurposed for delete

        @SuppressLint("SetTextI18n")
        fun bind(event: Event) {
            eventName.text = event.name
            eventDate.text = event.date
            eventSeats.text = "Seats Available: ${event.availableSeats}"
            deleteButton.text = "Delete"
        }
    }
}
