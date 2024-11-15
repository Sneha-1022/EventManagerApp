package com.example.ema.adapter

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ema.R
import com.example.ema.entity.Event

class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val title: TextView = itemView.findViewById(R.id.eventTitle)
    private val description: TextView = itemView.findViewById(R.id.eventDescription)
    private val date: TextView = itemView.findViewById(R.id.eventDate)

    @SuppressLint("SetTextI18n")
    fun bind(event: Event) {
        title.text = event.title
        description.text = event.description
        date.text = event.date.toString()
    }
}