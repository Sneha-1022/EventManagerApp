package com.example.ema.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ema.R
import com.example.ema.data.AppDatabase
import com.example.ema.data.dao.UserDao
import com.example.ema.data.entities.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val db = AppDatabase.getDatabase(this)
        userDao = db.userDao()

        findViewById<Button>(R.id.registerButton).setOnClickListener {
            val username = findViewById<EditText>(R.id.username).text.toString()
            val password = findViewById<EditText>(R.id.password).text.toString()

            if (username.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Username and Password cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            registerUser(username, password)
        }
    }

    private fun registerUser(username: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val existingUser = userDao.getUserByUsername(username)
            if (existingUser == null) {
                val newUser = User(username = username, password = password)
                userDao.insertUser(newUser)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@RegisterActivity, "com.example.ema.data.entities.Registration Successful", Toast.LENGTH_SHORT).show()
                    finish() // Redirect to LoginActivity
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@RegisterActivity, "Username already exists", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
