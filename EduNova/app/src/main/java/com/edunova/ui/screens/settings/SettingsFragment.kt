package com.edunova.ui.screens.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDelegate
import com.edunova.databinding.FragmentSettingsBinding
import com.edunova.data.settings.SettingsManager

class SettingsFragment : Fragment() {

	private var _binding: FragmentSettingsBinding? = null
	private val binding get() = _binding!!
	private lateinit var settings: SettingsManager

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentSettingsBinding.inflate(inflater, container, false)
		settings = SettingsManager(requireContext())
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		// Dark mode
		binding.switchDarkMode.isChecked = settings.isDarkMode()
		binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
			settings.setDarkMode(isChecked)
		}

		// Language spinner (English/Hindi as example)
		val languages = listOf("English" to "en", "Hindi" to "hi")
		val names = languages.map { it.first }
		binding.spinnerLanguage.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, names)
		val currentCode = settings.getLanguageCode()
		binding.spinnerLanguage.setSelection(languages.indexOfFirst { it.second == currentCode }.coerceAtLeast(0))
		binding.btnApplyLanguage.setOnClickListener {
			val idx = binding.spinnerLanguage.selectedItemPosition
			val code = languages.getOrNull(idx)?.second ?: "en"
			settings.setLanguageCode(code)
			applyLanguageLocal(code)
			// Recreate activity to apply across views
			requireActivity().recreate()
		}
	}

	private fun applyLanguageLocal(languageCode: String) {
		val locale = java.util.Locale(languageCode)
		java.util.Locale.setDefault(locale)
		val res = resources
		val config = android.content.res.Configuration(res.configuration)
		config.setLocale(locale)
		@Suppress("DEPRECATION")
		res.updateConfiguration(config, res.displayMetrics)
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}


