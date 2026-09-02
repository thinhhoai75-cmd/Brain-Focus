package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StudySessionDao {
    @Query("SELECT * FROM study_sessions ORDER BY startTimestamp DESC")
    fun getAllSessionsFlow(): Flow<List<StudySessionEntity>>

    @Query("SELECT * FROM study_sessions ORDER BY startTimestamp DESC LIMIT :limit")
    fun getRecentSessionsFlow(limit: Int = 10): Flow<List<StudySessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: StudySessionEntity): Long

    @Query("SELECT COUNT(*) FROM study_sessions WHERE isCompletedWithoutExit = 1")
    fun getCompletedSessionsCountFlow(): Flow<Int>

    @Query("SELECT COALESCE(SUM(actualDurationMinutes), 0) FROM study_sessions")
    fun getTotalMinutesStudiedFlow(): Flow<Long>

    @Query("UPDATE study_sessions SET postSessionEmotion = :emotion, note = :note WHERE id = :sessionId")
    suspend fun updateSessionReflection(sessionId: Long, emotion: String, note: String)
}
