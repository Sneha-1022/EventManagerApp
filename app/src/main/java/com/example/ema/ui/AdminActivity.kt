package com.example.ema.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ema.R
import com.example.ema.data.AppDatabase
import com.example.ema.data.dao.EventDao
import com.example.ema.data.entities.Event
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AdminActivity : AppCompatActivity() {

    private lateinit var eventDao: EventDao
    private lateinit var recyclerView: RecyclerView
    private lateinit var eventAdapter: AdminEventAdapter
    private val events = mutableListOf<Event>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        val db = AppDatabase.getDatabase(this)
        eventDao = db.eventDao()

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize AdminEventAdapter
        eventAdapter = AdminEventAdapter(
            events = events,
            onDeleteEvent = { event -> deleteEvent(event) }
        )

        recyclerView.adapter = eventAdapter

        fetchEvents()

        // Set up FloatingActionButton to add new events
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            val intent = Intent(this, AddEventActivity::class.java)
            startActivityForResult(intent, REQUEST_ADD_EVENT)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchEvents() {
        eventDao.getAllEvents().observe(this, Observer { fetchedEvents ->
            events.clear()
            events.addAll(fetchedEvents)
            eventAdapter.notifyDataSetChanged()
        })
    }

    private fun deleteEvent(event: Event) {
        CoroutineScope(Dispatchers.IO).launch {
            eventDao.deleteEvent(event.id)
            withContext(Dispatchers.Main) {
                fetchEvents()
                Toast.makeText(this@AdminActivity, "Event deleted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ADD_EVENT && resultCode == RESULT_OK) {
            fetchEvents()
        }
    }

    companion object {
        const val REQUEST_ADD_EVENT = 1
    }
}
