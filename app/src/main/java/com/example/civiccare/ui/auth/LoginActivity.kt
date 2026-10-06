package com.example.civiccare.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.civiccare.R
import com.example.civiccare.ui.dashboard.DashboardActivity
import com.example.civiccare.viewmodel.LoginResult
import com.example.civiccare.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvRegister: TextView
    private lateinit var tvForgotPassword: TextView

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        // XML views connect
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvRegister = findViewById(R.id.tvRegister)
        tvForgotPassword = findViewById(R.id.tvForgotPassword)

        // WATCH LOGIN RESULT
        viewModel.loginResult.observe(this) { result ->
            when (result) {
                is LoginResult.Success -> {
                    saveSession(result.user.id, result.user.role, result.user.name)
                    Toast.makeText(this, "Welcome, ${result.user.name}", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, DashboardActivity::class.java))
                    finish()
                }
                is LoginResult.Failure -> {
                    btnLogin.isEnabled = true
                    Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // LOGIN BUTTON
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (validateInput(email, password)) {
                btnLogin.isEnabled = false
                viewModel.login(email, password)
            }
        }

        // REGISTER TEXT
        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // FORGOT PASSWORD
        tvForgotPassword.setOnClickListener {
            Toast.makeText(this, "Forgot Password clicked", Toast.LENGTH_SHORT).show()
        }
    }

    // Remember who is logged in (used by Dashboard, Profile and Logout)
    private fun saveSession(userId: Int, role: String, name: String) {
        getSharedPreferences("civiccare_session", MODE_PRIVATE)
            .edit()
            .putInt("user_id", userId)
            .putString("role", role)
            .putString("name", name)
            .apply()
    }

    private fun validateInput(
        email: String,
        password: String
    ): Boolean {

        if (email.isEmpty()) {
            etEmail.error = "Please enter email"
            etEmail.requestFocus()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = "Enter valid email"
            etEmail.requestFocus()
            return false
        }

        if (password.isEmpty()) {
            etPassword.error = "Please enter password"
            etPassword.requestFocus()
            return false
        }

        if (password.length < 6) {
            etPassword.error = "Minimum 6 characters required"
            etPassword.requestFocus()
            return false
        }

        return true
    }
}