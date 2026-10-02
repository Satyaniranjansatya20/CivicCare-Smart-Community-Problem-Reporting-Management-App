package com.example.civiccare

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvRegister: TextView
    private lateinit var tvForgotPassword: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        // Connect Kotlin variables with XML
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvRegister = findViewById(R.id.tvRegister)
        tvForgotPassword = findViewById(R.id.tvForgotPassword)

        // Login Button
        btnLogin.setOnClickListener {

            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (validateInput(email, password)) {

                Toast.makeText(
                    this,
                    "Login successful",
                    Toast.LENGTH_SHORT
                ).show()

                // Dashboard will be connected later.
            }
        }

        // Register
        tvRegister.setOnClickListener {

            Toast.makeText(
                this,
                "Registration screen will open here",
                Toast.LENGTH_SHORT
            ).show()

            /*
            Later:

            val intent = Intent(
                this,
                RegisterActivity::class.java
            )

            startActivity(intent)
            */
        }

        // Forgot Password
        tvForgotPassword.setOnClickListener {

            Toast.makeText(
                this,
                "Forgot Password will be added later",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private fun validateInput(
        email: String,
        password: String
    ): Boolean {

        // Empty Email
        if (email.isEmpty()) {

            etEmail.error = "Please enter your email"
            etEmail.requestFocus()

            return false
        }

        // Invalid Email
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            etEmail.error = "Please enter a valid email"
            etEmail.requestFocus()

            return false
        }

        // Empty Password
        if (password.isEmpty()) {

            etPassword.error = "Please enter your password"
            etPassword.requestFocus()

            return false
        }

        // Short Password
        if (password.length < 6) {

            etPassword.error =
                "Password must contain at least 6 characters"

            etPassword.requestFocus()

            return false
        }

        return true
    }
}