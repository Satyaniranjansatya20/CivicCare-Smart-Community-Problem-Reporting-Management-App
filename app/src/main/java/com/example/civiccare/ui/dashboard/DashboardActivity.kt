package com.example.civiccare.ui.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.civiccare.R
import com.example.civiccare.ui.complaint.ReportComplaintActivity

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_dashboard)

        findViewById<android.widget.Button>(R.id.btnReportProblem).setOnClickListener {
            startActivity(Intent(this, ReportComplaintActivity::class.java))
        }
    }
}