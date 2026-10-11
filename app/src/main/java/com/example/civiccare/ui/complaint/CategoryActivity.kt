package com.example.civiccare.ui.complaint

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.civiccare.R
import com.example.civiccare.data.database.CivicCareDatabase
import kotlinx.coroutines.launch

class CategoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        findViewById<TextView>(R.id.tvBack).setOnClickListener { finish() }

        val adapter = CategoryAdapter { category ->
            val result = Intent().putExtra("category", category.categoryName)
            setResult(RESULT_OK, result)
            finish()
        }

        val rv = findViewById<RecyclerView>(R.id.rvCategories)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        val dao = CivicCareDatabase.getDatabase(this).categoryDao()
        lifecycleScope.launch {
            dao.getAllCategories().collect { adapter.submit(it) }
        }
    }
}