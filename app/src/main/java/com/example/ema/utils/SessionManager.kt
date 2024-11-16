package com.example.ema.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    companion object {
        private const val PREF_NAME = "UserSession"
        private const val IS_LOGGED_IN = "isLoggedIn"
        private const val USER_ID = "userId"
        private const val USERNAME = "username"
    }

    fun createLoginSession(userId: String) {
        editor.putBoolean(IS_LOGGED_IN, true)
        editor.putString(USER_ID, userId)
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false)
    }

    fun getUserId(): String? {
        return sharedPreferences.getString(USER_ID, null)
    }

    fun logout() {
        editor.clear()
        editor.apply()
    }

    fun saveUsername(username: String) {
        editor.putString(USERNAME, username)
        editor.apply()
    }

    fun getUsername(): String? {
        return sharedPreferences.getString(USERNAME, null)
    }
}
