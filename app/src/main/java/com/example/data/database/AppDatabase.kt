package com.example.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.dao.AppDao
import com.example.data.entity.*

@Database(
    entities = [
        UserProfileEntity::class,
        DoctorEntity::class,
        AppointmentEntity::class,
        ReportEntity::class,
        HealthPackageEntity::class,
        NotificationEntity::class,
        WalletTransactionEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}
