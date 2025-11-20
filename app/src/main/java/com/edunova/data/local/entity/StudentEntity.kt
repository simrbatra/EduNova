package com.edunova.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.edunova.domain.model.Student

@Entity(tableName = "students")
data class StudentEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val age: Int,
    val grade: String,
    val preferredLanguage: String,
    val avatarUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    fun toDomain(): Student {
        return Student(
            id = id,
            name = name,
            age = age,
            grade = grade,
            preferredLanguage = preferredLanguage,
            avatarUrl = avatarUrl
        )
    }
}

fun Student.toEntity(): StudentEntity {
    return StudentEntity(
        id = id,
        name = name,
        age = age,
        grade = grade,
        preferredLanguage = preferredLanguage,
        avatarUrl = avatarUrl
    )
}

