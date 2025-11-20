package com.edunova.ui.screens.progress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.edunova.databinding.FragmentProgressBinding
import com.edunova.databinding.ItemProgressBinding
import com.edunova.domain.model.StudentProgress
import kotlinx.coroutines.launch

class ProgressFragment : Fragment() {
    
    private var _binding: FragmentProgressBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: ProgressViewModel by viewModels()
    private lateinit var progressAdapter: ProgressAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProgressBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupObservers()
    }
    
    private fun setupRecyclerView() {
        progressAdapter = ProgressAdapter()
        binding.recyclerViewProgress.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = progressAdapter
        }
    }
    
    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.overallProgress.observe(viewLifecycleOwner) { progress ->
                binding.progressBarOverall.progress = progress.toInt()
                binding.textOverallProgress.text = "${progress.toInt()}%"
            }
            
            viewModel.totalLessonsCompleted.observe(viewLifecycleOwner) { count ->
                binding.textLessonsCompleted.text = count.toString()
            }
            
            viewModel.totalTimeSpent.observe(viewLifecycleOwner) { timeSpent ->
                binding.textTimeSpent.text = viewModel.formatTime(timeSpent)
            }
            
            viewModel.progressList.observe(viewLifecycleOwner) { progressList ->
                progressAdapter.submitList(progressList)
            }
            
            viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// Simple RecyclerView Adapter
class ProgressAdapter : ListAdapter<StudentProgress, ProgressAdapter.ProgressViewHolder>(
    ProgressDiffCallback()
) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgressViewHolder {
        val binding = ItemProgressBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProgressViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: ProgressViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    class ProgressViewHolder(
        private val binding: ItemProgressBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(progress: StudentProgress) {
            binding.progressBarLesson.progress = progress.completionPercentage.toInt()
            binding.textProgressPercentage.text = "${progress.completionPercentage.toInt()}%"
            binding.textMasteryLevel.text = progress.masteryLevel
            binding.textAttempts.text = "${progress.attempts} attempts"
            
            progress.weakAreas?.let {
                binding.textWeakAreas.text = "Weak: $it"
                binding.textWeakAreas.visibility = View.VISIBLE
            } ?: run {
                binding.textWeakAreas.visibility = View.GONE
            }
            
            progress.strongAreas?.let {
                binding.textStrongAreas.text = "Strong: $it"
                binding.textStrongAreas.visibility = View.VISIBLE
            } ?: run {
                binding.textStrongAreas.visibility = View.GONE
            }
        }
    }
}

class ProgressDiffCallback : DiffUtil.ItemCallback<StudentProgress>() {
    override fun areItemsTheSame(
        oldItem: StudentProgress,
        newItem: StudentProgress
    ): Boolean {
        return oldItem.id == newItem.id
    }
    
    override fun areContentsTheSame(
        oldItem: StudentProgress,
        newItem: StudentProgress
    ): Boolean {
        return oldItem == newItem
    }
}

