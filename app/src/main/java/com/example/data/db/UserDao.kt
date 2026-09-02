package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getUserProfileFlow(): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    suspend fun getUserProfileOnce(): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfileEntity)

    @Update
    suspend fun updateProfile(profile: UserProfileEntity)

    @Query("UPDATE user_profile SET fbsScore = :newScore WHERE id = 1")
    suspend fun updateFbsScore(newScore: Int)

    @Query("UPDATE user_profile SET currentPoints = currentPoints + :pointsDelta, fbsScore = MAX(100, MIN(1000, fbsScore + :fbsDelta)) WHERE id = 1")
    suspend fun adjustPointsAndFbs(pointsDelta: Int, fbsDelta: Int)
}
