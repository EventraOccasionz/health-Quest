package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val name: String,
    val email: String,
    val phone: String,
    val age: Int,
    val gender: String,
    val weight: String,
    val height: String
)

@Entity(tableName = "doctors")
data class DoctorEntity(
    @PrimaryKey val id: String,
    val name: String,
    val specialty: String,
    val rating: Double,
    val experience: Int,
    val price: Double,
    val department: String,
    val imageUrl: String = ""
)

@Entity(tableName = "appointments")
data class AppointmentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val doctorId: String,
    val doctorName: String,
    val specialty: String,
    val date: String,
    val timeSlot: String,
    val patientName: String,
    val status: String, // "Upcoming", "Completed", "Cancelled"
    val notes: String = ""
)

@Entity(tableName = "reports")
data class ReportEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val date: String,
    val patientName: String,
    val status: String, // "Available", "Pending"
    val resultSummary: String,
    val doctorName: String
)

@Entity(tableName = "health_packages")
data class HealthPackageEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val originalPrice: Double,
    val testsCount: Int,
    val testListString: String // Comma separated tests
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val message: String,
    val timestamp: Long,
    val isRead: Boolean = false,
    val type: String // "Booking", "Report", "Wallet", "System"
)

@Entity(tableName = "wallet_transactions")
data class WalletTransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amount: Double,
    val type: String, // "Credit", "Debit"
    val description: String,
    val timestamp: Long
)
