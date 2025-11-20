package com.edunova.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.edunova.domain.model.GamificationData

@Entity(
    tableName = "gamification",
    foreignKeys = [
        ForeignKey(
            entity = StudentEntity::class,
            parentColumns = ["id"],
            childColumns = ["studentId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class GamificationEntity(
    @PrimaryKey
    val studentId: String,
    val xpPoints: Int = 0,
    val level: Int = 1,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val lastActivityDate: Long = System.currentTimeMillis(),
    val badges: String = "[]", // JSON array of badge IDs
    val achievements: String = "[]" // JSON array of achievement IDs
) {
    fun toDomain(): GamificationData {
        return GamificationData(
            studentId = studentId,
            xpPoints = xpPoints,
            level = level,
            currentStreak = currentStreak,
            longestStreak = longestStreak,
            lastActivityDate = lastActivityDate,
            badges = badges,
            achievements = achievements
        )
    }
}

fun GamificationData.toEntity(): GamificationEntity {
    return GamificationEntity(
        studentId = studentId,
        xpPoints = xpPoints,
        level = level,
        currentStreak = currentStreak,
        longestStreak = longestStreak,
        lastActivityDate = lastActivityDate,
        badges = badges,
        achievements = achievements
    )
}

