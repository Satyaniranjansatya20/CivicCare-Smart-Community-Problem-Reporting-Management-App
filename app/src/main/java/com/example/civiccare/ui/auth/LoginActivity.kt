package com.example.civiccare.ui.auth

import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.civiccare.R

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvRegister: TextView
    private lateinit var tvForgotPassword: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        // XML views connect
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvRegister = findViewById(R.id.tvRegister)
        tvForgotPassword = findViewById(R.id.tvForgotPassword)

        // LOGIN BUTTON
        btnLogin.setOnClickListener {

            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (validateInput(email, password)) {

                Toast.makeText(
                    this,
                    "Login Successful",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // REGISTER TEXT
        tvRegister.setOnClickListener {

            Toast.makeText(
                this,
                "Register clicked",
                Toast.LENGTH_SHORT
            ).show()
        }

        // FORGOT PASSWORD
        tvForgotPassword.setOnClickListener {

            Toast.makeText(
                this,
                "Forgot Password clicked",
                Toast.LENGTH_SHORT
            ).show()
        }
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