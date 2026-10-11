package com.example.civiccare.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "complaints")
data class Complaint(
    @PrimaryKey(autoGenerate = true)
    val complaintId: Int = 0,
    val title: String,
    val category: String,
    val description: String,
    val location: String,
    val priority: String = "Medium",
    val imageUri: String = "",
    val status: String = "Pending",
    val date: String
)