package com.edunova.ui.screens.profile

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

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    
    private val _student = MutableLiveData<Student?>()
    val student: LiveData<Student?> = _student
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val authManager = AuthManager(application)
    private val studentRepository: StudentRepository by lazy {
        val database = com.edunova.di.DatabaseModule.getDatabase(getApplication())
        StudentRepository(database.studentDao())
    }
    
    init {
        loadStudentProfile()
    }
    
    private fun loadStudentProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            val studentId = authManager.getCurrentStudentId()
            _student.value = if (studentId != null) {
                studentRepository.getStudentById(studentId)
            } else {
                null
            }
            _isLoading.value = false
        }
    }
    
    fun updateStudentProfile(student: Student) {
        viewModelScope.launch {
            studentRepository.updateStudent(student)
            _student.value = student
        }
    }
    
    fun refreshProfile() {
        loadStudentProfile()
    }
}

