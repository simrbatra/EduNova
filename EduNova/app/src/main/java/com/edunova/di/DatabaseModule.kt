package com.edunova.di

import android.content.Context
import androidx.room.Room
import com.edunova.data.local.EduNovaDatabase

object DatabaseModule {
    
    @Volatile
    private var INSTANCE: EduNovaDatabase? = null
    
    fun getDatabase(context: Context): EduNovaDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                EduNovaDatabase::class.java,
                "edunova_database"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}

