package com.edunova

import android.app.Application
import android.content.res.Configuration
import java.util.Locale
import androidx.appcompat.app.AppCompatDelegate
import com.edunova.data.settings.SettingsManager

class EduNovaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val settings = SettingsManager(this)
        // Apply dark mode
        AppCompatDelegate.setDefaultNightMode(
            if (settings.isDarkMode()) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
        // Apply language
        applyLanguage(settings.getLanguageCode())
    }

    private fun applyLanguage(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        @Suppress("DEPRECATION")
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}

