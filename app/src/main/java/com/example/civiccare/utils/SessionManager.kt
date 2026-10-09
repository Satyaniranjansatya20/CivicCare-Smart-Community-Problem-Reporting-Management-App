package com.example.civiccare.utils

import android.content.Context

class SessionManager(context: Context) {

    private val prefs = context.getSharedPreferences("civiccare_session", Context.MODE_PRIVATE)

    // Call this after a successful login
    fun saveSession(userId: Int, role: String, name: String) {
        prefs.edit()
            .putInt("user_id", userId)
            .putString("role", role)
            .putString("name", name)
            .apply()
    }

    // Returns -1 if nobody is logged in
    fun getUserId(): Int = prefs.getInt("user_id", -1)

    fun getRole(): String = prefs.getString("role", "USER") ?: "USER"

    fun getName(): String = prefs.getString("name", "Citizen") ?: "Citizen"

    fun updateName(name: String) {
        prefs.edit().putString("name", name).apply()
    }

    fun isLoggedIn(): Boolean = getUserId() != -1

    // Call this on logout
    fun clear() {
        prefs.edit().clear().apply()
    }
}