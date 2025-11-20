package com.edunova.ai

import android.util.Log
import com.edunova.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel

object GeminiHelper {
    @Volatile
    private var modelInstance: GenerativeModel? = null
    
    private const val TAG = "GeminiHelper"
    
    /**
     * Check if API key is configured
     */
    fun isApiKeyConfigured(): Boolean {
        val apiKey = BuildConfig.GEMINI_API_KEY
        return apiKey.isNotBlank() && apiKey != "YOUR_API_KEY_HERE"
    }
    
    /**
     * Get the Gemini model instance
     * @throws IllegalStateException if API key is not configured
     */
    fun getModel(): GenerativeModel {
        return modelInstance ?: synchronized(this) {
            modelInstance ?: try {
                val apiKey = BuildConfig.GEMINI_API_KEY
                if (apiKey.isBlank() || apiKey == "YOUR_API_KEY_HERE") {
                    val errorMsg = "GEMINI_API_KEY is not configured. Please set it in gradle.properties. " +
                            "Get your API key from: https://makersuite.google.com/app/apikey"
                    Log.e(TAG, errorMsg)
                    throw IllegalStateException(errorMsg)
                }
                
                GenerativeModel(
                    modelName = "gemini-1.5-flash",
                    apiKey = apiKey
                ).also { 
                    modelInstance = it
                    Log.d(TAG, "Gemini model initialized successfully")
                }
            } catch (e: IllegalStateException) {
                // Re-throw configuration errors as-is
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Failed to initialize Gemini model", e)
                throw RuntimeException("Failed to initialize Gemini model: ${e.message}. Please check your API key configuration.", e)
            }
        }
    }
    
    /**
     * Get model if available, or null if not configured
     */
    fun getModelOrNull(): GenerativeModel? {
        return try {
            if (isApiKeyConfigured()) getModel() else null
        } catch (e: Exception) {
            null
        }
    }
    
    fun resetModel() {
        synchronized(this) {
            modelInstance = null
        }
    }
}
