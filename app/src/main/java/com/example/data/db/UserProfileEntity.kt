package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val name: String,
    val email: String,
    val avatarIcon: String = "brain", // "brain", "owl", "cat", "monk", "astronaut", "fox"
    val isAssessmentCompleted: Boolean = false,
    val fbsScore: Int = 500, // Focus Brain Score (0 - 1000)
    val rankingPoints: Int = 1000, // Starting default ranking points
    val totalFocusMinutes: Int = 0,
    val completedSessionsCount: Int = 0,
    val dailySessionsToday: Int = 0,
    val lastSessionDate: String = "", // Format: YYYY-MM-DD
    val studyStreak: Int = 0, // Current active daily streak
    val lastStreakDate: String = "", // Format: YYYY-MM-DD
    val dailyReminderEnabled: Boolean = true, // Daily study reminder toggle
    val dailyReminderTime: String = "20:00", // Daily reminder time string
    val githubUrl: String = "", // GitHub repository or profile link
    val createdAt: Long = System.currentTimeMillis()
) {
    fun getActiveStreak(today: String, yesterday: String): Int {
        return when {
            lastStreakDate == today || lastStreakDate == yesterday -> studyStreak
            else -> 0
        }
    }
}

