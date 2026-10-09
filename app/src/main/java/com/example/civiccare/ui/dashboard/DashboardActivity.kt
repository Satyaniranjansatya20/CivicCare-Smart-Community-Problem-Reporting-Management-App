package com.example.civiccare.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.civiccare.R
import com.example.civiccare.ui.auth.ProfileActivity
import com.example.civiccare.utils.SessionManager

class DashboardActivity : AppCompatActivity() {

    private lateinit var tvGreeting: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_dashboard)

        tvGreeting = findViewById(R.id.tvGreeting)

        val btnProfile = findViewById<Button>(R.id.btnProfile)
        btnProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    // Runs every time the Dashboard appears, so the name updates after editing Profile
    override fun onResume() {
        super.onResume()
        tvGreeting.text = "Hello, ${SessionManager(this).getName()} 👋"
    }
}