package com.edunova.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.edunova.domain.model.Assessment

@Entity(
    tableName = "assessments",
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
data class AssessmentEntity(
    @PrimaryKey
    val id: String,
    val studentId: String,
    val lessonId: String,
    val score: Float,
    val totalQuestions: Int,
    val correctAnswers: Int,
    val assessmentType: String, // QUIZ, TEST, PRACTICE
    val questions: String, // JSON array of questions
    val answers: String, // JSON array of answers
    val timeTaken: Long, // in seconds
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toDomain(): Assessment {
        return Assessment(
            id = id,
            studentId = studentId,
            lessonId = lessonId,
            score = score,
            totalQuestions = totalQuestions,
            correctAnswers = correctAnswers,
            assessmentType = assessmentType,
            questions = questions,
            answers = answers,
            timeTaken = timeTaken,
            createdAt = createdAt
        )
    }
}

fun Assessment.toEntity(): AssessmentEntity {
    return AssessmentEntity(
        id = id,
        studentId = studentId,
        lessonId = lessonId,
        score = score,
        totalQuestions = totalQuestions,
        correctAnswers = correctAnswers,
        assessmentType = assessmentType,
        questions = questions,
        answers = answers,
        timeTaken = timeTaken,
        createdAt = createdAt
    )
}

