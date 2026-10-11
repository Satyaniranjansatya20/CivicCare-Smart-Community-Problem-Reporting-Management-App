package com.example.civiccare.ui.complaint

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.civiccare.R
import com.example.civiccare.data.model.Complaint
import com.example.civiccare.viewmodel.ComplaintViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReportComplaintActivity : AppCompatActivity() {

    private val viewModel: ComplaintViewModel by viewModels()

    private lateinit var etTitle: EditText
    private lateinit var etDescription: EditText
    private lateinit var etLocation: EditText
    private lateinit var tvCategory: TextView
    private lateinit var tvLow: TextView
    private lateinit var tvMedium: TextView
    private lateinit var tvHigh: TextView
    private lateinit var tvAddPhoto: TextView
    private lateinit var imgPreview: ImageView

    private var selectedCategory = ""
    private var selectedPriority = "Medium"
    private var selectedImagePath = ""

    private val categoryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                selectedCategory = result.data?.getStringExtra("category") ?: ""
                tvCategory.text = selectedCategory
                tvCategory.setTextColor(Color.parseColor("#212121"))
            }
        }

    private val photoPicker =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            if (uri != null) savePhoto(uri)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_report_complaint)

            etTitle = findViewById(R.id.etTitle)
            etDescription = findViewById(R.id.etDescription)
            etLocation = findViewById(R.id.etLocation)
            tvCategory = findViewById(R.id.tvCategory)
            tvLow = findViewById(R.id.tvLow)
            tvMedium = findViewById(R.id.tvMedium)
            tvHigh = findViewById(R.id.tvHigh)
            tvAddPhoto = findViewById(R.id.tvAddPhoto)
            imgPreview = findViewById(R.id.imgPreview)

            findViewById<TextView>(R.id.tvBack).setOnClickListener { finish() }

            tvCategory.setOnClickListener {
                categoryLauncher.launch(Intent(this, CategoryActivity::class.java))
            }

            tvAddPhoto.setOnClickListener {
                photoPicker.launch(arrayOf("image/*"))
            }

            tvLow.setOnClickListener { setPriority("Low") }
            tvMedium.setOnClickListener { setPriority("Medium") }
            tvHigh.setOnClickListener { setPriority("High") }

            findViewById<Button>(R.id.btnSubmit).setOnClickListener { validateAndSubmit() }

            setPriority("Medium")
        } catch (e: Throwable) {
            AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(e.stackTraceToString().take(900))
                .setPositiveButton("OK") { _, _ -> finish() }
                .show()
        }
    }

    private fun savePhoto(uri: Uri) {
        lifecycleScope.launch {
            val path = withContext(Dispatchers.IO) {
                contentResolver.openInputStream(uri)?.use { input ->
                    val file = File(filesDir, "complaint_${System.currentTimeMillis()}.jpg")
                    file.outputStream().use { input.copyTo(it) }
                    file.absolutePath
                } ?: ""
            }
            if (path.isNotEmpty()) {
                selectedImagePath = path
                imgPreview.setImageURI(Uri.fromFile(File(path)))
                imgPreview.visibility = View.VISIBLE
                tvAddPhoto.text = "📷  Change Photo"
            } else {
                Toast.makeText(this@ReportComplaintActivity, "Could not load photo", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateAndSubmit() {
        val title = etTitle.text.toString().trim()
        val description = etDescription.text.toString().trim()
        val location = etLocation.text.toString().trim()

        if (title.isEmpty()) {
            etTitle.error = "Enter title"
            return
        }
        if (selectedCategory.isEmpty()) {
            Toast.makeText(this, "Select Category", Toast.LENGTH_SHORT).show()
            return
        }
        if (description.length < 10) {
            etDescription.error = "Description must be at least 10 characters"
            return
        }
        if (location.isEmpty()) {
            etLocation.error = "Enter location"
            return
        }

        lifecycleScope.launch {
            val similar = viewModel.similarCount(selectedCategory, location)
            if (similar > 0) {
                AlertDialog.Builder(this@ReportComplaintActivity)
                    .setTitle("Similar complaint found")
                    .setMessage("A similar complaint already exists at this location. Submit anyway?")
                    .setPositiveButton("Submit anyway") { _, _ -> save(title, description, location) }
                    .setNegativeButton("Cancel", null)
                    .show()
            } else {
                save(title, description, location)
            }
        }
    }

    private fun save(title: String, description: String, location: String) {
        val date = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date())
        val complaint = Complaint(
            title = title,
            category = selectedCategory,
            description = description,
            location = location,
            priority = selectedPriority,
            imageUri = selectedImagePath,
            date = date
        )
        viewModel.submit(complaint) { id ->
            AlertDialog.Builder(this)
                .setTitle("Complaint submitted ✅")
                .setMessage("Your complaint ID is CC${1000 + id}\nStatus: Pending")
                .setPositiveButton("OK", null)
                .show()
            clearForm()
        }
    }

    private fun clearForm() {
        etTitle.text.clear()
        etDescription.text.clear()
        etLocation.text.clear()
        selectedCategory = ""
        tvCategory.text = "Select Category  ›"
        tvCategory.setTextColor(Color.parseColor("#757575"))
        selectedImagePath = ""
        imgPreview.visibility = View.GONE
        tvAddPhoto.text = "📷  Add Photo (optional)"
        setPriority("Medium")
    }

    private fun setPriority(priority: String) {
        selectedPriority = priority
        listOf(tvLow to "Low", tvMedium to "Medium", tvHigh to "High").forEach { (view, name) ->
            if (name == priority) {
                view.setBackgroundResource(R.drawable.bg_chip_selected)
                view.setTextColor(Color.WHITE)
            } else {
                view.setBackgroundResource(R.drawable.bg_card)
                view.setTextColor(Color.parseColor("#212121"))
            }
        }
    }
}