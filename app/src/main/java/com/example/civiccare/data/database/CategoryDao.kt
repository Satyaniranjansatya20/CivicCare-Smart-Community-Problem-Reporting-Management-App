package com.example.civiccare.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.civiccare.data.model.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Insert
    suspend fun insertCategory(category: Category)

    @Query("SELECT * FROM categories")
    fun getAllCategories(): Flow<List<Category>>
}