package com.edunova.domain.model

data class Student(
    val id: String,
    val name: String,
    val age: Int,
    val grade: String,
    val preferredLanguage: String,
    val avatarUrl: String? = null,

)

