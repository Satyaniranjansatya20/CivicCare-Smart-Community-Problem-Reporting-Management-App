package com.example.civiccare.ui.auth

import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.civiccare.R
import com.example.civiccare.viewmodel.LoginViewModel

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirm: EditText
    private lateinit var btnReset: Button
    private lateinit var btnBack: ImageView

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_forgot_password)

        etEmail = findViewById(R.id.etResetEmail)
        etPassword = findViewById(R.id.etResetPassword)
        etConfirm = findViewById(R.id.etResetConfirmPassword)
        btnReset = findViewById(R.id.btnResetPassword)
        btnBack = findViewById(R.id.btnBack)

        btnBack.setOnClickListener { finish() }

        // WATCH RESET RESULT
        viewModel.resetResult.observe(this) { success ->
            if (success) {
                Toast.makeText(
                    this,
                    "Password changed. Please login.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            } else {
                btnReset.isEnabled = true
                etEmail.error = "No account found with this email"
                etEmail.requestFocus()
            }
        }

        // RESET BUTTON
        btnReset.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirm = etConfirm.text.toString().trim()

            if (validateInput(email, password, confirm)) {
                btnReset.isEnabled = false
                viewModel.resetPassword(email, password)
            }
        }
    }

    private fun validateInput(email: String, password: String, confirm: String): Boolean {

        if (email.isEmpty()) {
            etEmail.error = "Enter your email"
            etEmail.requestFocus()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = "Enter valid email"
            etEmail.requestFocus()
            return false
        }

        if (password.length < 6) {
            etPassword.error = "Password must contain at least 6 characters"
            etPassword.requestFocus()
            return false
        }

        if (confirm != password) {
            etConfirm.error = "Passwords do not match"
            etConfirm.requestFocus()
            return false
        }

        return true
    }
}