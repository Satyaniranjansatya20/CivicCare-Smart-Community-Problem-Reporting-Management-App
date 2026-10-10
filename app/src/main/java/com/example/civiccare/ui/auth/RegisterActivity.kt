package com.example.civiccare.ui.auth

import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.civiccare.R
import com.example.civiccare.viewmodel.LoginViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var name: EditText
    private lateinit var email: EditText
    private lateinit var phone: EditText
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var registerButton: Button
    private lateinit var btnBack: ImageView
    private lateinit var tvLogin: TextView

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)

        name = findViewById(R.id.etName)
        email = findViewById(R.id.etRegisterEmail)
        phone = findViewById(R.id.etRegisterPhone)
        password = findViewById(R.id.etRegisterPassword)
        confirmPassword = findViewById(R.id.etRegisterConfirmPassword)
        registerButton = findViewById(R.id.btnRegisterAccount)
        btnBack = findViewById(R.id.btnBack)
        tvLogin = findViewById(R.id.tvLogin)

        // BACK ARROW AND LOGIN LINK
        btnBack.setOnClickListener { finish() }
        tvLogin.setOnClickListener { finish() }

        // WATCH REGISTER RESULT
        viewModel.registerResult.observe(this) { success ->
            if (success) {
                Toast.makeText(
                    this,
                    "Registration successful. Please login.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            } else {
                registerButton.isEnabled = true
                email.error = "This email is already registered"
                email.requestFocus()
            }
        }

        // REGISTER BUTTON
        registerButton.setOnClickListener {

            val nameText = name.text.toString().trim()
            val emailText = email.text.toString().trim()
            val phoneText = phone.text.toString().trim()
            val passwordText = password.text.toString().trim()
            val confirmText = confirmPassword.text.toString().trim()

            if (validateInput(nameText, emailText, phoneText, passwordText, confirmText)) {
                registerButton.isEnabled = false
                viewModel.register(nameText, emailText, phoneText, passwordText)
            }
        }
    }

    private fun validateInput(
        nameText: String,
        emailText: String,
        phoneText: String,
        passwordText: String,
        confirmText: String
    ): Boolean {

        if (nameText.isEmpty()) {
            name.error = "Enter your name"
            name.requestFocus()
            return false
        }

        if (emailText.isEmpty()) {
            email.error = "Enter email"
            email.requestFocus()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            email.error = "Enter valid email"
            email.requestFocus()
            return false
        }

        if (phoneText.length != 10) {
            phone.error = "Enter a 10 digit phone number"
            phone.requestFocus()
            return false
        }

        if (passwordText.length < 6) {
            password.error = "Password must contain at least 6 characters"
            password.requestFocus()
            return false
        }

        if (confirmText != passwordText) {
            confirmPassword.error = "Passwords do not match"
            confirmPassword.requestFocus()
            return false
        }

        return true
    }
}