package com.example.civiccare.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.civiccare.data.database.CivicCareDatabase
import com.example.civiccare.data.model.Complaint
import com.example.civiccare.data.repository.ComplaintRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ComplaintViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = ComplaintRepository(
        CivicCareDatabase.getDatabase(app).complaintDao()
    )

    val complaints: Flow<List<Complaint>> = repository.allComplaints

    fun submit(complaint: Complaint, onDone: (Long) -> Unit) {
        viewModelScope.launch {
            val id = repository.insert(complaint)
            onDone(id)
        }
    }

    suspend fun similarCount(category: String, location: String): Int =
        repository.countSimilar(category, location)
}