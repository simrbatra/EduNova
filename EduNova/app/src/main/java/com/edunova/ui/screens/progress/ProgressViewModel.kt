package com.edunova.ui.screens.progress

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.edunova.data.auth.AuthManager
import com.edunova.domain.model.StudentProgress
import kotlinx.coroutines.launch

class ProgressViewModel(application: Application) : AndroidViewModel(application) {
    
    private val database by lazy { com.edunova.di.DatabaseModule.getDatabase(getApplication()) }
    private val auth by lazy { AuthManager(getApplication()) }
    
    private val _progressList = MutableLiveData<List<StudentProgress>>()
    val progressList: LiveData<List<StudentProgress>> = _progressList
    
    private val _overallProgress = MutableLiveData<Float>()
    val overallProgress: LiveData<Float> = _overallProgress
    
    private val _totalLessonsCompleted = MutableLiveData<Int>()
    val totalLessonsCompleted: LiveData<Int> = _totalLessonsCompleted
    
    private val _totalTimeSpent = MutableLiveData<Long>()
    val totalTimeSpent: LiveData<Long> = _totalTimeSpent
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    init {
        loadProgressData()
    }
    
    private fun loadProgressData() {
        viewModelScope.launch {
            _isLoading.value = true
            val studentId = auth.getCurrentStudentId()
            if (studentId != null) {
                database.progressDao().getProgressByStudent(studentId).asLiveData().observeForever { list ->
                    val domainList = list.map { it.toDomain() }
                    _progressList.value = domainList
                    _overallProgress.value = domainList.map { it.completionPercentage }.average().toFloat()
                    _totalLessonsCompleted.value = domainList.count { it.completionPercentage >= 100f }
                    _totalTimeSpent.value = domainList.sumOf { it.timeSpent * 1000 } // convert sec to ms
                    _isLoading.value = false
                }
            } else {
                _progressList.value = emptyList()
                _overallProgress.value = 0f
                _totalLessonsCompleted.value = 0
                _totalTimeSpent.value = 0
                _isLoading.value = false
            }
        }
    }
    
    fun refreshProgress() {
        loadProgressData()
    }
    
    fun formatTime(millis: Long): String {
        val hours = millis / 3600000
        val minutes = (millis % 3600000) / 60000
        return if (hours > 0) {
            "${hours}h ${minutes}m"
        } else {
            "${minutes}m"
        }
    }
}

