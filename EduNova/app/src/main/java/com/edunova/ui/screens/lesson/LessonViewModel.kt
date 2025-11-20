package com.edunova.ui.screens.lesson

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.edunova.data.repository.LessonRepository
import com.edunova.domain.model.Lesson
import kotlinx.coroutines.launch

class LessonViewModel(application: Application) : AndroidViewModel(application) {
    
    private val database by lazy { com.edunova.di.DatabaseModule.getDatabase(getApplication()) }
    private val lessonRepository by lazy { LessonRepository(database.lessonDao()) }
    
    val lessons: LiveData<List<Lesson>> = lessonRepository.getAllLessons().asLiveData()
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _filteredSubject = MutableLiveData<String?>()
    val filteredSubject: LiveData<String?> = _filteredSubject
    
    init {
        preloadIfEmpty()
    }
    
    private fun preloadIfEmpty() {
        viewModelScope.launch {
            _isLoading.value = true
            // Seed basic lessons if database is empty
            val existing = lessonRepository.getAllLessons().asLiveData().value
            if (existing.isNullOrEmpty()) {
                lessonRepository.insertLessons(
                    listOf(
                        Lesson(
                            id = "1",
                            title = "Introduction to Mathematics",
                            description = "Learn basic math concepts including addition, subtraction, and multiplication",
                            subject = "Math",
                            grade = "Grade 5",
                            difficulty = "Beginner",
                            content = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                            estimatedDuration = 30,
                            language = "en"
                        ),
                        Lesson(
                            id = "2",
                            title = "Science Fundamentals",
                            description = "Explore the world of science with fun experiments",
                            subject = "Science",
                            grade = "Grade 5",
                            difficulty = "Beginner",
                            content = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
                            estimatedDuration = 45,
                            language = "en"
                        ),
                        Lesson(
                            id = "3",
                            title = "English Grammar Basics",
                            description = "Master English grammar rules and sentence structure",
                            subject = "English",
                            grade = "Grade 5",
                            difficulty = "Intermediate",
                            content = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
                            estimatedDuration = 40,
                            language = "en"
                        )
                    )
                )
            }
            _isLoading.value = false
        }
    }
    
    fun filterBySubject(subject: String?) {
        _filteredSubject.value = subject
    }
    
    fun clearFilter() {
        _filteredSubject.value = null
    }
    
    fun refreshLessons() {
        preloadIfEmpty()
    }
}

