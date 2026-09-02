package com.example.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FocusReminderDao {
    @Query("SELECT * FROM focus_reminders ORDER BY time ASC")
    fun getAllRemindersFlow(): Flow<List<FocusReminderEntity>>

    @Query("SELECT COUNT(*) FROM focus_reminders")
    suspend fun getRemindersCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(reminders: List<FocusReminderEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: FocusReminderEntity): Long

    @Update
    suspend fun updateReminder(reminder: FocusReminderEntity)

    @Delete
    suspend fun deleteReminder(reminder: FocusReminderEntity)
}
