package com.edunova.data.settings

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

class SettingsManager(context: Context) {

	private val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

	fun isDarkMode(): Boolean = prefs.getBoolean(KEY_DARK, false)

	fun setDarkMode(enabled: Boolean) {
		prefs.edit().putBoolean(KEY_DARK, enabled).apply()
		AppCompatDelegate.setDefaultNightMode(
			if (enabled) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
		)
	}

	fun getLanguageCode(): String = prefs.getString(KEY_LANG, "en") ?: "en"

	fun setLanguageCode(code: String) {
		prefs.edit().putString(KEY_LANG, code).apply()
	}

	private companion object {
		const val PREFS = "edunova_settings"
		const val KEY_DARK = "dark_mode"
		const val KEY_LANG = "language_code"
	}
}


