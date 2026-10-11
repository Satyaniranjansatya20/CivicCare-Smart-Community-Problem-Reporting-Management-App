package com.example.civiccare.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.civiccare.data.model.Category
import com.example.civiccare.data.model.Complaint

@Database(
    entities = [Complaint::class, Category::class],
    version = 1,
    exportSchema = false
)
abstract class CivicCareDatabase : RoomDatabase() {

    abstract fun complaintDao(): ComplaintDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        @Volatile
        private var INSTANCE: CivicCareDatabase? = null

        fun getDatabase(context: Context): CivicCareDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    CivicCareDatabase::class.java,
                    "civiccare_db"
                ).addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        listOf(
                            "Road Damage", "Garbage Issue", "Water Leakage",
                            "Street Light Problem", "Drainage Problem",
                            "Electricity Issue", "Public Safety"
                        ).forEach {
                            db.execSQL("INSERT INTO categories (categoryName) VALUES ('$it')")
                        }
                    }
                }).build().also { INSTANCE = it }
            }
    }
}