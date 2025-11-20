package com.edunova.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.edunova.domain.model.StudentProgress

@Entity(
    tableName = "progress",
    foreignKeys = [
        ForeignKey(
            entity = StudentEntity::class,
            parentColumns = ["id"],
            childColumns = ["studentId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = LessonEntity::class,
            parentColumns = ["id"],
            childColumns = ["lessonId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        androidx.room.Index(value = ["studentId"]),
        androidx.room.Index(value = ["lessonId"])
    ]
)
data class ProgressEntity(
    @PrimaryKey
    val id: String,
    val studentId: String,
    val lessonId: String,
    val completionPercentage: Float,
    val timeSpent: Long, // in seconds
    val attempts: Int,
    val lastAttemptDate: Long,
    val masteryLevel: String, // BEGINNER, INTERMEDIATE, ADVANCED, MASTERED
    val weakAreas: String? = null, // JSON array of weak areas
    val strongAreas: String? = null // JSON array of strong areas
) {
    fun toDomain(): StudentProgress {
        return StudentProgress(
            id = id,
            studentId = studentId,
            lessonId = lessonId,
            completionPercentage = completionPercentage,
            timeSpent = timeSpent,
            attempts = attempts,
            lastAttemptDate = lastAttemptDate,
            masteryLevel = masteryLevel,
            weakAreas = weakAreas,
            strongAreas = strongAreas
        )
    }
}

fun StudentProgress.toEntity(): ProgressEntity {
    return ProgressEntity(
        id = id,
        studentId = studentId,
        lessonId = lessonId,
        completionPercentage = completionPercentage,
        timeSpent = timeSpent,
        attempts = attempts,
        lastAttemptDate = lastAttemptDate,
        masteryLevel = masteryLevel,
        weakAreas = weakAreas,
        strongAreas = strongAreas
    )
}

