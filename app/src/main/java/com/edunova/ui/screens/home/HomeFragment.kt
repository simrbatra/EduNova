package com.edunova.ui.screens.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.edunova.chat.ChatActivity
import com.edunova.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: HomeViewModel by viewModels()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupObservers()
        setupClickListeners()
        setupChatbotButton()
    }
    
    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.level.observe(viewLifecycleOwner) { level ->
                binding.textLevel.text = level.toString()
            }
            
            viewModel.xp.observe(viewLifecycleOwner) { xp ->
                binding.textXp.text = xp.toString()
            }
            
            viewModel.streak.observe(viewLifecycleOwner) { streak ->
                binding.textStreak.text = "$streak days"
            }
            
            viewModel.recommendedLessons.observe(viewLifecycleOwner) { lessons ->
                // Update recommended lessons UI
                setupRecommendedLessons(lessons)
            }
        }
    }
    
    private fun setupClickListeners() {
        binding.btnStartLesson.setOnClickListener {
            findNavController().navigate(com.edunova.R.id.nav_lessons)
        }
        
        binding.btnViewProgress.setOnClickListener {
            findNavController().navigate(com.edunova.R.id.nav_progress)
        }

        binding.btnTakeQuiz.setOnClickListener {
            findNavController().navigate(com.edunova.R.id.nav_quiz)
        }
    }
    
    private fun setupChatbotButton() {
        // Use the FAB from layout
        binding.fabChatbot.setOnClickListener {
            val intent = Intent(requireContext(), ChatActivity::class.java)
            startActivity(intent)
        }
    }
    
    private fun setupRecommendedLessons(lessons: List<com.edunova.domain.model.Lesson>) {
        // Simple horizontal scroll view for recommended lessons
        binding.recommendedLessonsContainer.removeAllViews()
        
        lessons.forEach { lesson ->
            val cardView = com.google.android.material.card.MaterialCardView(requireContext()).apply {
                layoutParams = ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    width = resources.getDimensionPixelSize(android.R.dimen.app_icon_size) * 5
                    height = resources.getDimensionPixelSize(android.R.dimen.app_icon_size) * 3
                    setMargins(8, 8, 8, 8)
                }
                radius = 12f
                elevation = 4f
                
                setOnClickListener {
                    // Tap card -> open subject-specific quiz
                    val subject = lesson.subject
                    val args = android.os.Bundle().apply { putString("selectedSubject", subject) }
                    findNavController().navigate(com.edunova.R.id.nav_quiz, args)
                }
            }
            
            val layout = android.widget.LinearLayout(requireContext()).apply {
                orientation = android.widget.LinearLayout.VERTICAL
                gravity = android.view.Gravity.CENTER
                setPadding(16, 16, 16, 16)
            }
            
            android.widget.TextView(requireContext()).apply {
                text = lesson.subject
                textSize = 18f
                gravity = android.view.Gravity.CENTER
            }.let { layout.addView(it) }
            
            cardView.addView(layout)
            binding.recommendedLessonsContainer.addView(cardView)
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

