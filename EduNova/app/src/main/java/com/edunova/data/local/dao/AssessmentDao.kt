package com.edunova.data.local.dao

import androidx.room.*
import com.edunova.data.local.entity.AssessmentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AssessmentDao {
    @Query("SELECT * FROM assessments WHERE studentId = :studentId ORDER BY createdAt DESC")
    fun getAssessmentsByStudent(studentId: String): Flow<List<AssessmentEntity>>

    @Query("SELECT * FROM assessments WHERE studentId = :studentId AND lessonId = :lessonId ORDER BY createdAt DESC")
    fun getAssessmentsByStudentAndLesson(studentId: String, lessonId: String): Flow<List<AssessmentEntity>>

    @Query("SELECT * FROM assessments WHERE id = :id")
    suspend fun getAssessmentById(id: String): AssessmentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssessment(assessment: AssessmentEntity)

    @Query("SELECT AVG(score) FROM assessments WHERE studentId = :studentId")
    suspend fun getAverageScore(studentId: String): Float?

    @Query("SELECT * FROM assessments WHERE studentId = :studentId ORDER BY createdAt DESC LIMIT :limit")
    fun getRecentAssessments(studentId: String, limit: Int = 10): Flow<List<AssessmentEntity>>
}

