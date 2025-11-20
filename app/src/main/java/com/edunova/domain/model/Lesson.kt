package com.edunova.domain.model

data class Lesson(
    val id: String,
    val title: String,
    val description: String,
    val subject: String,
    val grade: String,
    val difficulty: String,
    val content: String,
    val estimatedDuration: Int,
    val language: String,
    val isDownloaded: Boolean = false
)

