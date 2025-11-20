package com.edunova.ui.screens.lesson

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.edunova.data.api.EducationApiService
import com.edunova.data.repository.LessonRepository
import com.edunova.data.settings.SettingsManager
import com.edunova.domain.model.Lesson
import kotlinx.coroutines.launch

class LessonViewModel(application: Application) : AndroidViewModel(application) {
    
    private val database by lazy { com.edunova.di.DatabaseModule.getDatabase(getApplication()) }
    private val lessonRepository by lazy { LessonRepository(database.lessonDao()) }
    private val apiService by lazy { EducationApiService() }
    private val settingsManager = SettingsManager(application)
    
    private val _lessons = MutableLiveData<List<Lesson>>()
    val lessons: LiveData<List<Lesson>> = _lessons
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _filteredSubject = MutableLiveData<String?>()
    val filteredSubject: LiveData<String?> = _filteredSubject
    
    private var currentGrade = "Grade 5" // Default grade, can be updated from student profile
    private val studentRepository by lazy { 
        val database = com.edunova.di.DatabaseModule.getDatabase(getApplication())
        com.edunova.data.repository.StudentRepository(database.studentDao())
    }
    private val authManager by lazy { com.edunova.data.auth.AuthManager(getApplication()) }
    
    init {
        // Load grade from student profile
        viewModelScope.launch {
            val studentId = authManager.getCurrentStudentId()
            if (studentId != null) {
                val student = studentRepository.getStudentById(studentId)
                student?.grade?.let { 
                    currentGrade = it
                }
            }
            loadLessonsFromApi()
        }
    }
    
    /**
     * Load lessons from API based on grade and language
     */
    private fun loadLessonsFromApi() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val language = settingsManager.getLanguageCode()
                val apiLessons = apiService.fetchLessonsByGradeAndSubject(
                    grade = currentGrade,
                    subject = null,
                    language = language
                )
                
                if (apiLessons.isNotEmpty()) {
                    // Save to local database
                    lessonRepository.insertLessons(apiLessons)
                    _lessons.value = apiLessons
                } else {
                    // Fallback to local database if API fails
                    val localLessons = lessonRepository.getAllLessons().asLiveData().value
                    if (localLessons.isNullOrEmpty()) {
                        preloadDefaultLessons()
                    } else {
                        _lessons.value = localLessons
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Fallback to local database
                val localLessons = lessonRepository.getAllLessons().asLiveData().value
                _lessons.value = localLessons ?: emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Load lessons for a specific subject
     */
    fun loadLessonsForSubject(subject: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val language = settingsManager.getLanguageCode()
                val apiLessons = apiService.fetchLessonsByGradeAndSubject(
                    grade = currentGrade,
                    subject = subject,
                    language = language
                )
                
                if (apiLessons.isNotEmpty()) {
                    lessonRepository.insertLessons(apiLessons)
                    _lessons.value = apiLessons
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Get relevant video URL for a lesson using API
     */
    suspend fun getRelevantVideoUrl(lesson: Lesson): String {
        return apiService.getRelevantVideoUrl(lesson.title, lesson.description, lesson.subject, lesson.grade)
    }
    
    /**
     * Update grade and reload lessons
     */
    fun setGrade(grade: String) {
        if (currentGrade != grade) {
            currentGrade = grade
            loadLessonsFromApi()
        }
    }
    
    private fun preloadDefaultLessons() {
        viewModelScope.launch {
            val defaultLessons = listOf(
                Lesson(
                    id = "1",
                    title = "Introduction to Mathematics",
                    description = "Learn basic math concepts including addition, subtraction, and multiplication",
                    subject = "Math",
                    grade = currentGrade,
                    difficulty = "Beginner",
                    content = "https://www.youtube.com/embed/dQw4w9WgXcQ",
                    estimatedDuration = 30,
                    language = "en"
                ),
                Lesson(
                    id = "2",
                    title = "Science Fundamentals",
                    description = "Explore the world of science with fun experiments",
                    subject = "Science",
                    grade = currentGrade,
                    difficulty = "Beginner",
                    content = "https://www.youtube.com/embed/dQw4w9WgXcQ",
                    estimatedDuration = 45,
                    language = "en"
                ),
                Lesson(
                    id = "3",
                    title = "English Grammar Basics",
                    description = "Master English grammar rules and sentence structure",
                    subject = "English",
                    grade = currentGrade,
                    difficulty = "Intermediate",
                    content = "https://www.youtube.com/embed/dQw4w9WgXcQ",
                    estimatedDuration = 40,
                    language = "en"
                )
            )
            lessonRepository.insertLessons(defaultLessons)
            _lessons.value = defaultLessons
        }
    }
    
    fun filterBySubject(subject: String?) {
        _filteredSubject.value = subject
        if (subject != null) {
            loadLessonsForSubject(subject)
        } else {
            loadLessonsFromApi()
        }
    }
    
    fun clearFilter() {
        _filteredSubject.value = null
        loadLessonsFromApi()
    }
    
    fun refreshLessons() {
        loadLessonsFromApi()
    }
}

