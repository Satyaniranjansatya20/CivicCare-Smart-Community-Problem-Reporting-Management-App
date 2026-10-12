package com.example.civiccare.ui.complaint

import android.content.Intent
import android.graphics.Color
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
import com.example.civiccare.data.model.Complaint
import com.example.civiccare.viewmodel.ComplaintViewModel
import kotlinx.coroutines.launch

class ComplaintListActivity : AppCompatActivity() {

    private val viewModel: ComplaintViewModel by viewModels()

    private var allComplaints: List<Complaint> = emptyList()
    private var currentFilter = "All"

    private lateinit var adapter: ComplaintAdapter
    private lateinit var tvEmpty: TextView
    private lateinit var chips: List<Pair<TextView, String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complaint_list)

        findViewById<TextView>(R.id.tvBack).setOnClickListener { finish() }

        findViewById<Button>(R.id.btnNewComplaint).setOnClickListener {
            startActivity(Intent(this, ReportComplaintActivity::class.java))
        }

        tvEmpty = findViewById(R.id.tvEmpty)
        val rv = findViewById<RecyclerView>(R.id.rvComplaints)

        adapter = ComplaintAdapter { complaint ->
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

        chips = listOf(
            findViewById<TextView>(R.id.tvFilterAll) to "All",
            findViewById<TextView>(R.id.tvFilterPending) to "Pending",
            findViewById<TextView>(R.id.tvFilterProgress) to "In Progress",
            findViewById<TextView>(R.id.tvFilterResolved) to "Resolved"
        )
        chips.forEach { (view, name) ->
            view.setOnClickListener {
                currentFilter = name
                applyFilter()
            }
        }

        lifecycleScope.launch {
            viewModel.complaints.collect { list ->
                allComplaints = list
                applyFilter()
            }
        }
    }

    private fun applyFilter() {
        val shown = if (currentFilter == "All") allComplaints
        else allComplaints.filter { it.status == currentFilter }

        adapter.submit(shown)
        tvEmpty.visibility = if (shown.isEmpty()) View.VISIBLE else View.GONE

        chips.forEach { (view, name) ->
            val selected = name == currentFilter
            view.setBackgroundResource(if (selected) R.drawable.bg_chip_selected else R.drawable.bg_card)
            view.setTextColor(if (selected) Color.WHITE else Color.parseColor("#212121"))
        }
    }
}