package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "focus_tips")
data class FocusTipEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String, // "Phương pháp học", "Môi trường", "Cai nghiện số", "Sức khỏe não bộ"
    val description: String,
    val isFavorite: Boolean = false,
    val isApplied: Boolean = false,
    val isCustom: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
