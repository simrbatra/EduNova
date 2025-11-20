package com.edunova

import android.app.Application
import android.content.res.Configuration
import java.util.Locale
import androidx.appcompat.app.AppCompatDelegate
import com.edunova.data.settings.SettingsManager
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import android.util.Log

class EduNovaApplication : Application() {
    
    companion object {
        private const val TAG = "EduNovaApplication"
    }
    
    override fun onCreate() {
        super.onCreate()
        
        // Set up global exception handler to prevent crashes
        setupGlobalExceptionHandler()
        
        // Initialize Firebase with error handling
        try {
            if (FirebaseApp.getApps(this).isEmpty()) {
                FirebaseApp.initializeApp(this)
                Log.d(TAG, "Firebase initialized successfully")
            }
            // Initialize Analytics (optional, won't crash if not available)
            try {
                val analytics = FirebaseAnalytics.getInstance(this)
                analytics.setAnalyticsCollectionEnabled(true)
            } catch (e: Exception) {
                Log.w(TAG, "Firebase Analytics initialization failed: ${e.message}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Firebase initialization failed: ${e.message}", e)
            // Continue app execution even if Firebase fails
        }
        
        val settings = SettingsManager(this)
        // Apply dark mode
        AppCompatDelegate.setDefaultNightMode(
            if (settings.isDarkMode()) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
        // Apply language
        applyLanguage(settings.getLanguageCode())
    }
    
    private fun setupGlobalExceptionHandler() {
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, exception ->
            // Log specific Firebase/Google Play Services errors but don't crash
            val stackTrace = exception.stackTraceToString()
            if (exception is SecurityException && 
                (stackTrace.contains("GoogleApiManager") || 
                 stackTrace.contains("com.google.android.gms") ||
                 stackTrace.contains("Unknown calling package"))) {
                Log.w(TAG, "Caught Firebase/Google Play Services SecurityException (non-fatal):", exception)
                // These are typically non-fatal warnings, don't crash the app
                return@setDefaultUncaughtExceptionHandler
            }
            // For other exceptions, use default handler
            defaultHandler?.uncaughtException(thread, exception)
        }
    }

    private fun applyLanguage(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        @Suppress("DEPRECATION")
        resources.updateConfiguration(config, resources.displayMetrics)
    }
    
    fun updateLanguage(languageCode: String) {
        applyLanguage(languageCode)
    }
}

