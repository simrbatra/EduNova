package com.edunova.domain.model

data class GamificationData(
    val studentId: String,
    val xpPoints: Int = 0,
    val level: Int = 1,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val lastActivityDate: Long = System.currentTimeMillis(),
    val badges: String = "[]",
    val achievements: String = "[]"
)

