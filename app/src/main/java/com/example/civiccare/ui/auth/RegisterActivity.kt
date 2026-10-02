package com.example.civiccare.ui.auth

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.civiccare.R

class RegisterActivity : AppCompatActivity() {

    private lateinit var name: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)

        name = findViewById(R.id.etName)
        email = findViewById(R.id.etRegisterEmail)
        password = findViewById(R.id.etRegisterPassword)

        registerButton = findViewById(R.id.btnRegisterAccount)

        registerButton.setOnClickListener {

            val nameText = name.text.toString().trim()
            val emailText = email.text.toString().trim()
            val passwordText = password.text.toString().trim()

            if (nameText.isEmpty()) {

                name.error = "Enter your name"
                return@setOnClickListener
            }

            if (emailText.isEmpty()) {

                email.error = "Enter email"
                return@setOnClickListener
            }

            if (passwordText.length < 6) {

                password.error = "Password must contain at least 6 characters"
                return@setOnClickListener
            }

            Toast.makeText(
                this,
                "Registration successful",
                Toast.LENGTH_SHORT
            ).show()

            finish()
        }
    }
}
