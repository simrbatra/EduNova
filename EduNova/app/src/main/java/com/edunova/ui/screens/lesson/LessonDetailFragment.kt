package com.edunova.ui.screens.lesson

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		val lessonId = arguments?.getString("lessonId")
		if (lessonId != null) {
			viewLifecycleOwner.lifecycleScope.launch {
				viewModel.isLoading.observe(viewLifecycleOwner) {}
				viewModel.lessons.observe(viewLifecycleOwner) { list ->
					val lesson = list.firstOrNull { it.id == lessonId }
					lesson?.let { bindLesson(it.title, it.description, it.content) }
				}
			}
		}
	}

	private fun bindLesson(title: String, description: String, contentUrl: String) {
		binding.textTitle.text = title
		binding.textDescription.text = description
		if (contentUrl.startsWith("http")) {
			val controller = MediaController(requireContext())
			controller.setAnchorView(binding.videoView)
			binding.videoView.setMediaController(controller)
			binding.videoView.setVideoURI(Uri.parse(contentUrl))
			binding.videoView.requestFocus()
			binding.videoView.start()
		} else {
			binding.videoView.visibility = View.GONE
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}


