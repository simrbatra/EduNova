package com.edunova.ai

import android.util.Log
import com.edunova.domain.model.StudentProgress
import kotlin.math.max
import kotlin.math.min

/**
 * Adaptive Learning & Assessment Engine
 * Tracks student behavior and recommends personalized learning paths
 */
class AdaptiveLearningEngine {
    
    private val TAG = "AdaptiveLearningEngine"
    
    /**
     * Analyze student performance and update learning path
     */
    fun analyzePerformance(
        progressHistory: List<StudentProgress>,
        recentAssessments: List<Float>
    ): LearningAnalysis {
        val avgCompletion = progressHistory.map { it.completionPercentage }.average()
        val avgScore = recentAssessments.average()
        val avgTimeSpent = progressHistory.map { it.timeSpent }.average()
        val totalAttempts = progressHistory.sumOf { it.attempts }
        
        val learningSpeed = calculateLearningSpeed(avgTimeSpent, avgCompletion)
        val confidenceLevel = calculateConfidenceLevel(avgScore, totalAttempts)
        val attentionSpan = estimateAttentionSpan(progressHistory)
        
        return LearningAnalysis(
            learningSpeed = learningSpeed,
            confidenceLevel = confidenceLevel,
            attentionSpan = attentionSpan,
            recommendedDifficulty = recommendDifficulty(avgScore, avgCompletion),
            weakAreas = identifyWeakAreas(progressHistory),
            strongAreas = identifyStrongAreas(progressHistory)
        )
    }
    
    /**
     * Recommend next lesson based on student profile
     */
    fun recommendNextLesson(
        studentId: String,
        completedLessons: List<String>,
        performanceHistory: Map<String, Float>,
        availableLessons: List<String>
    ): String? {
        // Khan Academy-style mastery model
        val masteredTopics = performanceHistory.filter { it.value >= 0.8 }.keys
        val strugglingTopics = performanceHistory.filter { it.value < 0.5 }.keys
        
        return when {
            // If student is struggling, recommend review
            strugglingTopics.isNotEmpty() -> {
                strugglingTopics.firstOrNull()
            }
            // If student has mastered current topic, move to next
            masteredTopics.isNotEmpty() -> {
                availableLessons.firstOrNull { it !in completedLessons }
            }
            // Continue with current difficulty
            else -> {
                availableLessons.firstOrNull { it !in completedLessons }
            }
        }
    }
    
    /**
     * Generate personalized quiz based on student's weak areas
     */
    fun generatePersonalizedQuiz(
        weakAreas: List<String>,
        difficulty: String
    ): List<QuizQuestion> {
        // In production, query question bank based on weak areas
        return listOf(
            QuizQuestion(
                id = "q1",
                question = "Sample question for weak area: ${weakAreas.firstOrNull() ?: "general"}",
                options = listOf("Option A", "Option B", "Option C", "Option D"),
                correctAnswer = 0,
                difficulty = difficulty
            )
        )
    }
    
    private fun calculateLearningSpeed(avgTimeSpent: Double, avgCompletion: Double): String {
        return when {
            avgTimeSpent < 300 && avgCompletion > 0.8 -> "FAST"
            avgTimeSpent > 900 && avgCompletion < 0.5 -> "SLOW"
            else -> "MODERATE"
        }
    }
    
    private fun calculateConfidenceLevel(avgScore: Double, totalAttempts: Int): Float {
        val baseConfidence = min(1.0f, (avgScore / 100).toFloat())
        val attemptPenalty = max(0.0f, 1.0f - (totalAttempts * 0.05f))
        return baseConfidence * attemptPenalty
    }
    
    private fun estimateAttentionSpan(progressHistory: List<StudentProgress>): Long {
        // Estimate based on average time spent per session
        val avgTime = progressHistory.map { it.timeSpent }.average()
        return (avgTime * 1.5).toLong() // Add 50% buffer
    }
    
    private fun recommendDifficulty(avgScore: Double, avgCompletion: Double): String {
        val combinedScore = (avgScore + avgCompletion * 100) / 2
        return when {
            combinedScore < 40 -> "EASY"
            combinedScore < 70 -> "MEDIUM"
            else -> "HARD"
        }
    }
    
    private fun identifyWeakAreas(progressHistory: List<StudentProgress>): List<String> {
        // In production, analyze actual weak areas from progress data
        return progressHistory
            .filter { it.completionPercentage < 0.6 }
            .map { it.lessonId }
            .take(3)
    }
    
    private fun identifyStrongAreas(progressHistory: List<StudentProgress>): List<String> {
        return progressHistory
            .filter { it.completionPercentage > 0.8 }
            .map { it.lessonId }
            .take(3)
    }
}

data class LearningAnalysis(
    val learningSpeed: String,
    val confidenceLevel: Float,
    val attentionSpan: Long,
    val recommendedDifficulty: String,
    val weakAreas: List<String>,
    val strongAreas: List<String>
)

data class QuizQuestion(
    val id: String,
    val question: String,
    val options: List<String>,
    val correctAnswer: Int,
    val difficulty: String
)

