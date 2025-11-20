package com.edunova.ai

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.tasks.await

/**
 * Local Language Support
 * Translates content to regional languages using ML Kit Translation
 */
class LanguageTranslator(private val context: Context) {

    private val supportedLanguages = mapOf(
        "hi" to TranslateLanguage.HINDI,
        "te" to TranslateLanguage.TELUGU,
        "ta" to TranslateLanguage.TAMIL,
        "mr" to TranslateLanguage.MARATHI,
        "bn" to TranslateLanguage.BENGALI,
        "gu" to TranslateLanguage.GUJARATI,
        "kn" to TranslateLanguage.KANNADA,
        "ml" to "ml", // manual Malayalam code
        "pa" to "pa", // manual Punjabi code
        "ur" to TranslateLanguage.URDU
    )


    /**
     * Translate text to target language
     */
    suspend fun translate(text: String, targetLanguage: String): String {
        return try {
            val targetLangCode = supportedLanguages[targetLanguage] 
                ?: TranslateLanguage.ENGLISH
            
            val options = TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(targetLangCode)
                .build()
            
            val translator = Translation.getClient(options)
            
            // Download model if needed (works offline after download)
            translator.downloadModelIfNeeded().await()
            
            val result = translator.translate(text).await()
            translator.close()
            
            result
        } catch (e: Exception) {
            Log.e(TAG, "Translation error", e)
            text // Return original if translation fails
        }
    }
    
    /**
     * Check if language model is downloaded
     */
    suspend fun isLanguageModelDownloaded(language: String): Boolean {
        return try {
            val langCode = supportedLanguages[language] ?: return false
            val options = TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(langCode)
                .build()
            
            val translator = Translation.getClient(options)
            val isDownloaded = translator.downloadModelIfNeeded().isComplete
            translator.close()
            isDownloaded
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Get phonetic pronunciation for text (for regional languages)
     */
    fun getPhoneticPronunciation(text: String, language: String): String {
        // In production, use phonetic engine or TTS phonetics
        // For now, return original text
        return text
    }
    
    /**
     * Get supported languages
     */
    fun getSupportedLanguages(): List<String> {
        return supportedLanguages.keys.toList()
    }
}

