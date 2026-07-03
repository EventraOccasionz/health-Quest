package com.example

import android.app.Application
import androidx.room.Room
import com.example.data.database.AppDatabase
import com.example.data.repository.AppRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class HealthQuestApplication : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    lateinit var database: AppDatabase
    lateinit var repository: AppRepository

    override fun onCreate() {
        super.onCreate()
        
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "health_quest_database"
        )
        .fallbackToDestructiveMigration()
        .build()

        repository = AppRepository(database.appDao())

        // Run prepopulation in the background safely
        applicationScope.launch {
            repository.prepopulateIfEmpty()
        }
    }
}
