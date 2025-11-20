package com.edunova.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.edunova.domain.model.Lesson

@Entity(tableName = "lessons")
data class LessonEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val subject: String,
    val grade: String,
    val difficulty: String, // EASY, MEDIUM, HARD
    val content: String, // JSON or markdown content
    val estimatedDuration: Int, // in minutes
    val language: String,
    val isDownloaded: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toDomain(): Lesson {
        return Lesson(
            id = id,
            title = title,
            description = description,
            subject = subject,
            grade = grade,
            difficulty = difficulty,
            content = content,
            estimatedDuration = estimatedDuration,
            language = language,
            isDownloaded = isDownloaded
        )
    }
}

fun Lesson.toEntity(): LessonEntity {
    return LessonEntity(
        id = id,
        title = title,
        description = description,
        subject = subject,
        grade = grade,
        difficulty = difficulty,
        content = content,
        estimatedDuration = estimatedDuration,
        language = language,
        isDownloaded = isDownloaded
    )
}

