package com.edunova.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.edunova.data.local.dao.*
import com.edunova.data.local.entity.*

@Database(
    entities = [
        StudentEntity::class,
        LessonEntity::class,
        ProgressEntity::class,
        AssessmentEntity::class,
        GamificationEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class EduNovaDatabase : RoomDatabase() {
    abstract fun studentDao(): StudentDao
    abstract fun lessonDao(): LessonDao
    abstract fun progressDao(): ProgressDao
    abstract fun assessmentDao(): AssessmentDao
    abstract fun gamificationDao(): GamificationDao
}

