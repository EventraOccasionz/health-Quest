package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.entity.*
import com.example.data.repository.AppRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HealthViewModel(private val repository: AppRepository) : ViewModel() {

    // Logged in state
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    // Database Flows
    val userProfile: StateFlow<UserProfileEntity?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val doctors: StateFlow<List<DoctorEntity>> = repository.doctors
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val appointments: StateFlow<List<AppointmentEntity>> = repository.appointments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val reports: StateFlow<List<ReportEntity>> = repository.reports
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val healthPackages: StateFlow<List<HealthPackageEntity>> = repository.healthPackages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notifications: StateFlow<List<NotificationEntity>> = repository.notifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val transactions: StateFlow<List<WalletTransactionEntity>> = repository.walletTransactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // UI Interactive States
    val walletBalance: StateFlow<Double> = repository.walletTransactions
        .map { list ->
            list.fold(0.0) { acc, tx ->
                if (tx.type == "Credit") acc + tx.amount else acc - tx.amount
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedDepartment = MutableStateFlow("All")
    val selectedDepartment: StateFlow<String> = _selectedDepartment.asStateFlow()

    private val _selectedDoctor = MutableStateFlow<DoctorEntity?>(null)
    val selectedDoctor: StateFlow<DoctorEntity?> = _selectedDoctor.asStateFlow()

    private val _selectedPackage = MutableStateFlow<HealthPackageEntity?>(null)
    val selectedPackage: StateFlow<HealthPackageEntity?> = _selectedPackage.asStateFlow()

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedDepartment(dept: String) {
        _selectedDepartment.value = dept
    }

    fun setSelectedDoctor(doctor: DoctorEntity?) {
        _selectedDoctor.value = doctor
    }

    fun setSelectedPackage(pkg: HealthPackageEntity?) {
        _selectedPackage.value = pkg
    }

    fun login(phoneOrEmail: String) {
        viewModelScope.launch {
            _isLoggedIn.value = true
            // If user profile is not configured, insert default
            repository.prepopulateIfEmpty()
        }
    }

    fun updateUserProfile(profile: com.example.data.entity.UserProfileEntity) {
        viewModelScope.launch {
            repository.updateUserProfile(profile)
        }
    }

    fun logout() {
        _isLoggedIn.value = false
    }

    fun bookAppointment(doctor: DoctorEntity, date: String, time: String, notes: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val profile = repository.userProfile.firstOrNull()
            val patientName = profile?.name ?: "Rajesh Kumar"
            
            // Deduct doctor price from wallet if possible, or book with mock cash
            val booked = AppointmentEntity(
                doctorId = doctor.id,
                doctorName = doctor.name,
                specialty = doctor.specialty,
                date = date,
                timeSlot = time,
                patientName = patientName,
                status = "Upcoming",
                notes = notes
            )
            repository.insertAppointment(booked)
            onSuccess()
        }
    }

    fun cancelAppointment(appointmentId: Int) {
        viewModelScope.launch {
            repository.cancelAppointment(appointmentId)
        }
    }

    fun addMoney(amount: Double) {
        viewModelScope.launch {
            repository.addMoneyToWallet(amount)
        }
    }

    fun buyHealthPackage(pkg: HealthPackageEntity, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            val success = repository.payWithWallet(
                pkg.price, 
                "Purchased ${pkg.name}"
            )
            if (success) {
                // Add a new mock report for this health package
                repository.insertReport(
                    ReportEntity(
                        title = "${pkg.name} Report",
                        date = "Pending",
                        patientName = "Rajesh Kumar",
                        status = "Pending",
                        resultSummary = "Awaiting lab sample collection. We have assigned a technician to contact you.",
                        doctorName = "Health Quest Labs"
                    )
                )
                onSuccess()
            } else {
                onFailure("Insufficient wallet balance. Please add money first!")
            }
        }
    }

    fun payOrTransfer(amount: Double, description: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            val success = repository.payWithWallet(amount, description)
            if (success) {
                onSuccess()
            } else {
                onFailure("Insufficient wallet balance. Please add money first!")
            }
        }
    }

    fun markNotificationsRead() {
        viewModelScope.launch {
            repository.markAllNotificationsAsRead()
        }
    }

    class Factory(private val repository: AppRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HealthViewModel::class.java)) {
                return HealthViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
