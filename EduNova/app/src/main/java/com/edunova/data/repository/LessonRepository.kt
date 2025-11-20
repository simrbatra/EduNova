package com.edunova.data.repository

import com.edunova.data.local.dao.LessonDao
import com.edunova.data.local.entity.LessonEntity
import com.edunova.domain.model.Lesson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LessonRepository(
    private val lessonDao: LessonDao
) {
    fun getAllLessons(): Flow<List<Lesson>> {
        return lessonDao.getAllLessons().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    suspend fun getLessonById(id: String): Lesson? {
        return lessonDao.getLessonById(id)?.toDomain()
    }
    
    fun getLessonsByGradeAndSubject(grade: String, subject: String): Flow<List<Lesson>> {
        return lessonDao.getLessonsByGradeAndSubject(grade, subject).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    fun getDownloadedLessons(): Flow<List<Lesson>> {
        return lessonDao.getDownloadedLessons().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    suspend fun insertLesson(lesson: Lesson) {
        lessonDao.insertLesson(
            LessonEntity(
                id = lesson.id,
                title = lesson.title,
                description = lesson.description,
                subject = lesson.subject,
                grade = lesson.grade,
                difficulty = lesson.difficulty,
                content = lesson.content,
                estimatedDuration = lesson.estimatedDuration,
                language = lesson.language,
                isDownloaded = lesson.isDownloaded
            )
        )
    }
    
    suspend fun insertLessons(lessons: List<Lesson>) {
        lessonDao.insertLessons(
            lessons.map { lesson ->
                LessonEntity(
                    id = lesson.id,
                    title = lesson.title,
                    description = lesson.description,
                    subject = lesson.subject,
                    grade = lesson.grade,
                    difficulty = lesson.difficulty,
                    content = lesson.content,
                    estimatedDuration = lesson.estimatedDuration,
                    language = lesson.language,
                    isDownloaded = lesson.isDownloaded
                )
            }
        )
    }
    
    suspend fun updateDownloadStatus(lessonId: String, isDownloaded: Boolean) {
        lessonDao.updateDownloadStatus(lessonId, isDownloaded)
    }
}

