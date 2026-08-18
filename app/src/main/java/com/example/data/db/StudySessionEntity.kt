package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "study_sessions")
data class StudySessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val targetMinutes: Int, // e.g., 15, 25, 45, 60, 90
    val actualSecondsCompleted: Int,
    val wereExitsDetected: Boolean,
    val exitCount: Int,
    val pointsEarned: Int,
    val fbsBoostEarned: Int,
    val dateString: String, // YYYY-MM-DD
    val timestamp: Long = System.currentTimeMillis(),
    val emotion: String? = null, // e.g., "🤩 Tuyệt vời", "😊 Hài lòng", "😌 Thư thái", "😐 Bình thường", "😫 Mệt mỏi"
    val reflectionNote: String? = null, // Ghi chú sự tiến bộ của bản thân
    val reflectionBonusPoints: Int = 0,
    val reflectionBonusFbs: Int = 0
)
