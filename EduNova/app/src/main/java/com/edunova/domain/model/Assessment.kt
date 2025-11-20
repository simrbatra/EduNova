package com.edunova.domain.model

data class Assessment(
    val id: String,
    val studentId: String,
    val lessonId: String,
    val score: Float,
    val totalQuestions: Int,
    val correctAnswers: Int,
    val assessmentType: String,
    val questions: String,
    val answers: String,
    val timeTaken: Long,
    val createdAt: Long
)

