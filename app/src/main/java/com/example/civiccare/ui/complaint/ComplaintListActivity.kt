package com.example.civiccare.ui.complaint

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.civiccare.R
import com.example.civiccare.viewmodel.ComplaintViewModel
import kotlinx.coroutines.launch

class ComplaintListActivity : AppCompatActivity() {

    private val viewModel: ComplaintViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complaint_list)

        findViewById<TextView>(R.id.tvBack).setOnClickListener { finish() }

        findViewById<Button>(R.id.btnNewComplaint).setOnClickListener {
            startActivity(Intent(this, ReportComplaintActivity::class.java))
        }

        val tvEmpty = findViewById<TextView>(R.id.tvEmpty)
        val rv = findViewById<RecyclerView>(R.id.rvComplaints)
        val adapter = ComplaintAdapter { complaint ->
            val intent = Intent(this, ComplaintDetailsActivity::class.java)
                .putExtra("id", complaint.complaintId)
                .putExtra("title", complaint.title)
                .putExtra("category", complaint.category)
                .putExtra("priority", complaint.priority)
                .putExtra("location", complaint.location)
                .putExtra("date", complaint.date)
                .putExtra("description", complaint.description)
                .putExtra("status", complaint.status)
                .putExtra("imageUri", complaint.imageUri)
            startActivity(intent)
        }
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        lifecycleScope.launch {
            viewModel.complaints.collect { list ->
                adapter.submit(list)
                tvEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }
}