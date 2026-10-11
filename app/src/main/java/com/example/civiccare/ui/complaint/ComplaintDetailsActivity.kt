package com.example.civiccare.ui.complaint

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
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
        val status = intent.getStringExtra("status") ?: "Pending"
        val imagePath = intent.getStringExtra("imageUri") ?: ""

        findViewById<TextView>(R.id.tvTitle).text = intent.getStringExtra("title")
        findViewById<TextView>(R.id.tvId).text = "ID: CC${1000 + id}"
        findViewById<TextView>(R.id.tvMeta).text =
            "${intent.getStringExtra("category")} • ${intent.getStringExtra("priority")} priority"
        findViewById<TextView>(R.id.tvLocation).text = "📍 ${intent.getStringExtra("location")}"
        findViewById<TextView>(R.id.tvDate).text = intent.getStringExtra("date")
        findViewById<TextView>(R.id.tvDescription).text = intent.getStringExtra("description")

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
    }
}