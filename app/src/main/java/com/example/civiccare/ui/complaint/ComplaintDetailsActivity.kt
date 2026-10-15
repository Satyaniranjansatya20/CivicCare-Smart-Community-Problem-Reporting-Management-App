package com.example.civiccare.ui.complaint

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.civiccare.R
import java.io.File

class ComplaintDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complaint_details)

        findViewById<TextView>(R.id.tvBack).setOnClickListener { finish() }

        val id = intent.getIntExtra("id", 0)
        val title = intent.getStringExtra("title") ?: ""
        val category = intent.getStringExtra("category") ?: ""
        val priority = intent.getStringExtra("priority") ?: ""
        val location = intent.getStringExtra("location") ?: ""
        val date = intent.getStringExtra("date") ?: ""
        val description = intent.getStringExtra("description") ?: ""
        val status = intent.getStringExtra("status") ?: "Pending"
        val imagePath = intent.getStringExtra("imageUri") ?: ""
        val code = "CC${1000 + id}"

        findViewById<TextView>(R.id.tvTitle).text = title
        findViewById<TextView>(R.id.tvId).text = "ID: $code"
        findViewById<TextView>(R.id.tvMeta).text = "$category • $priority priority"
        findViewById<TextView>(R.id.tvLocation).text = "📍 $location"
        findViewById<TextView>(R.id.tvDate).text = date
        findViewById<TextView>(R.id.tvDescription).text = description

        val tvStatus = findViewById<TextView>(R.id.tvStatus)
        tvStatus.text = status
        tvStatus.setTextColor(
            when (status) {
                "Resolved" -> Color.parseColor("#2E7D32")
                "In Progress" -> Color.parseColor("#1565C0")
                else -> Color.parseColor("#EF6C00")
            }
        )

        if (imagePath.isNotEmpty() && File(imagePath).exists()) {
            val img = findViewById<ImageView>(R.id.imgPhoto)
            img.setImageURI(Uri.fromFile(File(imagePath)))
            img.visibility = View.VISIBLE
        }

        findViewById<Button>(R.id.btnShare).setOnClickListener {
            val message = "CivicCare complaint $code\n" +
                    "$title\n" +
                    "Category: $category ($priority priority)\n" +
                    "Location: $location\n" +
                    "Status: $status\n" +
                    "Reported on: $date"
            val share = Intent(Intent.ACTION_SEND)
                .setType("text/plain")
                .putExtra(Intent.EXTRA_TEXT, message)
            startActivity(Intent.createChooser(share, "Share complaint"))
        }
    }
}