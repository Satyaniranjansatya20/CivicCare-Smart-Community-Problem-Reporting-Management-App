package com.example.civiccare.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.civiccare.R
import com.example.civiccare.ui.auth.LoginActivity
import com.example.civiccare.ui.dashboard.DashboardActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({

            // Already logged in? Skip Login and open the Dashboard
            val prefs = getSharedPreferences("civiccare_session", MODE_PRIVATE)
            val isLoggedIn = prefs.getInt("user_id", -1) != -1

            val nextScreen = if (isLoggedIn) {
                DashboardActivity::class.java
            } else {
                LoginActivity::class.java
            }

            startActivity(Intent(this, nextScreen))

            finish()

        }, 2000)
    }
}