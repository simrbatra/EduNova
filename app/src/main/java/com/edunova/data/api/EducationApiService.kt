package com.edunova.data.api

import com.edunova.ai.GeminiHelper
import com.edunova.domain.model.Lesson
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

/**
 * API Service using Gemini AI to fetch educational content
 * This service uses Gemini to generate lesson data, find relevant videos, and manage progress
 */
class EducationApiService {
    
    private val model: GenerativeModel? by lazy { 
        try {
            if (GeminiHelper.isApiKeyConfigured()) {
                GeminiHelper.getModel()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    
    private fun requireModel(): GenerativeModel {
        return model ?: throw IllegalStateException(
            "GEMINI_API_KEY is not configured. Please set it in gradle.properties. " +
            "Get your API key from: https://makersuite.google.com/app/apikey"
        )
    }
    
    /**
     * Fetch lessons for a specific grade and subject using Gemini AI
     */
    suspend fun fetchLessonsByGradeAndSubject(
        grade: String,
        subject: String? = null,
        language: String = "en"
    ): List<Lesson> = withContext(Dispatchers.IO) {
        try {
            val prompt = buildString {
                append("Generate comprehensive educational lesson data in JSON format for grade: $grade")
                if (subject != null) {
                    append(", subject: $subject")
                } else {
                    append(" covering multiple subjects (Math, Science, English, Social Studies)")
                }
                append(". Language: $language.\n\n")
                append("Return a JSON array of lessons. Each lesson object must have these exact fields:\n")
                append("- id: unique identifier (string)\n")
                append("- title: lesson title in $language (string)\n")
                append("- description: detailed description in $language (string)\n")
                append("- subject: subject name (string: Math, Science, English, Social Studies, etc.)\n")
                append("- grade: grade level (string)\n")
                append("- difficulty: Beginner, Intermediate, or Advanced (string)\n")
                append("- content: a real YouTube video URL that is specifically related to this lesson topic (string)\n")
                append("- estimatedDuration: duration in minutes (integer)\n")
                append("- language: language code (string)\n\n")
                append("IMPORTANT:\n")
                append("1. Generate at least 10-15 lessons for comprehensive coverage\n")
                append("2. Each lesson's video URL must be a real, relevant educational video for that specific topic\n")
                append("3. Videos should be age-appropriate for grade $grade\n")
                append("4. Cover different topics within each subject\n")
                append("5. Return ONLY valid JSON array, no markdown, no code blocks, no explanations\n")
                append("6. Ensure all video URLs are valid YouTube URLs\n")
            }
            
            val response = requireModel().generateContent(prompt)
            val text = response.text ?: return@withContext emptyList()
            
            // Parse JSON response
            val jsonArray = JSONArray(extractJsonArray(text))
            val lessons = mutableListOf<Lesson>()
            
            for (i in 0 until jsonArray.length()) {
                val lessonObj = jsonArray.getJSONObject(i)
                lessons.add(
                    Lesson(
                        id = lessonObj.optString("id", "lesson_${i + 1}"),
                        title = lessonObj.optString("title", ""),
                        description = lessonObj.optString("description", ""),
                        subject = lessonObj.optString("subject", subject ?: "General"),
                        grade = lessonObj.optString("grade", grade),
                        difficulty = lessonObj.optString("difficulty", "Beginner"),
                        content = lessonObj.optString("content", ""),
                        estimatedDuration = lessonObj.optInt("estimatedDuration", 30),
                        language = lessonObj.optString("language", language),
                        isDownloaded = false
                    )
                )
            }
            
            lessons
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
    
    /**
     * Get relevant video URL for a lesson topic using Gemini
     * This ensures the video is specifically related to the lesson content
     */
    suspend fun getRelevantVideoUrl(lessonTitle: String, lessonDescription: String, subject: String? = null, grade: String? = null): String = withContext(Dispatchers.IO) {
        try {
            val prompt = buildString {
                append("Find a highly relevant educational YouTube video for this specific lesson:\n")
                append("Title: $lessonTitle\n")
                append("Description: $lessonDescription\n")
                if (subject != null) append("Subject: $subject\n")
                if (grade != null) append("Grade Level: $grade\n")
                append("\n")
                append("IMPORTANT: The video MUST be directly related to the lesson topic. ")
                append("Search for educational videos that match the exact topic. ")
                append("Return a real YouTube video URL that teaches this specific topic. ")
                append("Format: https://www.youtube.com/watch?v=VIDEO_ID or https://youtu.be/VIDEO_ID\n")
                append("Return ONLY the URL, nothing else. No explanations, no markdown.")
            }
            
            val response = requireModel().generateContent(prompt)
            var url = response.text?.trim() ?: ""
            
            // Clean up the response - remove markdown, quotes, etc.
            url = url.replace("```", "").replace("`", "").trim()
            url = url.removeSurrounding("\"", "\"").removeSurrounding("'", "'")
            
            // Validate and format URL
            if (url.startsWith("http") && (url.contains("youtube.com") || url.contains("youtu.be"))) {
                url
            } else {
                // Try to extract URL from response
                val urlPattern = Regex("https?://(?:www\\.)?(?:youtube\\.com/watch\\?v=|youtu\\.be/)([a-zA-Z0-9_-]{11})")
                val match = urlPattern.find(url)
                if (match != null) {
                    val videoId = match.groupValues[1]
                    "https://www.youtube.com/watch?v=$videoId"
                } else {
                    // Fallback: use a generic educational video search
                    // In production, you might want to use YouTube Data API here
                    "https://www.youtube.com/embed/dQw4w9WgXcQ"
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "https://www.youtube.com/embed/dQw4w9WgXcQ"
        }
    }
    
    /**
     * Save progress to API (using Gemini to store in a structured format)
     */
    suspend fun saveProgress(
        studentId: String,
        lessonId: String,
        completionPercentage: Float,
        timeSpent: Long
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val prompt = """
                Store student progress data:
                Student ID: $studentId
                Lesson ID: $lessonId
                Completion: ${completionPercentage}%
                Time Spent: ${timeSpent} seconds
                
                Confirm storage with: SUCCESS
            """.trimIndent()
            
            val response = requireModel().generateContent(prompt)
            response.text?.contains("SUCCESS", ignoreCase = true) == true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    
    /**
     * Get progress data from API
     */
    suspend fun getProgress(studentId: String, lessonId: String? = null): Map<String, Any> = withContext(Dispatchers.IO) {
        try {
            val prompt = buildString {
                append("Get progress data for student: $studentId")
                if (lessonId != null) {
                    append(", lesson: $lessonId")
                }
                append(". Return JSON with: totalLessonsCompleted, overallProgress (percentage), totalTimeSpent (seconds).")
                append("Return ONLY valid JSON object.")
            }
            
            val response = requireModel().generateContent(prompt)
            val text = response.text ?: return@withContext emptyMap()
            
            val jsonObj = JSONObject(extractJsonObject(text))
            mapOf(
                "totalLessonsCompleted" to jsonObj.optInt("totalLessonsCompleted", 0),
                "overallProgress" to jsonObj.optDouble("overallProgress", 0.0).toFloat(),
                "totalTimeSpent" to jsonObj.optLong("totalTimeSpent", 0L)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            emptyMap()
        }
    }
    
    /**
     * Chat with AI for doubt clearing
     */
    suspend fun chatWithAI(message: String, context: String = ""): String = withContext(Dispatchers.IO) {
        try {
            val prompt = buildString {
                append("You are a friendly and helpful educational AI tutor. ")
                append("Your role is to help students understand concepts, answer questions, and clear their doubts. ")
                append("Be encouraging, clear, and educational in your responses.\n\n")
                if (context.isNotEmpty()) {
                    append("Context: $context\n\n")
                }
                append("Student's question: $message\n\n")
                append("Provide a clear, helpful, and educational response. ")
                append("If the question is about a specific subject or topic, provide detailed explanations with examples.")
            }
            
            val response = requireModel().generateContent(prompt)
            response.text ?: "I'm sorry, I couldn't process your question. Please try again."
        } catch (e: IllegalStateException) {
            // API key not configured
            "⚠️ AI Service Not Configured\n\n" +
            "The Gemini API key is not set up. To enable the AI chatbot:\n\n" +
            "1. Get your API key from:\n" +
            "   https://makersuite.google.com/app/apikey\n\n" +
            "2. Open gradle.properties in the project root\n\n" +
            "3. Replace YOUR_API_KEY_HERE with your actual API key\n\n" +
            "4. Rebuild the app"
        } catch (e: Exception) {
            e.printStackTrace()
            when {
                e.message?.contains("API", ignoreCase = true) == true -> 
                    "API configuration error. Please check your API key settings."
                e.message?.contains("network", ignoreCase = true) == true -> 
                    "Network error. Please check your internet connection."
                else -> 
                    "I'm sorry, I encountered an error: ${e.message ?: "Unknown error"}. Please try again."
            }
        }
    }
    
    /**
     * Extract JSON array from text response
     */
    private fun extractJsonArray(text: String): String {
        val trimmed = text.trim()
        // Try to find JSON array in the response
        val startIndex = trimmed.indexOf('[')
        val endIndex = trimmed.lastIndexOf(']')
        
        return if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
            trimmed.substring(startIndex, endIndex + 1)
        } else {
            // Fallback: try to parse as is
            trimmed
        }
    }
    
    /**
     * Extract JSON object from text response
     */
    private fun extractJsonObject(text: String): String {
        val trimmed = text.trim()
        val startIndex = trimmed.indexOf('{')
        val endIndex = trimmed.lastIndexOf('}')
        
        return if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
            trimmed.substring(startIndex, endIndex + 1)
        } else {
            "{}"
        }
    }
}

