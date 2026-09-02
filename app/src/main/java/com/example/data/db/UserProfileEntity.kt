package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val username: String = "Bạn học tập",
    val schoolOrGrade: String = "Lớp 12",
    val targetGoal: String = "Ôn thi Đại học & Chống xao nhãng",
    val avatarRes: String = "avatar_default",
    val fbsScore: Int = 500, // Brain Focus Score (BFS: 100 - 1000)
    val totalFocusMinutes: Long = 0,
    val totalSessionsCompleted: Int = 0,
    val streakDays: Int = 1,
    val lastStudyDateTimestamp: Long = 0,
    val currentPoints: Int = 100,
    val rankTitle: String = "Tập sự Tập trung",
    val isAssessmentCompleted: Boolean = false
)
