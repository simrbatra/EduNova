package com.edunova.ui.screens.lesson

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.MediaController
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.edunova.databinding.FragmentLessonDetailBinding
import kotlinx.coroutines.launch

class LessonDetailFragment : Fragment() {

	private var _binding: FragmentLessonDetailBinding? = null
	private val binding get() = _binding!!

	private val viewModel: LessonViewModel by viewModels()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentLessonDetailBinding.inflate(inflater, container, false)
		return binding.root
	}

	private var currentLesson: com.edunova.domain.model.Lesson? = null
	private var startTime: Long = 0
	private val progressViewModel: com.edunova.ui.screens.progress.ProgressViewModel by viewModels()

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		setupWebView()
		setupProgressTracking()
		
		val lessonId = arguments?.getString("lessonId")
		if (lessonId != null) {
			viewLifecycleOwner.lifecycleScope.launch {
				viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
					binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
				}
				viewModel.lessons.observe(viewLifecycleOwner) { list ->
					val lesson = list.firstOrNull { it.id == lessonId }
					lesson?.let { 
						currentLesson = it
						startTime = System.currentTimeMillis()
						// Get relevant video URL from API
						viewLifecycleOwner.lifecycleScope.launch {
							val videoUrl = viewModel.getRelevantVideoUrl(it)
							bindLesson(it.title, it.description, videoUrl)
						}
					}
				}
			}
		}
	}
	
	private fun setupProgressTracking() {
		// Track when user views the lesson
		startTime = System.currentTimeMillis()
		
		// Save progress when fragment is destroyed
		viewLifecycleOwner.lifecycle.addObserver(object : androidx.lifecycle.DefaultLifecycleObserver {
			override fun onDestroy(owner: androidx.lifecycle.LifecycleOwner) {
				currentLesson?.let { lesson ->
					val timeSpent = (System.currentTimeMillis() - startTime) / 1000 // in seconds
					// Assume 50% completion if user spent at least 30 seconds
					val completionPercentage = if (timeSpent >= 30) 50f else (timeSpent / 30f * 50f).coerceAtMost(50f)
					progressViewModel.saveProgress(lesson.id, completionPercentage, timeSpent)
				}
			}
		})
	}
	
	private fun setupWebView() {
		binding.webViewVideo.settings.apply {
			javaScriptEnabled = true
			domStorageEnabled = true
			loadWithOverviewMode = true
			useWideViewPort = true
		}
		binding.webViewVideo.webChromeClient = WebChromeClient()
		binding.webViewVideo.webViewClient = WebViewClient()
	}

	private fun bindLesson(title: String, description: String, contentUrl: String) {
		binding.textTitle.text = title
		binding.textDescription.text = description
		
		// Handle YouTube URLs and other video URLs
		if (contentUrl.contains("youtube.com") || contentUrl.contains("youtu.be")) {
			// Extract video ID from YouTube URL
			val videoId = extractYouTubeVideoId(contentUrl)
			if (videoId != null) {
				// Use WebView to play YouTube video
				binding.videoView.visibility = View.GONE
				binding.webViewVideo.visibility = View.VISIBLE
				
				// Load YouTube embed URL
				val embedUrl = "https://www.youtube.com/embed/$videoId"
				binding.webViewVideo.loadUrl(embedUrl)
			} else {
				// Fallback: try to load the URL directly
				binding.videoView.visibility = View.GONE
				binding.webViewVideo.visibility = View.VISIBLE
				binding.webViewVideo.loadUrl(contentUrl)
			}
		} else if (contentUrl.startsWith("http")) {
			// Direct video URL - use VideoView
			binding.webViewVideo.visibility = View.GONE
			binding.videoView.visibility = View.VISIBLE
			
			val controller = MediaController(requireContext())
			controller.setAnchorView(binding.videoView)
			binding.videoView.setMediaController(controller)
			binding.videoView.setVideoURI(Uri.parse(contentUrl))
			binding.videoView.requestFocus()
			binding.videoView.start()
		} else {
			binding.videoView.visibility = View.GONE
			binding.webViewVideo.visibility = View.GONE
		}
	}
	
	private fun extractYouTubeVideoId(url: String): String? {
		val patterns = listOf(
			Regex("youtube\\.com/watch\\?v=([^&]+)"),
			Regex("youtu\\.be/([^?]+)"),
			Regex("youtube\\.com/embed/([^?]+)")
		)
		
		for (pattern in patterns) {
			val match = pattern.find(url)
			if (match != null) {
				return match.groupValues[1]
			}
		}
		return null
	}

	override fun onDestroyView() {
		super.onDestroyView()
		binding.webViewVideo.destroy()
		_binding = null
	}
}


