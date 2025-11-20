package com.edunova.ai

import com.edunova.domain.model.GamificationData
import kotlin.math.pow

/**
 * Gamification Engine
 * Manages XP, badges, streaks, and rewards
 */
class GamificationEngine {
    
    /**
     * Calculate XP awarded for completing a lesson
     */
    fun calculateXPForLesson(
        completionPercentage: Float,
        difficulty: String,
        timeSpent: Long
    ): Int {
        val baseXP = when (difficulty) {
            "EASY" -> 10
            "MEDIUM" -> 25
            "HARD" -> 50
            else -> 15
        }
        
        val completionBonus = (completionPercentage * baseXP).toInt()
        val timeBonus = if (timeSpent < 600) 5 else 0 // Bonus for quick completion
        
        return completionBonus + timeBonus
    }
    
    /**
     * Calculate XP for assessment
     */
    fun calculateXPForAssessment(score: Float, totalQuestions: Int): Int {
        val baseXP = totalQuestions * 5
        return (baseXP * (score / 100)).toInt()
    }
    
    /**
     * Calculate level from total XP
     */
    fun calculateLevel(xpPoints: Int): Int {
        // Level formula: level = sqrt(xp / 100)
        return (kotlin.math.sqrt(xpPoints / 100.0)).toInt() + 1
    }
    
    /**
     * Calculate XP needed for next level
     */
    fun xpNeededForNextLevel(currentLevel: Int): Int {
        val nextLevelXP = (currentLevel + 1).toDouble().pow(2) * 100
        val currentLevelXP = currentLevel.toDouble().pow(2) * 100
        return (nextLevelXP - currentLevelXP).toInt()
    }
    
    /**
     * Update streak based on activity date
     */
    fun updateStreak(
        currentStreak: Int,
        lastActivityDate: Long,
        currentDate: Long = System.currentTimeMillis()
    ): Int {
        val daysSinceLastActivity = (currentDate - lastActivityDate) / (1000 * 60 * 60 * 24)
        
        return when {
            daysSinceLastActivity == 0L -> currentStreak // Same day
            daysSinceLastActivity == 1L -> currentStreak + 1 // Next day
            else -> 1 // Streak broken, start over
        }
    }
    
    /**
     * Check for new badges based on achievements
     */
    fun checkForNewBadges(
        gamificationData: GamificationData,
        newXP: Int
    ): List<String> {
        val newBadges = mutableListOf<String>()
        val totalXP = gamificationData.xpPoints + newXP
        
        // Level-based badges
        val newLevel = calculateLevel(totalXP)
        if (newLevel > gamificationData.level) {
            newBadges.add("LEVEL_${newLevel}")
        }
        
        // Streak badges
        if (gamificationData.currentStreak >= 7) {
            newBadges.add("WEEK_STREAK")
        }
        if (gamificationData.currentStreak >= 30) {
            newBadges.add("MONTH_STREAK")
        }
        
        // XP milestones
        when {
            totalXP >= 1000 && !gamificationData.achievements.contains("1000_XP") -> {
                newBadges.add("1000_XP")
            }
            totalXP >= 5000 && !gamificationData.achievements.contains("5000_XP") -> {
                newBadges.add("5000_XP")
            }
            totalXP >= 10000 && !gamificationData.achievements.contains("10000_XP") -> {
                newBadges.add("10000_XP")
            }
        }
        
        return newBadges
    }
    
    /**
     * Get reward message for completing activity
     */
    fun getRewardMessage(xpEarned: Int, newBadges: List<String>): String {
        val messages = mutableListOf<String>()
        
        if (xpEarned > 0) {
            messages.add("Earned $xpEarned XP!")
        }
        
        if (newBadges.isNotEmpty()) {
            messages.add("Unlocked badge: ${newBadges.first()}")
        }
        
        return messages.joinToString(" ")
    }
}

