package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "focus_reminders")
data class FocusReminderEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String, // "Học tập 90p", "Nghỉ ngơi mắt", "Tập thể dục não", "Cai mạng xã hội"
    val time: String, // e.g. "19:30"
    val frequency: String, // "Hằng ngày", "T2-T6", "Cuối tuần"
    val isEnabled: Boolean = true
)
