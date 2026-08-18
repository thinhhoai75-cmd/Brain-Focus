package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FocusTipDao {
    @Query("SELECT * FROM focus_tips ORDER BY id ASC")
    fun getAllTips(): Flow<List<FocusTipEntity>>

    @Query("SELECT COUNT(*) FROM focus_tips")
    suspend fun getTipsCount(): Int

    @Query("SELECT * FROM focus_tips")
    suspend fun getAllTipsList(): List<FocusTipEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tips: List<FocusTipEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTip(tip: FocusTipEntity)

    @Update
    suspend fun updateTip(tip: FocusTipEntity)
}
