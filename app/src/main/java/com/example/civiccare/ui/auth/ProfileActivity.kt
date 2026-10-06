package com.example.civiccare.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.civiccare.R
import com.example.civiccare.data.model.User
import com.example.civiccare.viewmodel.LoginViewModel

class ProfileActivity : AppCompatActivity() {

    private lateinit var tvRole: TextView
    private lateinit var tvHeaderName: TextView
    private lateinit var tvHeaderEmail: TextView
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhone: EditText
    private lateinit var btnSave: Button
    private lateinit var btnLogout: Button
    private lateinit var btnBack: ImageView

    private val viewModel: LoginViewModel by viewModels()
    private var loadedUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_profile)

        tvRole = findViewById(R.id.tvProfileRole)
        tvHeaderName = findViewById(R.id.tvProfileName)
        tvHeaderEmail = findViewById(R.id.tvProfileEmail)
        etName = findViewById(R.id.etProfileName)
        etEmail = findViewById(R.id.etProfileEmail)
        etPhone = findViewById(R.id.etProfilePhone)
        btnSave = findViewById(R.id.btnSaveProfile)
        btnLogout = findViewById(R.id.btnLogout)
        btnBack = findViewById(R.id.btnBack)

        btnBack.setOnClickListener { finish() }

        val prefs = getSharedPreferences("civiccare_session", MODE_PRIVATE)
        val userId = prefs.getInt("user_id", -1)

        // Nobody is logged in, so go back to Login
        if (userId == -1) {
            goToLogin()
            return
        }

        // SHOW USER DETAILS
        viewModel.currentUser.observe(this) { user ->
            if (user != null) {
                loadedUser = user
                etName.setText(user.name)
                etEmail.setText(user.email)
                etPhone.setText(user.phone)
                tvHeaderName.text = user.name
                tvHeaderEmail.text = user.email
                tvRole.text = user.role
            }
        }

        // AFTER SAVING
        viewModel.updateResult.observe(this) { success ->
            if (success) {
                val newName = etName.text.toString().trim()
                prefs.edit().putString("name", newName).apply()
                tvHeaderName.text = newName
                Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
            }
        }

        // SAVE BUTTON
        btnSave.setOnClickListener {
            val nameText = etName.text.toString().trim()
            val phoneText = etPhone.text.toString().trim()

            if (nameText.isEmpty()) {
                etName.error = "Enter your name"
                etName.requestFocus()
                return@setOnClickListener
            }

            if (phoneText.length != 10) {
                etPhone.error = "Enter a 10 digit phone number"
                etPhone.requestFocus()
                return@setOnClickListener
            }

            loadedUser?.let { user ->
                viewModel.updateProfile(user.copy(name = nameText, phone = phoneText))
            }
        }

        // LOGOUT BUTTON
        btnLogout.setOnClickListener {
            prefs.edit().clear().apply()
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
            goToLogin()
        }

        viewModel.loadUser(userId)
    }

    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}