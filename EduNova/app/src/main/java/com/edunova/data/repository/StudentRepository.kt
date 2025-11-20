package com.edunova.data.repository

import com.edunova.data.local.dao.StudentDao
import com.edunova.data.local.entity.StudentEntity
import com.edunova.domain.model.Student
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StudentRepository(
    private val studentDao: StudentDao
) {
    fun getAllStudents(): Flow<List<Student>> {
        return studentDao.getAllStudents().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    suspend fun getStudentById(id: String): Student? {
        return studentDao.getStudentById(id)?.toDomain()
    }
    
    suspend fun insertStudent(student: Student) {
        studentDao.insertStudent(
            StudentEntity(
                id = student.id,
                name = student.name,
                age = student.age,
                grade = student.grade,
                preferredLanguage = student.preferredLanguage,
                avatarUrl = student.avatarUrl
            )
        )
    }
    
    suspend fun updateStudent(student: Student) {
        studentDao.updateStudent(
            StudentEntity(
                id = student.id,
                name = student.name,
                age = student.age,
                grade = student.grade,
                preferredLanguage = student.preferredLanguage,
                avatarUrl = student.avatarUrl
            )
        )
    }
    
    suspend fun deleteStudent(student: Student) {
        studentDao.deleteStudent(
            StudentEntity(
                id = student.id,
                name = student.name,
                age = student.age,
                grade = student.grade,
                preferredLanguage = student.preferredLanguage,
                avatarUrl = student.avatarUrl
            )
        )
    }
}

