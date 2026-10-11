package com.example.civiccare.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.civiccare.data.model.Complaint
import kotlinx.coroutines.flow.Flow

@Dao
interface ComplaintDao {

    @Insert
    suspend fun insertComplaint(complaint: Complaint): Long

    @Query("SELECT * FROM complaints ORDER BY complaintId DESC")
    fun getAllComplaints(): Flow<List<Complaint>>

    @Query("SELECT COUNT(*) FROM complaints WHERE category = :category AND location = :location AND status != 'Resolved'")
    suspend fun countSimilar(category: String, location: String): Int
}