package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "study_sessions")
data class StudySessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val subjectName: String,
    val plannedDurationMinutes: Int,
    val actualDurationMinutes: Int,
    val startTimestamp: Long,
    val endTimestamp: Long,
    val isCompletedWithoutExit: Boolean,
    val exitAttemptCount: Int = 0,
    val backgroundMusicUsed: String = "Không nhạc",
    val pointsEarned: Int = 0,
    val fbsScoreImpact: Int = 0,
    val postSessionEmotion: String = "Tập trung tốt",
    val note: String = ""
)
