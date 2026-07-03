package com.example.data.repository

import com.example.data.dao.AppDao
import com.example.data.entity.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppRepository(private val appDao: AppDao) {

    val userProfile: Flow<UserProfileEntity?> = appDao.getUserProfile()
    val doctors: Flow<List<DoctorEntity>> = appDao.getAllDoctors()
    val appointments: Flow<List<AppointmentEntity>> = appDao.getAllAppointments()
    val reports: Flow<List<ReportEntity>> = appDao.getAllReports()
    val healthPackages: Flow<List<HealthPackageEntity>> = appDao.getAllHealthPackages()
    val notifications: Flow<List<NotificationEntity>> = appDao.getAllNotifications()
    val walletTransactions: Flow<List<WalletTransactionEntity>> = appDao.getAllTransactions()

    suspend fun insertAppointment(appointment: AppointmentEntity) {
        withContext(Dispatchers.IO) {
            appDao.insertAppointment(appointment)
            // Add notification
            appDao.insertNotification(
                NotificationEntity(
                    title = "Booking Confirmed",
                    message = "Your appointment with ${appointment.doctorName} is confirmed for ${appointment.date} at ${appointment.timeSlot}.",
                    timestamp = System.currentTimeMillis(),
                    type = "Booking"
                )
            )
        }
    }

    suspend fun cancelAppointment(appointmentId: Int) {
        withContext(Dispatchers.IO) {
            appDao.cancelAppointment(appointmentId)
        }
    }

    suspend fun insertReport(report: ReportEntity) {
        withContext(Dispatchers.IO) {
            appDao.insertReport(report)
        }
    }

    suspend fun addMoneyToWallet(amount: Double) {
        withContext(Dispatchers.IO) {
            val transaction = WalletTransactionEntity(
                amount = amount,
                type = "Credit",
                description = "Added to Health Wallet",
                timestamp = System.currentTimeMillis()
            )
            appDao.insertTransaction(transaction)
            
            appDao.insertNotification(
                NotificationEntity(
                    title = "Wallet Credited",
                    message = "₹${String.format("%.2f", amount)} has been credited to your Health Wallet.",
                    timestamp = System.currentTimeMillis(),
                    type = "Wallet"
                )
            )
        }
    }

    suspend fun payWithWallet(amount: Double, description: String): Boolean {
        return withContext(Dispatchers.IO) {
            // Check current balance
            val txs = appDao.getAllTransactions().firstOrNull() ?: emptyList()
            val balance = txs.fold(0.0) { acc, tx ->
                if (tx.type == "Credit") acc + tx.amount else acc - tx.amount
            }
            if (balance >= amount) {
                val transaction = WalletTransactionEntity(
                    amount = amount,
                    type = "Debit",
                    description = description,
                    timestamp = System.currentTimeMillis()
                )
                appDao.insertTransaction(transaction)
                
                appDao.insertNotification(
                    NotificationEntity(
                        title = "Wallet Debited",
                        message = "₹${String.format("%.2f", amount)} was paid from your wallet for $description.",
                        timestamp = System.currentTimeMillis(),
                        type = "Wallet"
                    )
                )
                true
            } else {
                false
            }
        }
    }

    suspend fun markAllNotificationsAsRead() {
        withContext(Dispatchers.IO) {
            appDao.markAllNotificationsAsRead()
        }
    }

    suspend fun updateUserProfile(profile: com.example.data.entity.UserProfileEntity) {
        withContext(Dispatchers.IO) {
            appDao.insertUserProfile(profile)
        }
    }

    suspend fun prepopulateIfEmpty() {
        withContext(Dispatchers.IO) {
            // 1. Prepopulate User Profile
            val profile = appDao.getUserProfile().firstOrNull()
            if (profile == null) {
                appDao.insertUserProfile(
                    UserProfileEntity(
                        id = 1,
                        name = "Rajesh Kumar",
                        email = "rajesh.kumar@healthquest.com",
                        phone = "+91 98765 43210",
                        age = 32,
                        gender = "Male",
                        weight = "72 kg",
                        height = "176 cm"
                    )
                )
            }

            // 2. Prepopulate Doctors
            val docs = appDao.getAllDoctors().firstOrNull() ?: emptyList()
            if (docs.isEmpty()) {
                appDao.insertDoctors(
                    listOf(
                        DoctorEntity("doc_1", "Dr. S. K. Gupta", "Cardiologist", 4.8, 12, 500.0, "Cardiology"),
                        DoctorEntity("doc_2", "Dr. Ananya Sen", "Gynecologist", 4.9, 10, 600.0, "Gynecology"),
                        DoctorEntity("doc_3", "Dr. Amit Sharma", "Pediatrician", 4.7, 8, 450.0, "Pediatrics"),
                        DoctorEntity("doc_4", "Dr. Kavita Reddy", "Dermatologist", 4.6, 7, 500.0, "Dermatology"),
                        DoctorEntity("doc_5", "Dr. Rajesh Mehta", "General Physician", 4.5, 15, 400.0, "General Medicine")
                    )
                )
            }

            // Prepopulate Appointments to match mockups in the image exactly
            val apptsList = appDao.getAllAppointments().firstOrNull() ?: emptyList()
            if (apptsList.isEmpty()) {
                appDao.insertAppointment(
                    AppointmentEntity(
                        doctorId = "doc_ct_scan",
                        doctorName = "CT Scan – Chest",
                        specialty = "Radiology",
                        date = "18 May",
                        timeSlot = "10:30 AM",
                        patientName = "Rajesh Kumar",
                        status = "Confirmed",
                        notes = "Sector 51"
                    )
                )
                appDao.insertAppointment(
                    AppointmentEntity(
                        doctorId = "doc_ultrasound",
                        doctorName = "Ultrasound – Abdomen",
                        specialty = "Radiology",
                        date = "22 May",
                        timeSlot = "9:00 AM",
                        patientName = "Rajesh Kumar",
                        status = "Pending",
                        notes = "Sector 69"
                    )
                )
            }

            // 3. Prepopulate Health Packages
            val pcks = appDao.getAllHealthPackages().firstOrNull() ?: emptyList()
            if (pcks.isEmpty() || pcks.any { it.testListString.contains("Kidney Function Test") }) {
                appDao.insertHealthPackages(
                    listOf(
                        HealthPackageEntity(
                            id = "pkg_1",
                            name = "Full Body Checkup",
                            description = "Detailed assessment of key organs & blood parameters.",
                            price = 2499.0,
                            originalPrice = 3299.0,
                            testsCount = 4,
                            testListString = "X-Ray Chest, Ultrasound Abdomen, Blood CBC, Thyroid Panel"
                        ),
                        HealthPackageEntity(
                            id = "pkg_2",
                            name = "Heart Health Package",
                            description = "Comprehensive screening for cardiovascular health & cholesterol.",
                            price = 1899.0,
                            originalPrice = 2400.0,
                            testsCount = 3,
                            testListString = "ECG, Echocardiogram, Lipid Profile"
                        ),
                        HealthPackageEntity(
                            id = "pkg_3",
                            name = "Women's Health Checkup",
                            description = "Specialized diagnostic screening for women's health needs.",
                            price = 1699.0,
                            originalPrice = 2100.0,
                            testsCount = 3,
                            testListString = "Mammography, Pelvic USG, Bone Density"
                        )
                    )
                )
            }

            // 4. Prepopulate Reports
            val reps = appDao.getAllReports().firstOrNull() ?: emptyList()
            if (reps.isEmpty()) {
                appDao.insertReports(
                    listOf(
                        ReportEntity(
                            title = "CT Scan – Chest",
                            date = "1 May 2026",
                            patientName = "Rajesh Kumar",
                            status = "Available",
                            resultSummary = "Completed",
                            doctorName = "Sector 51"
                        ),
                        ReportEntity(
                            title = "Ultrasound – Abdomen",
                            date = "22 May 2026",
                            patientName = "Rajesh Kumar",
                            status = "Pending",
                            resultSummary = "Test",
                            doctorName = "Sector 69"
                        ),
                        ReportEntity(
                            title = "X-Ray – Left Knee",
                            date = "14 Apr 2026",
                            patientName = "Rajesh Kumar",
                            status = "Available",
                            resultSummary = "Completed",
                            doctorName = "Manesar"
                        )
                    )
                )
            }

            // 5. Prepopulate Wallet and Transactions
            // We want the total balance to be exactly ₹1,840.50 as shown in Figma
            // Initial credit = 2000.0, debit = 159.50 -> balance = 1840.50
            val txs = appDao.getAllTransactions().firstOrNull() ?: emptyList()
            if (txs.isEmpty()) {
                appDao.insertTransaction(
                    WalletTransactionEntity(
                        amount = 2000.0,
                        type = "Credit",
                        description = "Initial Wallet Setup",
                        timestamp = System.currentTimeMillis() - 86400000 * 2
                    )
                )
                appDao.insertTransaction(
                    WalletTransactionEntity(
                        amount = 159.50,
                        type = "Debit",
                        description = "Service Booking Convenience Fee",
                        timestamp = System.currentTimeMillis() - 86400000
                    )
                )
            }

            // 6. Prepopulate Notifications
            val notifs = appDao.getAllNotifications().firstOrNull() ?: emptyList()
            if (notifs.isEmpty()) {
                appDao.insertNotification(
                    NotificationEntity(
                        title = "Report Ready 🎉",
                        message = "Your CT Scan – Chest report is ready. Tap to view & download.",
                        timestamp = System.currentTimeMillis() - 120000, // 2 mins ago
                        isRead = false,
                        type = "Report"
                    )
                )
                appDao.insertNotification(
                    NotificationEntity(
                        title = "Appointment Reminder",
                        message = "Ultrasound – Abdomen tomorrow at 9:00 AM, Sector 69 branch.",
                        timestamp = System.currentTimeMillis() - 3600000 * 2, // 2 hours ago
                        isRead = false,
                        type = "Booking"
                    )
                )
                appDao.insertNotification(
                    NotificationEntity(
                        title = "Special Offer 🏷️",
                        message = "Full Body Checkup at ₹2,499 only! Save ₹800 — valid till 31 May.",
                        timestamp = System.currentTimeMillis() - 3600000 * 14, // 14 hours ago
                        isRead = true,
                        type = "System"
                    )
                )
                appDao.insertNotification(
                    NotificationEntity(
                        title = "Booking Confirmed ✔️",
                        message = "Your CT Scan appointment on 1 May at Sector 51 is confirmed.",
                        timestamp = System.currentTimeMillis() - 86400000 * 5, // 5 days ago
                        isRead = true,
                        type = "Booking"
                    )
                )
            }
        }
    }
}
