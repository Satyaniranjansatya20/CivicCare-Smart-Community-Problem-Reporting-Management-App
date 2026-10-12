package com.example.civiccare.ui.complaint

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.civiccare.R
import com.example.civiccare.data.database.CivicCareDatabase
import com.example.civiccare.data.model.Category
import kotlinx.coroutines.launch

class CategoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        findViewById<TextView>(R.id.tvBack).setOnClickListener { finish() }

        val adapter = CategoryAdapter { category ->
            if (category.categoryName == "Other") askOther() else sendBack(category.categoryName)
        }

        val rv = findViewById<RecyclerView>(R.id.rvCategories)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        val dao = CivicCareDatabase.getDatabase(this).categoryDao()
        lifecycleScope.launch {
            dao.getAllCategories().collect { list ->
                adapter.submit(list.filter { it.categoryName != "Other" } + Category(categoryName = "Other"))
            }
        }
    }

    private fun askOther() {
        val input = EditText(this)
        input.hint = "Describe the problem type"
        input.setPadding(48, 32, 48, 32)

        AlertDialog.Builder(this)
            .setTitle("Other problem")
            .setView(input)
            .setPositiveButton("OK") { _, _ ->
                val text = input.text.toString().trim()
                if (text.isEmpty()) {
                    Toast.makeText(this, "Enter a problem type", Toast.LENGTH_SHORT).show()
                } else {
                    sendBack("Other: $text")
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun sendBack(name: String) {
        setResult(RESULT_OK, Intent().putExtra("category", name))
        finish()
    }
}