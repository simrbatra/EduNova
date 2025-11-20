package com.edunova.data.local.dao

import androidx.room.*
import com.edunova.data.local.entity.GamificationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GamificationDao {
    @Query("SELECT * FROM gamification WHERE studentId = :studentId")
    suspend fun getGamificationData(studentId: String): GamificationEntity?

    @Query("SELECT * FROM gamification WHERE studentId = :studentId")
    fun getGamificationDataFlow(studentId: String): Flow<GamificationEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGamificationData(gamification: GamificationEntity)

    @Update
    suspend fun updateGamificationData(gamification: GamificationEntity)

    @Query("UPDATE gamification SET xpPoints = xpPoints + :points WHERE studentId = :studentId")
    suspend fun addXP(studentId: String, points: Int)

    @Query("UPDATE gamification SET currentStreak = :streak, lastActivityDate = :date WHERE studentId = :studentId")
    suspend fun updateStreak(studentId: String, streak: Int, date: Long)
}

