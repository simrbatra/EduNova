package com.edunova.ui.screens.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.edunova.R
import com.edunova.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

	private var _binding: FragmentRegisterBinding? = null
	private val binding get() = _binding!!

	private val authViewModel: AuthViewModel by viewModels()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentRegisterBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.btnRegister.setOnClickListener {
			val name = view.findViewById<EditText>(R.id.input_name).text?.toString().orEmpty()
			val ageStr = view.findViewById<EditText>(R.id.input_age).text?.toString().orEmpty()
			val grade = view.findViewById<EditText>(R.id.input_grade).text?.toString().orEmpty()
			val age = ageStr.toIntOrNull() ?: 10
			authViewModel.register(name, age, grade) {
				findNavController().navigate(R.id.nav_home)
			}
		}

		binding.btnGoToLogin.setOnClickListener {
			findNavController().navigate(R.id.nav_login)
		}

		authViewModel.errorMessage.observe(viewLifecycleOwner) { msg ->
			msg?.let { Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show() }
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}


