package com.edunova.data.local.dao

import androidx.room.*
import com.edunova.data.local.entity.ProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgressDao {
    @Query("SELECT * FROM progress WHERE studentId = :studentId")
    fun getProgressByStudent(studentId: String): Flow<List<ProgressEntity>>

    @Query("SELECT * FROM progress WHERE studentId = :studentId AND lessonId = :lessonId")
    suspend fun getProgressByStudentAndLesson(studentId: String, lessonId: String): ProgressEntity?

    @Query("SELECT * FROM progress WHERE studentId = :studentId ORDER BY lastAttemptDate DESC LIMIT :limit")
    fun getRecentProgress(studentId: String, limit: Int = 10): Flow<List<ProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: ProgressEntity)

    @Update
    suspend fun updateProgress(progress: ProgressEntity)

    @Query("SELECT AVG(completionPercentage) FROM progress WHERE studentId = :studentId")
    suspend fun getAverageCompletion(studentId: String): Float?

    @Query("SELECT COUNT(*) FROM progress WHERE studentId = :studentId AND masteryLevel = 'MASTERED'")
    suspend fun getMasteredLessonsCount(studentId: String): Int
}

