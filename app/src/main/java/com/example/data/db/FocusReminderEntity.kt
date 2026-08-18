package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "focus_reminders")
data class FocusReminderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val category: String, // "Phiên học", "Nghỉ ngơi", "Game & Bài tập", "Hoạt động khác"
    val time: String, // Format: "HH:mm" e.g., "20:00"
    val frequency: String, // "Hằng ngày", "Thứ 2 - Thứ 6", "Cuối tuần", "Chỉ 1 lần"
    val isEnabled: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
