package com.example.ema.ui

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ema.R
import com.example.ema.data.AppDatabase
import com.example.ema.data.dao.UserDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.log

class LoginActivity : AppCompatActivity() {
    private lateinit var userDao: UserDao
    lateinit var register : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val db = AppDatabase.getDatabase(this)
        userDao = db.userDao()

        findViewById<Button>(R.id.loginButton).setOnClickListener {
            val username = findViewById<EditText>(R.id.username).text.toString()
            val password = findViewById<EditText>(R.id.password).text.toString()

            CoroutineScope(Dispatchers.IO).launch {
                val user = userDao.authenticate(username, password)
                withContext(Dispatchers.Main) {
                    Log.d(TAG, "onCreate: $username $password")
                    if (username == "admin" || user != null) {
                        Log.d(TAG, "onCreate: $username $password")

                        if (username == "admin" && password == "admin") {
                            startActivity(Intent(this@LoginActivity, AdminActivity::class.java))
                        } else {
                            val intent = Intent(this@LoginActivity, UserActivity::class.java)
                            if (user != null) {
                                intent.putExtra("USER_ID", user.id)
                            }
                            startActivity(intent)
                        }
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Invalid credentials", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        register = findViewById(R.id.registerLink)
        register.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
