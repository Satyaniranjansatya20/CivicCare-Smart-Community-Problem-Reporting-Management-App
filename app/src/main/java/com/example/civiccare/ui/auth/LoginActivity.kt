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
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.lifecycleScope
import com.example.civiccare.R
import com.example.civiccare.ui.dashboard.DashboardActivity
import com.example.civiccare.utils.SessionManager
import com.example.civiccare.viewmodel.LoginResult
import com.example.civiccare.viewmodel.LoginViewModel
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnGoogle: Button
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
        btnGoogle = findViewById(R.id.btnGoogle)
        tvRegister = findViewById(R.id.tvRegister)
        tvForgotPassword = findViewById(R.id.tvForgotPassword)

        // WATCH LOGIN RESULT (used by normal login and Google sign-in)
        viewModel.loginResult.observe(this) { result ->
            when (result) {
                is LoginResult.Success -> {
                    SessionManager(this).saveSession(
                        result.user.id,
                        result.user.role,
                        result.user.name
                    )
                    Toast.makeText(this, "Welcome, ${result.user.name}", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, DashboardActivity::class.java))
                    finish()
                }
                is LoginResult.Failure -> {
                    btnLogin.isEnabled = true
                    btnGoogle.isEnabled = true
                    Toast.makeText(this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show()
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

        // CONTINUE WITH GOOGLE BUTTON
        btnGoogle.setOnClickListener {
            startGoogleSignIn()
        }

        // REGISTER TEXT
        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // FORGOT PASSWORD
        tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }

    private fun startGoogleSignIn() {
        btnGoogle.isEnabled = false

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(getString(R.string.default_web_client_id))
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val credentialManager = CredentialManager.create(this)

        lifecycleScope.launch {
            try {
                val response = credentialManager.getCredential(this@LoginActivity, request)
                val credential = response.credential

                if (credential is CustomCredential &&
                    credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                ) {
                    val googleCredential = GoogleIdTokenCredential.createFrom(credential.data)
                    viewModel.googleSignIn(
                        googleCredential.displayName ?: "",
                        googleCredential.id
                    )
                } else {
                    btnGoogle.isEnabled = true
                    Toast.makeText(
                        this@LoginActivity,
                        "Unexpected Google sign-in response",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                btnGoogle.isEnabled = true
                Toast.makeText(
                    this@LoginActivity,
                    "Google sign-in failed: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
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