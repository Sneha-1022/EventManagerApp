package com.example.ema.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ema.R
import com.example.ema.data.AppDatabase
import com.example.ema.data.dao.EventDao
import com.example.ema.data.entities.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class AddEventActivity : AppCompatActivity() {

    private lateinit var eventDao: EventDao
    private lateinit var eventNameEditText: EditText
    private lateinit var eventDescriptionEditText: EditText
    private lateinit var eventDateEditText: EditText
    private lateinit var eventLocationEditText: EditText
    private lateinit var eventSeatsEditText: EditText
    private lateinit var addEventButton: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)

        val db = AppDatabase.getDatabase(this)
        eventDao = db.eventDao()

        eventNameEditText = findViewById(R.id.eventNameEditText)
        eventDescriptionEditText = findViewById(R.id.eventDescriptionEditText)
        eventDateEditText = findViewById(R.id.eventDateEditText)
        eventLocationEditText = findViewById(R.id.eventLocationEditText)
        eventSeatsEditText = findViewById(R.id.eventSeatsEditText)
        addEventButton = findViewById(R.id.addEventButton)

        addEventButton.setOnClickListener {
            addEvent()
        }

        eventDateEditText.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun addEvent() {
        val eventName = eventNameEditText.text.toString().trim()
        val eventDescription = eventDescriptionEditText.text.toString().trim()
        val eventDate = eventDateEditText.text.toString().trim()
        val eventLocation = eventLocationEditText.text.toString().trim()
        val eventSeats = eventSeatsEditText.text.toString().trim()

        if (eventName.isEmpty() || eventDescription.isEmpty() || eventDate.isEmpty() || eventLocation.isEmpty() || eventSeats.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val totalSeats = eventSeats.toIntOrNull()
        if (totalSeats == null || totalSeats <= 0) {
            Toast.makeText(this, "Please enter a valid number for seats", Toast.LENGTH_SHORT).show()
            return
        }

        val newEvent = Event(
            id = 0,
            name = eventName,
            description = eventDescription,
            date = eventDate,
            location = eventLocation,
            totalSeats = totalSeats,
            availableSeats = totalSeats
        )


        CoroutineScope(Dispatchers.IO).launch {
            eventDao.insertEvent(newEvent)
            withContext(Dispatchers.Main) {
                Toast.makeText(this@AddEventActivity, "Event added successfully", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK)
                finish()
            }
        }
    }

    private fun showDatePickerDialog() {

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
            eventDateEditText.setText(selectedDate)
        }, year, month, day)

        datePickerDialog.show()
    }
}
