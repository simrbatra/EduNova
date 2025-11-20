package com.edunova.ui.screens.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.edunova.databinding.FragmentProfileBinding
import kotlinx.coroutines.launch
import com.edunova.data.auth.AuthManager
import androidx.navigation.NavOptions

class ProfileFragment : Fragment() {
    
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: ProfileViewModel by viewModels()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupObservers()
        setupClickListeners()
    }
    
    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.student.observe(viewLifecycleOwner) { student ->
                student?.let {
                    binding.textStudentName.text = it.name
                    binding.textStudentAge.text = "Age: ${it.age}"
                    binding.textStudentGrade.text = it.grade
                    binding.textPreferredLanguage.text = "Language: ${it.preferredLanguage}"
                }
            }
            
            viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }
    }
    
    private fun setupClickListeners() {
        binding.btnEditProfile.setOnClickListener {
            val context = requireContext()
            val current = viewModel.student.value
            val dialogView = LayoutInflater.from(context).inflate(com.edunova.R.layout.dialog_edit_profile, null)
            val nameInput = dialogView.findViewById<EditText>(com.edunova.R.id.input_name)
            val gradeInput = dialogView.findViewById<EditText>(com.edunova.R.id.input_grade)
            nameInput.setText(current?.name ?: "")
            gradeInput.setText(current?.grade ?: "")
            AlertDialog.Builder(context)
                .setTitle("Edit Profile")
                .setView(dialogView)
                .setPositiveButton("Save") { _, _ ->
                    current?.let {
                        val updated = it.copy(
                            name = nameInput.text?.toString().orEmpty().ifBlank { it.name },
                            grade = gradeInput.text?.toString().orEmpty().ifBlank { it.grade }
                        )
                        viewModel.updateStudentProfile(updated)
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
        
        binding.btnSettings.setOnClickListener {
            findNavController().navigate(com.edunova.R.id.nav_settings)
        }
        
        binding.btnAbout.setOnClickListener {
            findNavController().navigate(com.edunova.R.id.nav_about)
        }

        binding.btnLogout.setOnClickListener {
            val auth = AuthManager(requireContext())
            auth.logout()
            val navOptions = NavOptions.Builder()
                .setPopUpTo(findNavController().graph.id, true)
                .build()
            findNavController().navigate(com.edunova.R.id.nav_login, null, navOptions)
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

