package com.edunova.domain.model

data class StudentProgress(
    val id: String,
    val studentId: String,
    val lessonId: String,
    val completionPercentage: Float,
    val timeSpent: Long,
    val attempts: Int,
    val lastAttemptDate: Long,
    val masteryLevel: String,
    val weakAreas: String? = null,
    val strongAreas: String? = null
)

