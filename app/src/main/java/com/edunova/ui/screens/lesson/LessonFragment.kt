package com.edunova.ui.screens.lesson

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.edunova.databinding.FragmentLessonBinding
import com.edunova.databinding.ItemLessonBinding
import com.edunova.domain.model.Lesson
import kotlinx.coroutines.launch

class LessonFragment : Fragment() {
    
    private var _binding: FragmentLessonBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: LessonViewModel by viewModels()
    private lateinit var lessonAdapter: LessonAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLessonBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupObservers()
        setupFilterButtons()
    }
    
    private fun setupRecyclerView() {
        lessonAdapter = LessonAdapter { lesson ->
            findNavController().navigate(
                com.edunova.R.id.nav_lesson_detail,
                bundleOf("lessonId" to lesson.id)
            )
        }
        
        binding.recyclerViewLessons.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = lessonAdapter
        }
    }
    
    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.lessons.observe(viewLifecycleOwner) { lessons ->
                lessonAdapter.submitList(lessons)
            }
            
            viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }
    }
    
    private fun setupFilterButtons() {
        binding.btnFilterAll.setOnClickListener {
            viewModel.clearFilter()
            updateFilterButtons(null)
        }
        
        binding.btnFilterMath.setOnClickListener {
            viewModel.filterBySubject("Math")
            updateFilterButtons("Math")
        }
        
        binding.btnFilterScience.setOnClickListener {
            viewModel.filterBySubject("Science")
            updateFilterButtons("Science")
        }
        
        binding.btnFilterEnglish.setOnClickListener {
            viewModel.filterBySubject("English")
            updateFilterButtons("English")
        }
    }
    
    private fun updateFilterButtons(selectedSubject: String?) {
        binding.btnFilterAll.isSelected = selectedSubject == null
        binding.btnFilterMath.isSelected = selectedSubject == "Math"
        binding.btnFilterScience.isSelected = selectedSubject == "Science"
        binding.btnFilterEnglish.isSelected = selectedSubject == "English"
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// Simple RecyclerView Adapter
class LessonAdapter(
    private val onLessonClick: (Lesson) -> Unit
) : ListAdapter<Lesson, LessonAdapter.LessonViewHolder>(
    LessonDiffCallback()
) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        val binding = ItemLessonBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LessonViewHolder(binding, onLessonClick)
    }
    
    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    class LessonViewHolder(
        private val binding: ItemLessonBinding,
        private val onLessonClick: (Lesson) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(lesson: Lesson) {
            binding.textLessonTitle.text = lesson.title
            binding.textLessonDescription.text = lesson.description
            binding.textLessonSubject.text = lesson.subject
            binding.textLessonGrade.text = lesson.grade
            binding.textLessonDifficulty.text = lesson.difficulty
            binding.textLessonDuration.text = "${lesson.estimatedDuration} min"
            
            binding.root.setOnClickListener {
                onLessonClick(lesson)
            }
        }
    }
}

class LessonDiffCallback : DiffUtil.ItemCallback<Lesson>() {
    override fun areItemsTheSame(oldItem: Lesson, newItem: Lesson): Boolean {
        return oldItem.id == newItem.id
    }
    
    override fun areContentsTheSame(oldItem: Lesson, newItem: Lesson): Boolean {
        return oldItem == newItem
    }
}

