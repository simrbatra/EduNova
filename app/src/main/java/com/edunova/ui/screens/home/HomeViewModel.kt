package com.edunova.ui.screens.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edunova.domain.model.Lesson
import com.edunova.domain.model.Student
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    
    private val _student = MutableLiveData<Student?>()
    val student: LiveData<Student?> = _student
    
    private val _recommendedLessons = MutableLiveData<List<Lesson>>()
    val recommendedLessons: LiveData<List<Lesson>> = _recommendedLessons
    
    private val _level = MutableLiveData<Int>()
    val level: LiveData<Int> = _level
    
    private val _xp = MutableLiveData<Int>()
    val xp: LiveData<Int> = _xp
    
    private val _streak = MutableLiveData<Int>()
    val streak: LiveData<Int> = _streak
    
    init {
        loadHomeData()
    }
    
    private fun loadHomeData() {
        viewModelScope.launch {
            // TODO: Load from repository
            _level.value = 5
            _xp.value = 1250
            _streak.value = 7
            
            // Sample recommended lessons
            _recommendedLessons.value = listOf(
                Lesson(
                    id = "1",
                    title = "Introduction to Mathematics",
                    description = "Learn basic math concepts",
                    subject = "Math",
                    grade = "Grade 5",
                    difficulty = "Beginner",
                    content = "",
                    estimatedDuration = 30,
                    language = "en"
                ),
                Lesson(
                    id = "2",
                    title = "Science Fundamentals",
                    description = "Explore the world of science",
                    subject = "Science",
                    grade = "Grade 5",
                    difficulty = "Beginner",
                    content = "",
                    estimatedDuration = 45,
                    language = "en"
                ),
                Lesson(
                    id = "3",
                    title = "English Grammar",
                    description = "Master English grammar",
                    subject = "English",
                    grade = "Grade 5",
                    difficulty = "Intermediate",
                    content = "",
                    estimatedDuration = 40,
                    language = "en"
                )
            )
        }
    }
    
    fun refreshData() {
        loadHomeData()
    }
}

