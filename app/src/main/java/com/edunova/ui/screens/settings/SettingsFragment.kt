package com.edunova.ui.screens.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.edunova.databinding.FragmentSettingsBinding
import com.edunova.data.settings.SettingsManager

class SettingsFragment : Fragment() {

	private var _binding: FragmentSettingsBinding? = null
	private val binding get() = _binding!!
	private lateinit var settings: SettingsManager
	private lateinit var prefs: SharedPreferences

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentSettingsBinding.inflate(inflater, container, false)
		settings = SettingsManager(requireContext())
		prefs = requireContext().getSharedPreferences("edunova_prefs", android.content.Context.MODE_PRIVATE)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		setupDarkMode()
		setupLanguage()
		setupNotifications()
		setupDataUsage()
		setupAbout()
	}

	private fun setupDarkMode() {
		binding.switchDarkMode.isChecked = settings.isDarkMode()
		binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
			settings.setDarkMode(isChecked)
			// Apply immediately
			AppCompatDelegate.setDefaultNightMode(
				if (isChecked) AppCompatDelegate.MODE_NIGHT_YES 
				else AppCompatDelegate.MODE_NIGHT_NO
			)
			Toast.makeText(requireContext(), "Theme changed", Toast.LENGTH_SHORT).show()
		}
	}

	private fun setupLanguage() {
		// Language spinner with more options
		val languages = listOf(
			"English" to "en", 
			"Hindi" to "hi", 
			"Spanish" to "es", 
			"French" to "fr",
			"German" to "de",
			"Chinese" to "zh",
			"Japanese" to "ja",
			"Portuguese" to "pt"
		)
		val names = languages.map { it.first }
		binding.spinnerLanguage.adapter = ArrayAdapter(
			requireContext(), 
			android.R.layout.simple_spinner_dropdown_item, 
			names
		)
		val currentCode = settings.getLanguageCode()
		val currentIndex = languages.indexOfFirst { it.second == currentCode }.coerceAtLeast(0)
		binding.spinnerLanguage.setSelection(currentIndex)
		
		binding.btnApplyLanguage.setOnClickListener {
			val idx = binding.spinnerLanguage.selectedItemPosition
			val code = languages.getOrNull(idx)?.second ?: "en"
			settings.setLanguageCode(code)
			applyLanguageLocal(code)
			
			// Update app-wide locale
			val app = requireActivity().application as? com.edunova.EduNovaApplication
			app?.updateLanguage(code)
			
			Toast.makeText(
				requireContext(), 
				"Language changed to ${languages[idx].first}. Restart app for full effect.", 
				Toast.LENGTH_LONG
			).show()
			
			// Reload lessons with new language
			try {
				val lessonViewModel = androidx.lifecycle.ViewModelProvider(requireActivity())[
					com.edunova.ui.screens.lesson.LessonViewModel::class.java
				]
				lessonViewModel.refreshLessons()
			} catch (e: Exception) {
				// ViewModel might not be available, that's okay
			}
		}
	}

	private fun setupNotifications() {
		// Enable/disable notifications setting
		val notificationsEnabled = prefs.getBoolean("notifications_enabled", true)
		// If you have a switch for notifications, set it up here
		// For now, we'll just store the preference
	}

	private fun setupDataUsage() {
		// Data usage settings
		// You can add switches for:
		// - Download lessons only on WiFi
		// - Auto-download recommended lessons
		// - Clear cache
	}

	private fun setupAbout() {
		// About section
		// You can add:
		// - App version
		// - Privacy policy link
		// - Terms of service
		// - Contact support
	}

	private fun applyLanguageLocal(languageCode: String) {
		val locale = java.util.Locale(languageCode)
		java.util.Locale.setDefault(locale)
		val res = resources
		val config = android.content.res.Configuration(res.configuration)
		config.setLocale(locale)
		val context = requireContext().createConfigurationContext(config)
		@Suppress("DEPRECATION")
		res.updateConfiguration(config, res.displayMetrics)
		
		// Also update app-wide locale
		val appContext = requireContext().applicationContext
		val appConfig = appContext.resources.configuration
		appConfig.setLocale(locale)
		appContext.createConfigurationContext(appConfig)
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}


