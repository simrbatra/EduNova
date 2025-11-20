package com.edunova.ui.screens.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.edunova.data.auth.AuthManager
import com.edunova.data.local.EduNovaDatabase
import com.edunova.data.repository.StudentRepository
import com.edunova.domain.model.Student
import kotlinx.coroutines.launch
import java.util.UUID

class AuthViewModel(application: Application) : AndroidViewModel(application) {

	private val authManager = AuthManager(application)
	private val studentRepository: StudentRepository by lazy {
		val db = EduNovaDatabase::class.java
		val database = com.edunova.di.DatabaseModule.getDatabase(getApplication())
		StudentRepository(database.studentDao())
	}

	private val _isLoading = MutableLiveData<Boolean>()
	val isLoading: LiveData<Boolean> = _isLoading

	private val _errorMessage = MutableLiveData<String?>()
	val errorMessage: LiveData<String?> = _errorMessage

	fun isLoggedIn(): Boolean = authManager.isLoggedIn()

	fun login(name: String, grade: String, onSuccess: () -> Unit) {
		viewModelScope.launch {
			_isLoading.value = true
			try {
				val studentId = authManager.getCurrentStudentId() ?: UUID.randomUUID().toString()
				// ensure student exists locally
				studentRepository.insertStudent(
					Student(
						id = studentId,
						name = name.ifBlank { "Student" },
						age = 10,
						grade = grade.ifBlank { "Grade 5" },
						preferredLanguage = "English",
						avatarUrl = null
					)
				)
				authManager.setCurrentStudentId(studentId)
				onSuccess()
			} catch (e: Exception) {
				_errorMessage.value = e.message
			} finally {
				_isLoading.value = false
			}
		}
	}

	fun register(name: String, age: Int, grade: String, onSuccess: () -> Unit) {
		viewModelScope.launch {
			_isLoading.value = true
			try {
				val studentId = UUID.randomUUID().toString()
				studentRepository.insertStudent(
					Student(
						id = studentId,
						name = name.ifBlank { "Student" },
						age = age,
						grade = grade.ifBlank { "Grade 5" },
						preferredLanguage = "English",
						avatarUrl = null
					)
				)
				authManager.setCurrentStudentId(studentId)
				onSuccess()
			} catch (e: Exception) {
				_errorMessage.value = e.message
			} finally {
				_isLoading.value = false
			}
		}
	}
}


