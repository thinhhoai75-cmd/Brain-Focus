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
    fun getAllReminders(): Flow<List<FocusReminderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: FocusReminderEntity)

    @Update
    suspend fun updateReminder(reminder: FocusReminderEntity)

    @Delete
    suspend fun deleteReminder(reminder: FocusReminderEntity)

    @Query("UPDATE focus_reminders SET isEnabled = :isEnabled WHERE id = :id")
    suspend fun toggleReminder(id: Int, isEnabled: Boolean)

    @Query("SELECT COUNT(*) FROM focus_reminders")
    suspend fun getReminderCount(): Int
}
