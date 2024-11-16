package com.example.ema.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ema.R
import com.example.ema.data.AppDatabase
import com.example.ema.data.dao.EventDao
import com.example.ema.data.entities.Event
import com.example.ema.utils.SessionManager
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
    private lateinit var logout: Button
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        sessionManager = SessionManager(this)
        val db = AppDatabase.getDatabase(this)
        eventDao = db.eventDao()

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        eventAdapter = AdminEventAdapter(
            events = events,
            onDeleteEvent = { event -> deleteEvent(event) }
        )

        recyclerView.adapter = eventAdapter

        fetchEvents()

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            val intent = Intent(this, AddEventActivity::class.java)
            startActivityForResult(intent, REQUEST_ADD_EVENT)
        }

        logout = findViewById(R.id.logoutButton)
        logout.setOnClickListener {
            sessionManager.logout()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchEvents() {
        eventDao.getAllEvents().observe(this, Observer { fetchedEvents ->
            events.clear()
            events.addAll(fetchedEvents)
            eventAdapter.notifyDataSetChanged()

            if (events.isEmpty()) {
                findViewById<TextView>(R.id.noEvents).visibility = android.view.View.VISIBLE
                recyclerView.visibility = android.view.View.GONE
            } else {
                findViewById<TextView>(R.id.noEvents).visibility = android.view.View.GONE
                recyclerView.visibility = android.view.View.VISIBLE
            }
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
