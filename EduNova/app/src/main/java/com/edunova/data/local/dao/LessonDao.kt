package com.edunova.data.local.dao

import androidx.room.*
import com.edunova.data.local.entity.LessonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LessonDao {
    @Query("SELECT * FROM lessons")
    fun getAllLessons(): Flow<List<LessonEntity>>

    @Query("SELECT * FROM lessons WHERE id = :id")
    suspend fun getLessonById(id: String): LessonEntity?

    @Query("SELECT * FROM lessons WHERE grade = :grade AND subject = :subject")
    fun getLessonsByGradeAndSubject(grade: String, subject: String): Flow<List<LessonEntity>>

    @Query("SELECT * FROM lessons WHERE isDownloaded = 1")
    fun getDownloadedLessons(): Flow<List<LessonEntity>>

    @Query("SELECT * FROM lessons WHERE language = :language")
    fun getLessonsByLanguage(language: String): Flow<List<LessonEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLesson(lesson: LessonEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLessons(lessons: List<LessonEntity>)

    @Update
    suspend fun updateLesson(lesson: LessonEntity)

    @Query("UPDATE lessons SET isDownloaded = :isDownloaded WHERE id = :lessonId")
    suspend fun updateDownloadStatus(lessonId: String, isDownloaded: Boolean)

    @Delete
    suspend fun deleteLesson(lesson: LessonEntity)
    @Query("SELECT * FROM lessons")
    suspend fun getAllNow(): List<LessonEntity>
}

