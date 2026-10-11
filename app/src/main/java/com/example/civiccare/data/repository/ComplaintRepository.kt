package com.example.civiccare.data.repository

import com.example.civiccare.data.database.ComplaintDao
import com.example.civiccare.data.model.Complaint
import kotlinx.coroutines.flow.Flow

class ComplaintRepository(private val dao: ComplaintDao) {

    val allComplaints: Flow<List<Complaint>> = dao.getAllComplaints()

    suspend fun insert(complaint: Complaint): Long =
        dao.insertComplaint(complaint)

    suspend fun countSimilar(category: String, location: String): Int =
        dao.countSimilar(category, location)
}