package com.edunova.ui.screens.progress

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.edunova.data.api.EducationApiService
import com.edunova.data.auth.AuthManager
import com.edunova.domain.model.StudentProgress
import kotlinx.coroutines.launch

class ProgressViewModel(application: Application) : AndroidViewModel(application) {
    
    private val database by lazy { com.edunova.di.DatabaseModule.getDatabase(getApplication()) }
    private val auth by lazy { AuthManager(getApplication()) }
    private val apiService = EducationApiService()
    
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
            val studentId = auth.getCurrentStudentId() ?: "default_student"
            
            try {
                // Try to get progress from API first
                val apiProgress = apiService.getProgress(studentId, null)
                
                // Update from API if available
                if (apiProgress.isNotEmpty()) {
                    _overallProgress.value = apiProgress["overallProgress"] as? Float ?: 0f
                    _totalLessonsCompleted.value = apiProgress["totalLessonsCompleted"] as? Int ?: 0
                    _totalTimeSpent.value = (apiProgress["totalTimeSpent"] as? Long ?: 0L) * 1000 // convert to ms
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            
            // Also load from local database
            database.progressDao().getProgressByStudent(studentId).asLiveData().observeForever { list ->
                val domainList = list.map { it.toDomain() }
                _progressList.value = domainList
                
                // Update overall stats from local data if API didn't provide
                if (_overallProgress.value == null || _overallProgress.value == 0f) {
                    _overallProgress.value = if (domainList.isNotEmpty()) {
                        domainList.map { it.completionPercentage }.average().toFloat()
                    } else {
                        0f
                    }
                }
                
                if (_totalLessonsCompleted.value == null || _totalLessonsCompleted.value == 0) {
                    _totalLessonsCompleted.value = domainList.count { it.completionPercentage >= 100f }
                }
                
                if (_totalTimeSpent.value == null || _totalTimeSpent.value == 0L) {
                    _totalTimeSpent.value = domainList.sumOf { it.timeSpent * 1000 } // convert sec to ms
                }
                
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Save progress to API and local database
     */
    fun saveProgress(lessonId: String, completionPercentage: Float, timeSpent: Long) {
        viewModelScope.launch {
            val studentId = auth.getCurrentStudentId() ?: "default_student"
            
            try {
                // Get existing progress or create new
                val existingProgress = database.progressDao().getProgressByStudentAndLesson(studentId, lessonId)
                val progressId = existingProgress?.id ?: "${studentId}_${lessonId}_${System.currentTimeMillis()}"
                
                val masteryLevel = when {
                    completionPercentage >= 90f -> "MASTERED"
                    completionPercentage >= 70f -> "ADVANCED"
                    completionPercentage >= 40f -> "INTERMEDIATE"
                    else -> "BEGINNER"
                }
                
                val progressEntity = com.edunova.data.local.entity.ProgressEntity(
                    id = progressId,
                    studentId = studentId,
                    lessonId = lessonId,
                    completionPercentage = completionPercentage,
                    timeSpent = timeSpent,
                    attempts = (existingProgress?.attempts ?: 0) + 1,
                    lastAttemptDate = System.currentTimeMillis(),
                    masteryLevel = masteryLevel,
                    weakAreas = null,
                    strongAreas = null
                )
                
                // Save to local database
                database.progressDao().insertProgress(progressEntity)
                
                // Save to API
                apiService.saveProgress(studentId, lessonId, completionPercentage, timeSpent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            
            // Refresh local data
            loadProgressData()
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

