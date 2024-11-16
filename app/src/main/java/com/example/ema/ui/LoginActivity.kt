package com.example.ema.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ema.R
import com.example.ema.data.AppDatabase
import com.example.ema.data.dao.UserDao
import com.example.ema.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private lateinit var userDao: UserDao
    private lateinit var register: TextView
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sessionManager = SessionManager(this)

        if (sessionManager.isLoggedIn()) {
            navigateToNextScreen(sessionManager.getUserId())
        }

        val db = AppDatabase.getDatabase(this)
        userDao = db.userDao()

        findViewById<Button>(R.id.loginButton).setOnClickListener {
            val username = findViewById<EditText>(R.id.username).text.toString()
            val password = findViewById<EditText>(R.id.password).text.toString()

            CoroutineScope(Dispatchers.IO).launch {
                val user = userDao.authenticate(username, password)
                withContext(Dispatchers.Main) {
                    if (username == "admin" || user != null) {


                        if (username == "admin" && password == "admin") {
                            sessionManager.createLoginSession("admin")
                            startActivity(Intent(this@LoginActivity, AdminActivity::class.java))
                        } else {
                            user?.let {
                                sessionManager.createLoginSession(it.id.toString())
                                sessionManager.saveUsername(username.toString())
                                val intent = Intent(this@LoginActivity, UserActivity::class.java)
                                intent.putExtra("USER_ID", it.id)
                                startActivity(intent)
                            }
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

    private fun navigateToNextScreen(userId: String?) {
        if (userId == "admin") {
            startActivity(Intent(this, AdminActivity::class.java))
        } else {
            val intent = Intent(this, UserActivity::class.java)
            userId?.let { intent.putExtra("USER_ID", it) }
            startActivity(intent)
        }
        finish()
    }
}
