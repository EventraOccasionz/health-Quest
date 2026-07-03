package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.entity.*
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.HealthViewModel
import kotlinx.coroutines.launch
import androidx.compose.ui.draw.drawBehind
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner

data class MocService(
    val id: String,
    val name: String,
    val price: Double,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val iconBg: Color,
    val iconTint: Color
)

data class MocLocation(
    val id: String,
    val name: String,
    val address: String,
    val distance: String
)

data class MocDate(
    val dayOfWeek: String,
    val dayOfMonth: String,
    val fullDateString: String
)

@Composable
fun DetailRow(
    label: String,
    value: String,
    isBold: Boolean = false,
    valueColor: Color = Color(0xFF0C233A)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = Color(0xFF8A90A6),
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.SemiBold,
            color = valueColor
        )
    }
}

// 1. Book Appointment Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookAppointmentScreen(
    viewModel: HealthViewModel,
    modifier: Modifier = Modifier
) {
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val walletBalance by viewModel.walletBalance.collectAsStateWithLifecycle()

    val services = listOf(
        MocService("xray", "X-Ray", 400.0, Icons.Default.Person, Color(0xFFE3F2FD), Color(0xFF1E88E5)),
        MocService("ultrasound", "Ultrasound", 800.0, Icons.Default.Tv, Color(0xFFE8F5E9), Color(0xFF4CAF50)),
        MocService("ct_scan", "CT Scan", 1500.0, Icons.Default.AddCircle, Color(0xFFFFF3E0), Color(0xFFFB8C00)),
        MocService("mri", "MRI", 2500.0, Icons.Default.Adjust, Color(0xFFF3E5F5), Color(0xFF9C27B0))
    )

    val locations = listOf(
        MocLocation("sec51", "Sector 51, Gurugram", "Plot 12, Main Market", "0.8 km away"),
        MocLocation("sec69", "Sector 69, Gurugram", "Near Huda City Centre", "3.2 km")
    )

    val dates = listOf(
        MocDate("MON", "13", "13 May"),
        MocDate("TUE", "14", "14 May"),
        MocDate("WED", "15", "15 May"),
        MocDate("THU", "16", "16 May"),
        MocDate("FRI", "17", "17 May")
    )

    val slots = listOf(
        "8:00 AM",
        "10:30 AM",
        "11:00 AM",
        "12:00 PM",
        "2:00 PM",
        "4:00 PM"
    )

    var selectedService by remember { mutableStateOf(services[2]) } // CT Scan selected by default
    var selectedLocation by remember { mutableStateOf(locations[0]) } // Sector 51 selected by default
    var selectedDate by remember { mutableStateOf(dates[2]) } // Wed 15 selected by default
    var selectedTimeSlot by remember { mutableStateOf(slots[1]) } // 10:30 AM selected by default
    var uploadedFileName by remember { mutableStateOf<String?>(null) }

    var showPaymentSheet by remember { mutableStateOf(false) }
    var showTopUpDialog by remember { mutableStateOf(false) }
    var topUpAmount by remember { mutableStateOf("") }
    var showSuccessDialog by remember { mutableStateOf(false) }

    if (showTopUpDialog) {
        AlertDialog(
            onDismissRequest = { showTopUpDialog = false },
            title = { Text("Add Money to Wallet", fontWeight = FontWeight.Bold, color = Color(0xFF0C233A)) },
            text = {
                Column {
                    Text("Select or enter amount to top up your wallet:", fontSize = 13.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf("500", "1000", "2000").forEach { preset ->
                            val isSelected = topUpAmount == preset
                            Button(
                                onClick = { topUpAmount = preset },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSelected) Color(0xFFE91E63) else Color.LightGray.copy(alpha = 0.3f)
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("₹$preset", fontWeight = FontWeight.Bold, color = if (isSelected) Color.White else Color.Black)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    HQTextField(
                        value = topUpAmount,
                        onValueChange = { topUpAmount = it },
                        label = "Custom Amount",
                        placeholder = "₹ Enter amount",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amount = topUpAmount.toDoubleOrNull()
                        if (amount != null && amount > 0) {
                            viewModel.addMoney(amount)
                            showTopUpDialog = false
                            topUpAmount = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63))
                ) {
                    Text("Confirm Add", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { 
                    showTopUpDialog = false
                    topUpAmount = ""
                }) {
                    Text("Cancel")
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White
        )
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Booking Confirmed!", fontWeight = FontWeight.Bold, color = Color(0xFF0C233A))
                }
            },
            text = { 
                Text(
                    "Your diagnostic appointment for ${selectedService.name} has been successfully scheduled.\n\nDate: ${selectedDate.fullDateString}\nTime: ${selectedTimeSlot}\nLocation: ${selectedLocation.name}\n\nDetails are saved in your consultation schedule.",
                    color = Color.DarkGray,
                    fontSize = 14.sp
                ) 
            },
            confirmButton = {
                Button(
                    onClick = { showSuccessDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63))
                ) {
                    Text("Awesome!", fontWeight = FontWeight.Bold)
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = Color.White
        )
    }

    if (showPaymentSheet) {
        ModalBottomSheet(
            onDismissRequest = { showPaymentSheet = false },
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(24.dp)
            ) {
                Text(
                    text = "Confirm Booking Details",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0C233A)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        DetailRow(label = "Selected Service", value = selectedService.name)
                        DetailRow(label = "Location", value = selectedLocation.name)
                        DetailRow(label = "Date & Time", value = "${selectedDate.fullDateString} • ${selectedTimeSlot}")
                        HorizontalDivider(color = Color(0xFFE2E8F0), modifier = Modifier.padding(vertical = 12.dp))
                        DetailRow(
                            label = "Total Amount", 
                            value = "₹${String.format("%.0f", selectedService.price)}",
                            isBold = true,
                            valueColor = Color(0xFFE91E63)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                val hasBalance = walletBalance >= selectedService.price
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Your Wallet Balance",
                            fontSize = 12.sp,
                            color = Color(0xFF8A90A6)
                        )
                        Text(
                            text = "₹${String.format("%,.2f", walletBalance)}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0C233A)
                        )
                    }
                    
                    if (!hasBalance) {
                        Text(
                            text = "Insufficient!",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFEF5350)
                        )
                    } else {
                        Text(
                            text = "Balance: OK ✓",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4CAF50)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                if (hasBalance) {
                    Button(
                        onClick = {
                            viewModel.payOrTransfer(
                                amount = selectedService.price,
                                description = "Diagnostic: ${selectedService.name}",
                                onSuccess = {
                                    val dummyDoctor = DoctorEntity(
                                        id = "service_${selectedService.id}",
                                        name = "${selectedService.name} - Diagnostic",
                                        specialty = "Radiology",
                                        rating = 5.0,
                                        experience = 10,
                                        price = selectedService.price,
                                        department = "Radiology"
                                    )
                                    viewModel.bookAppointment(
                                        doctor = dummyDoctor,
                                        date = selectedDate.fullDateString,
                                        time = selectedTimeSlot,
                                        notes = selectedLocation.name,
                                        onSuccess = {
                                            showPaymentSheet = false
                                            showSuccessDialog = true
                                        }
                                    )
                                },
                                onFailure = { }
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(Color(0xFFBA2D81), Color(0xFFE91E63))
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Pay ₹${String.format("%.0f", selectedService.price)} from Wallet",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = { showTopUpDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            contentPadding = PaddingValues(),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        brush = Brush.horizontalGradient(
                                            colors = listOf(Color(0xFF7B1FA2), Color(0xFF9C27B0))
                                        ),
                                        shape = RoundedCornerShape(16.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Top Up Wallet (Add Money)",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }
                        }
                        
                        OutlinedButton(
                            onClick = {
                                val dummyDoctor = DoctorEntity(
                                    id = "service_${selectedService.id}",
                                    name = "${selectedService.name} - Diagnostic",
                                    specialty = "Radiology",
                                    rating = 5.0,
                                    experience = 10,
                                    price = selectedService.price,
                                    department = "Radiology"
                                )
                                viewModel.bookAppointment(
                                    doctor = dummyDoctor,
                                    date = selectedDate.fullDateString,
                                    time = selectedTimeSlot,
                                    notes = selectedLocation.name,
                                    onSuccess = {
                                        showPaymentSheet = false
                                        showSuccessDialog = true
                                    }
                                )
                            },
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.5.dp, Color(0xFFE91E63)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFE91E63)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(
                                text = "Book & Pay Cash at Clinic",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFCF8FA))
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFFFF0F5), RoundedCornerShape(12.dp))
                    .clickable { backDispatcher?.onBackPressed() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFFE91E63),
                    modifier = Modifier.size(20.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Text(
                text = "Book Appointment",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0C233A)
            )
        }

        // Progress bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            for (i in 0 until 4) {
                val color = if (i < 3) Color(0xFFE91E63) else Color(0xFFE2E8F0)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .background(color, RoundedCornerShape(2.dp))
                )
            }
        }

        // SELECT SERVICE
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(
                text = "SELECT SERVICE",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8A90A6),
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
            )
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                services.forEach { service ->
                    val isSelected = selectedService.id == service.id
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) Color(0xFFFFF0F5) else Color.White
                        ),
                        modifier = Modifier
                            .width(105.dp)
                            .height(115.dp)
                            .clickable { selectedService = service }
                            .border(
                                width = 1.5.dp,
                                color = if (isSelected) Color(0xFFE91E63) else Color(0xFFF1F5F9),
                                shape = RoundedCornerShape(16.dp)
                            ),
                        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 2.dp else 1.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(service.iconBg, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = service.icon,
                                    contentDescription = service.name,
                                    tint = service.iconTint,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = service.name,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0C233A),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        // CHOOSE LOCATION
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Text(
                text = "CHOOSE LOCATION",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8A90A6),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            locations.forEach { location ->
                val isSelected = selectedLocation.id == location.id
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFFFFF0F5) else Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable { selectedLocation = location }
                        .border(
                            width = 1.5.dp,
                            color = if (isSelected) Color(0xFFE91E63) else Color(0xFFF1F5F9),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 2.dp else 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(
                                    if (isSelected) Color(0xFFFFF0F5) else Color(0xFFF8FAFC), 
                                    CircleShape
                        ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = if (isSelected) Color(0xFFE91E63) else Color(0xFF8A90A6),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        Column {
                            Text(
                                text = location.name,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0C233A)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${location.address} • ${location.distance}",
                                fontSize = 12.sp,
                                color = Color(0xFF8A90A6)
                            )
                        }
                    }
                }
            }
        }

        // PICK A DATE
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text(
                text = "PICK A DATE",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8A90A6),
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp)
            )
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                dates.forEach { date ->
                    val isSelected = selectedDate.dayOfMonth == date.dayOfMonth
                    
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                        modifier = Modifier
                            .width(62.dp)
                            .height(78.dp)
                            .clickable { selectedDate = date }
                            .background(
                                brush = if (isSelected) {
                                    Brush.verticalGradient(colors = listOf(Color(0xFFBA2D81), Color(0xFFE91E63)))
                                } else {
                                    Brush.verticalGradient(colors = listOf(Color.White, Color.White))
                                },
                                shape = RoundedCornerShape(14.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = if (isSelected) Color.Transparent else Color(0xFFE2E8F0),
                                shape = RoundedCornerShape(14.dp)
                            ),
                        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 2.dp else 0.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = date.dayOfWeek,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White.copy(alpha = 0.9f) else Color(0xFF8A90A6)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = date.dayOfMonth,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else Color(0xFF0C233A)
                            )
                        }
                    }
                }
            }
        }

        // TIME SLOT
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Text(
                text = "TIME SLOT",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8A90A6),
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                for (row in 0 until 2) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        for (col in 0 until 3) {
                            val index = row * 3 + col
                            if (index < slots.size) {
                                val slot = slots[index]
                                val is8am = slot == "8:00 AM"
                                val isSelected = selectedTimeSlot == slot
                                
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(44.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            brush = if (isSelected) {
                                                Brush.horizontalGradient(colors = listOf(Color(0xFFBA2D81), Color(0xFFE91E63)))
                                            } else {
                                                Brush.horizontalGradient(colors = listOf(Color.White, Color.White))
                                            }
                                        )
                                        .border(
                                            width = 1.dp,
                                            color = if (isSelected) Color.Transparent else if (is8am) Color(0xFFF1F5F9) else Color(0xFFE2E8F0),
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .clickable(enabled = !is8am) { selectedTimeSlot = slot },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = slot,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) {
                                            Color.White
                                        } else if (is8am) {
                                            Color(0xFFCBD5E1)
                                        } else {
                                            Color(0xFF0C233A)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // UPLOAD PRESCRIPTION
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Text(
                text = "UPLOAD PRESCRIPTION (OPTIONAL)",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0C233A),
                modifier = Modifier.padding(bottom = 10.dp)
            )
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .drawBehind {
                        val stroke = Stroke(
                            width = 1.5.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 12f), 0f)
                        )
                        drawRoundRect(
                            color = Color(0xFFB2DFDB),
                            style = stroke,
                            cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx())
                        )
                    }
                    .clickable {
                        if (uploadedFileName == null) {
                            uploadedFileName = "prescription_july_2026.pdf"
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(
                    targetState = uploadedFileName,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "PrescriptionContent"
                ) { fileName ->
                    if (fileName == null) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CloudUpload,
                                contentDescription = null,
                                tint = Color(0xFF8A90A6),
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Tap to upload prescription",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0C233A)
                            )
                            Text(
                                text = "PDF, JPG • max 5MB",
                                fontSize = 11.sp,
                                color = Color(0xFF8A90A6),
                                modifier = Modifier.padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Browse Files",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFE91E63)
                            )
                        }
                    } else {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .background(Color(0xFFE8F5E9), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Description,
                                        contentDescription = null,
                                        tint = Color(0xFF4CAF50),
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Column {
                                    Text(
                                        text = fileName,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0C233A)
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "1.2 MB • Ready to send",
                                        fontSize = 11.sp,
                                        color = Color(0xFF4CAF50),
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                            IconButton(
                                onClick = { uploadedFileName = null }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remove file",
                                    tint = Color(0xFFEF5350)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Proceed button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 24.dp)
        ) {
            Button(
                onClick = { showPaymentSheet = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .shadow(4.dp, RoundedCornerShape(24.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFFBA2D81),
                                    Color(0xFFE91E63)
                                )
                            ),
                            shape = RoundedCornerShape(24.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Proceed to Payment ➔",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// 2. Support Screen
@Composable
fun SupportScreen(
    viewModel: HealthViewModel,
    modifier: Modifier = Modifier
) {
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    val faqs = listOf(
        "How do I get my reports?" to "You will receive a push notification once your report is ready. Go to My Reports to view or download the PDF instantly.",
        "Can I reschedule an appointment?" to "Yes, you can reschedule any upcoming appointment up to 2 hours prior to the scheduled time by visiting your active booking details.",
        "Is fasting required for my test?" to "Fasting requirements vary by test. For lipid profile and blood sugar fasting, 8-12 hours of fasting is recommended. Please check your package instructions."
    )

    // State to track expanded FAQ item indices. Default index 0 to true to match mockup.
    val expandedStates = remember { mutableStateMapOf<Int, Boolean>().apply { put(0, true) } }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFCF8FA))
            .padding(horizontal = 24.dp)
    ) {
        // High-fidelity Header Row with custom pink back button and title
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = 20.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFFF0F5))
                        .clickable { backDispatcher?.onBackPressed() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFFE91E63),
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Help & Support",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0C233A)
                )
            }
        }

        // WhatsApp Us Card
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFFDEEF4)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF25D366)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChatBubble,
                            contentDescription = "WhatsApp",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "WhatsApp Us",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0C233A)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Chat instantly with support",
                            fontSize = 13.sp,
                            color = Color(0xFF64748B)
                        )
                        Text(
                            text = "• Available 24/7",
                            fontSize = 13.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }
            }
        }

        // Call Centre Card
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFFDEEF4)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFE0F7FA)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "Call",
                            tint = Color(0xFF00ACC1),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Call Centre",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0C233A)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "0124-4567890 • Mon–Sat",
                            fontSize = 13.sp,
                            color = Color(0xFF64748B)
                        )
                        Text(
                            text = "8AM–8PM",
                            fontSize = 13.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }
            }
        }

        // Section Title: FREQUENTLY ASKED QUESTIONS
        item {
            Text(
                text = "FREQUENTLY ASKED QUESTIONS",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF475569),
                letterSpacing = 0.8.sp,
                modifier = Modifier
                    .padding(top = 28.dp, bottom = 12.dp)
            )
        }

        // Expandable FAQ Accordion List
        items(faqs.size) { index ->
            val faq = faqs[index]
            val isExpanded = expandedStates[index] ?: false

            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFFDEEF4)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { expandedStates[index] = !isExpanded }
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = faq.first,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0C233A),
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = if (isExpanded) "Collapse" else "Expand",
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    AnimatedVisibility(
                        visible = isExpanded,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = faq.second,
                                fontSize = 13.sp,
                                color = Color(0xFF64748B),
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// 3. Notifications Screen
@Composable
fun NotificationsScreen(
    viewModel: HealthViewModel,
    modifier: Modifier = Modifier
) {
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    // Split into TODAY and EARLIER
    val todayNotifications = notifications.filter { 
        System.currentTimeMillis() - it.timestamp < 24 * 3600 * 1000 
    }
    val earlierNotifications = notifications.filter { 
        System.currentTimeMillis() - it.timestamp >= 24 * 3600 * 1000 
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFCF8FA))
            .padding(horizontal = 24.dp)
    ) {
        // High-fidelity Header Row with custom pink back button and title
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = 20.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFFFF0F5))
                            .clickable { backDispatcher?.onBackPressed() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFFE91E63),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Notifications",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0C233A)
                    )
                }

                if (notifications.any { !it.isRead }) {
                    Text(
                        text = "Mark all as read",
                        color = Color(0xFFE91E63),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.clickable { viewModel.markNotificationsRead() }
                    )
                }
            }
        }

        if (notifications.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillParentMaxHeight(0.7f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.NotificationsOff,
                            contentDescription = null,
                            tint = Color.LightGray,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No notifications yet",
                            color = Color.Gray,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        } else {
            // TODAY SECTION
            if (todayNotifications.isNotEmpty()) {
                item {
                    Text(
                        text = "TODAY",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF475569),
                        letterSpacing = 0.8.sp,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                }
                items(todayNotifications) { notif ->
                    CustomNotificationItem(notification = notif)
                }
            }

            // EARLIER SECTION
            if (earlierNotifications.isNotEmpty()) {
                item {
                    Text(
                        text = "EARLIER",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF475569),
                        letterSpacing = 0.8.sp,
                        modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
                    )
                }
                items(earlierNotifications) { notif ->
                    CustomNotificationItem(notification = notif)
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun CustomNotificationItem(
    notification: NotificationEntity,
    modifier: Modifier = Modifier
) {
    val icon = when {
        notification.title.contains("Report") -> Icons.Default.Description
        notification.title.contains("Reminder") -> Icons.Default.Info
        notification.title.contains("Offer") -> Icons.Default.LocalOffer
        notification.title.contains("Confirmed") -> Icons.Default.Check
        else -> Icons.Default.Notifications
    }

    val timeText = when {
        notification.title.contains("Report") -> "Just now"
        notification.title.contains("Reminder") -> "2 hours ago"
        notification.title.contains("Offer") -> "Yesterday • 6:30 PM"
        notification.title.contains("Confirmed") -> "1 May • 9:15 AM"
        else -> {
            val diff = System.currentTimeMillis() - notification.timestamp
            when {
                diff < 60000 -> "Just now"
                diff < 3600000 -> "${diff / 60000} mins ago"
                diff < 86400000 -> "${diff / 3600000} hours ago"
                else -> "1 day ago"
            }
        }
    }

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFFDEEF4)),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFF0F5)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFFE91E63),
                    modifier = Modifier.size(20.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = notification.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0C233A)
                    )
                    if (!notification.isRead) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE91E63))
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = notification.message,
                    fontSize = 13.sp,
                    color = Color(0xFF64748B),
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = timeText,
                    fontSize = 12.sp,
                    color = Color(0xFF94A3B8)
                )
            }
        }
    }
}

// 4. Wallet Screen
data class LocalDisplayTransaction(
    val title: String,
    val subtitle: String,
    val amount: Double,
    val type: String, // "Credit" or "Debit"
    val date: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val iconColor: Color,
    val iconBgColor: Color
)

data class LocalLinkedPaymentMethod(
    val name: String,
    val details: String,
    val isDefault: Boolean,
    val type: String // "UPI", "Card"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletScreen(
    viewModel: HealthViewModel,
    onBackClick: (() -> Unit)? = null,
    onNotificationClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val balance by viewModel.walletBalance.collectAsStateWithLifecycle()
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()
    val listState = androidx.compose.foundation.lazy.rememberLazyListState()

    var showAddDialog by remember { mutableStateOf(false) }
    var addAmount by remember { mutableStateOf("") }

    var showPayBillDialog by remember { mutableStateOf(false) }
    var billerName by remember { mutableStateOf("") }
    var billAmount by remember { mutableStateOf("") }
    var selectedBillerType by remember { mutableStateOf("Electricity") }

    var showTransferDialog by remember { mutableStateOf(false) }
    var transferName by remember { mutableStateOf("") }
    var transferDetails by remember { mutableStateOf("") }
    var transferAmount by remember { mutableStateOf("") }

    var showNewPaymentMethodDialog by remember { mutableStateOf(false) }
    var newMethodName by remember { mutableStateOf("") }
    var newMethodDetails by remember { mutableStateOf("") }
    var newMethodType by remember { mutableStateOf("UPI") }

    var statusMessage by remember { mutableStateOf("") }
    var showStatusDialog by remember { mutableStateOf(false) }
    var isStatusSuccess by remember { mutableStateOf(true) }

    var linkedMethods by remember {
        mutableStateOf(
            listOf(
                LocalLinkedPaymentMethod("HDFC Bank UPI", "rajesh@hdfcbank", true, "UPI"),
                LocalLinkedPaymentMethod("Visa •••• 4812", "Expires 08/27", false, "Card")
            )
        )
    }

    // Helper to format currency
    val integerPart = balance.toLong()
    val fractionalPart = String.format("%.2f", balance).split(".").getOrNull(1) ?: "00"
    val formattedInteger = String.format("%,d", integerPart)

    // Maps real transactions from database + mock transactions from screenshot
    val dbTransactionsMapped = transactions.map { tx ->
        val isCredit = tx.type == "Credit"
        LocalDisplayTransaction(
            title = tx.description,
            subtitle = if (isCredit) "Added to Health Wallet" else "Paid from Health Wallet",
            amount = tx.amount,
            type = tx.type,
            date = "Today",
            icon = if (isCredit) Icons.Default.Add else Icons.Default.Receipt,
            iconColor = if (isCredit) Color(0xFF4CAF50) else Color(0xFFF44336),
            iconBgColor = if (isCredit) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
        )
    }

    val staticMockTransactions = listOf(
        LocalDisplayTransaction("Money Added", "Via UPI - 14 May", 500.0, "Credit", "14 May", Icons.Default.Check, Color(0xFF4CAF50), Color(0xFFE8F5E9)),
        LocalDisplayTransaction("Dr. Priya Sharma", "Consultation - 12 May", 350.0, "Debit", "12 May", Icons.Default.Person, Color(0xFFF44336), Color(0xFFFFEBEE)),
        LocalDisplayTransaction("Cashback Earned", "Blood Test - 10 May", 40.0, "Credit", "10 May", Icons.Default.Star, Color(0xFFFBC02D), Color(0xFFFFFDE7)),
        LocalDisplayTransaction("CBC Lab Report", "PathCare Lab - 8 May", 220.0, "Debit", "8 May", Icons.Default.Science, Color(0xFF673AB7), Color(0xFFEDE7F6)),
        LocalDisplayTransaction("Refund – Cancelled", "Appointment - 5 May", 200.0, "Credit", "5 May", Icons.Default.Healing, Color(0xFF009688), Color(0xFFE0F2F1))
    )

    val allTransactions = dbTransactionsMapped + staticMockTransactions

    // Dialogs Implementation
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add Money to Wallet", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Select or enter amount to top up your mock wallet:", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf("500", "1000", "2000").forEach { preset ->
                            val isSelected = addAmount == preset
                            Button(
                                onClick = { addAmount = preset },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSelected) PrimaryLight else Color.LightGray.copy(alpha = 0.3f)
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("₹$preset", fontWeight = FontWeight.Bold, color = if (isSelected) Color.White else Color.Black)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    HQTextField(
                        value = addAmount,
                        onValueChange = { addAmount = it },
                        label = "Custom Amount",
                        placeholder = "₹ Enter amount",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amount = addAmount.toDoubleOrNull()
                        if (amount != null && amount > 0) {
                            viewModel.addMoney(amount)
                            showAddDialog = false
                            addAmount = ""
                            isStatusSuccess = true
                            statusMessage = "₹$amount successfully added to your Health Wallet."
                            showStatusDialog = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryLight)
                ) {
                    Text("Confirm Add", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { 
                    showAddDialog = false
                    addAmount = ""
                }) {
                    Text("Cancel")
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White
        )
    }

    if (showPayBillDialog) {
        AlertDialog(
            onDismissRequest = { showPayBillDialog = false },
            title = { Text("Pay Utility Bill", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Quickly settle your bills directly using your Health Wallet balance.", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Electricity", "Water", "Gas").forEach { type ->
                            val isSelected = selectedBillerType == type
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) PrimaryLight else Color.LightGray.copy(alpha = 0.2f),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { selectedBillerType = type }
                            ) {
                                Text(
                                    text = type,
                                    color = if (isSelected) Color.White else Color.DarkGray,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                        }
                    }

                    HQTextField(
                        value = billerName,
                        onValueChange = { billerName = it },
                        label = "Biller / Provider Name",
                        placeholder = "e.g. Tata Power, BESCOM"
                    )

                    HQTextField(
                        value = billAmount,
                        onValueChange = { billAmount = it },
                        label = "Bill Amount (₹)",
                        placeholder = "e.g. 450",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amount = billAmount.toDoubleOrNull()
                        if (amount != null && amount > 0 && billerName.isNotBlank()) {
                            viewModel.payOrTransfer(
                                amount = amount,
                                description = "$selectedBillerType Bill: $billerName",
                                onSuccess = {
                                    showPayBillDialog = false
                                    billerName = ""
                                    billAmount = ""
                                    isStatusSuccess = true
                                    statusMessage = "Payment of ₹$amount to $billerName successful!"
                                    showStatusDialog = true
                                },
                                onFailure = { error ->
                                    showPayBillDialog = false
                                    isStatusSuccess = false
                                    statusMessage = error
                                    showStatusDialog = true
                                }
                            )
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryLight)
                ) {
                    Text("Pay Bill Now", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showPayBillDialog = false }) {
                    Text("Cancel")
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White
        )
    }

    if (showTransferDialog) {
        AlertDialog(
            onDismissRequest = { showTransferDialog = false },
            title = { Text("Transfer Wallet Balance", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Transfer mock balance instantly to any contact or UPI VPA.", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    
                    HQTextField(
                        value = transferName,
                        onValueChange = { transferName = it },
                        label = "Recipient Full Name",
                        placeholder = "e.g. Rajesh Kumar"
                    )

                    HQTextField(
                        value = transferDetails,
                        onValueChange = { transferDetails = it },
                        label = "UPI VPA ID or Mobile Number",
                        placeholder = "e.g. target@okaxis, 9876543210"
                    )

                    HQTextField(
                        value = transferAmount,
                        onValueChange = { transferAmount = it },
                        label = "Transfer Amount (₹)",
                        placeholder = "e.g. 500",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amount = transferAmount.toDoubleOrNull()
                        if (amount != null && amount > 0 && transferName.isNotBlank() && transferDetails.isNotBlank()) {
                            viewModel.payOrTransfer(
                                amount = amount,
                                description = "Transfer to $transferName",
                                onSuccess = {
                                    showTransferDialog = false
                                    transferName = ""
                                    transferDetails = ""
                                    transferAmount = ""
                                    isStatusSuccess = true
                                    statusMessage = "Transfer of ₹$amount to $transferName successful!"
                                    showStatusDialog = true
                                },
                                onFailure = { error ->
                                    showTransferDialog = false
                                    isStatusSuccess = false
                                    statusMessage = error
                                    showStatusDialog = true
                                }
                            )
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryLight)
                ) {
                    Text("Send Now", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showTransferDialog = false }) {
                    Text("Cancel")
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White
        )
    }

    if (showNewPaymentMethodDialog) {
        AlertDialog(
            onDismissRequest = { showNewPaymentMethodDialog = false },
            title = { Text("Add Linked Payment Method", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Add a mock payment source to your linked payment methods.", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("UPI", "Card").forEach { type ->
                            val isSelected = newMethodType == type
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) PrimaryLight else Color.LightGray.copy(alpha = 0.2f),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { newMethodType = type }
                            ) {
                                Text(
                                    text = type,
                                    color = if (isSelected) Color.White else Color.DarkGray,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                        }
                    }

                    HQTextField(
                        value = newMethodName,
                        onValueChange = { newMethodName = it },
                        label = if (newMethodType == "UPI") "Bank Name" else "Card Network / Name",
                        placeholder = if (newMethodType == "UPI") "e.g. HDFC Bank, ICICI Bank" else "e.g. Visa, MasterCard"
                    )

                    HQTextField(
                        value = newMethodDetails,
                        onValueChange = { newMethodDetails = it },
                        label = if (newMethodType == "UPI") "UPI VPA address" else "Card Details (Exp date)",
                        placeholder = if (newMethodType == "UPI") "e.g. name@okhdfcbank" else "e.g. Visa •••• 1234 (Expires 12/29)"
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newMethodName.isNotBlank() && newMethodDetails.isNotBlank()) {
                            val newMethod = LocalLinkedPaymentMethod(
                                name = if (newMethodType == "UPI") "$newMethodName UPI" else newMethodName,
                                details = newMethodDetails,
                                isDefault = linkedMethods.isEmpty(),
                                type = newMethodType
                            )
                            linkedMethods = linkedMethods + newMethod
                            showNewPaymentMethodDialog = false
                            newMethodName = ""
                            newMethodDetails = ""
                            isStatusSuccess = true
                            statusMessage = "New payment method successfully linked!"
                            showStatusDialog = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryLight)
                ) {
                    Text("Link Method", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showNewPaymentMethodDialog = false }) {
                    Text("Cancel")
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White
        )
    }

    if (showStatusDialog) {
        AlertDialog(
            onDismissRequest = { showStatusDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isStatusSuccess) Icons.Default.CheckCircle else Icons.Default.Info,
                        contentDescription = null,
                        tint = if (isStatusSuccess) StatusSuccess else StatusError,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isStatusSuccess) "Success" else "Transaction Failed", fontWeight = FontWeight.Bold)
                }
            },
            text = { Text(statusMessage, style = MaterialTheme.typography.bodyMedium) },
            confirmButton = {
                Button(
                    onClick = { showStatusDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = if (isStatusSuccess) StatusSuccess else PrimaryLight)
                ) {
                    Text("OK")
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = Color.White
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color(0xFFFFF9FC) // Soft background as in screenshot
    ) { paddingValues ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Gorgeous curved gradient header section
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF8E24AA), // Deep Violet
                                    Color(0xFFD81B60), // Hot Magenta
                                    Color(0xFFE91E63)  // Vibrant Pink
                                )
                            ),
                            shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                        )
                        .padding(bottom = 32.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    ) {
                        // Header Navigation Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .statusBarsPadding()
                                .padding(vertical = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Back button
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White.copy(alpha = 0.2f))
                                    .clickable { onBackClick?.invoke() },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            // Title
                            Text(
                                text = "My Wallet",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            // Notification bell
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White.copy(alpha = 0.2f))
                                    .clickable { onNotificationClick?.invoke() },
                                contentAlignment = Alignment.Center
                            ) {
                                Box {
                                    Icon(
                                        imageVector = Icons.Default.Notifications,
                                        contentDescription = "Notifications",
                                        tint = Color(0xFFFFD54F), // Sweet gold bell color
                                        modifier = Modifier.size(20.dp)
                                    )
                                    // Notification badge dot
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .align(Alignment.TopEnd)
                                            .background(Color.Red, CircleShape)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Glassmorphic Balance Card Container
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Color.White.copy(alpha = 0.12f),
                                    shape = RoundedCornerShape(24.dp)
                                )
                                .border(
                                    width = 1.dp,
                                    color = Color.White.copy(alpha = 0.25f),
                                    shape = RoundedCornerShape(24.dp)
                                )
                                .padding(24.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Total Wallet Balance",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.White.copy(alpha = 0.85f),
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    
                                    // Split Rupees and Paise representation using single Text and buildAnnotatedString to prevent vertical wrapping
                                    Text(
                                        text = buildAnnotatedString {
                                            withStyle(
                                                style = SpanStyle(
                                                    fontSize = 38.sp,
                                                    fontWeight = FontWeight.Black,
                                                    color = Color.White
                                                )
                                            ) {
                                                append("₹$formattedInteger")
                                            }
                                            withStyle(
                                                style = SpanStyle(
                                                    fontSize = 22.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White.copy(alpha = 0.85f)
                                                )
                                            ) {
                                                append(".$fractionalPart")
                                            }
                                        },
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                    
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "+ ₹240 Cashback pending",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFFB9F6CA), // Soft emerald cashback text
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }

                                // Capsule + Add Money button inside card
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50))
                                        .background(Color.White.copy(alpha = 0.18f))
                                        .border(1.dp, Color.White, RoundedCornerShape(50))
                                        .clickable { showAddDialog = true }
                                        .padding(horizontal = 16.dp, vertical = 10.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Add Money",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Quick Actions Section
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                    Text(
                        text = "Quick Actions",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = OnBackgroundLight
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Quick Action: Add Money
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable { showAddDialog = true }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(Color(0xFFE040FB), Color(0xFFD81B60))
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AddCard,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Add Money", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, color = Color.DarkGray)
                        }

                        // Quick Action: Pay Bill
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable { showPayBillDialog = true }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(Color(0xFFE040FB), Color(0xFFD81B60))
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Receipt,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Pay Bill", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, color = Color.DarkGray)
                        }

                        // Quick Action: Transfer
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable { showTransferDialog = true }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(Color(0xFFE040FB), Color(0xFFD81B60))
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CompareArrows,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Transfer", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, color = Color.DarkGray)
                        }

                        // Quick Action: History
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable {
                                scope.launch {
                                    // Scroll straight to Transactions Header
                                    listState.animateScrollToItem(index = 3)
                                }
                            }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(Color(0xFFE040FB), Color(0xFFD81B60))
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.History,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("History", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, color = Color.DarkGray)
                        }
                    }
                }
            }

            // Cashback / Marketing Promo Banner
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Box(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF673AB7), Color(0xFFE91E63))
                            ),
                            shape = RoundedCornerShape(18.dp)
                        )
                        .clickable {
                            isStatusSuccess = true
                            statusMessage = "Congratulations! Active promo code 'CASHBACK10' applied on your profile. Book any lab package to claim."
                            showStatusDialog = true
                        }
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(Color.White.copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CardGiftcard,
                                contentDescription = null,
                                tint = Color(0xFFFFD54F),
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Get 10% Cashback!",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleSmall
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "On every lab test booked this week",
                                color = Color.White.copy(alpha = 0.85f),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }

            // Recent Transactions Section Header
            item {
                Spacer(modifier = Modifier.height(28.dp))
                Text(
                    text = "Recent Transactions",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = OnBackgroundLight,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // List of Transactions (Real DB + Predefined visual mockups)
            if (allTransactions.isEmpty()) {
                item {
                    Text(
                        text = "No recent transactions.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                    )
                }
            } else {
                items(allTransactions) { tx ->
                    val isCredit = tx.type == "Credit"
                    val sign = if (isCredit) "+" else "-"
                    val displayColor = if (isCredit) Color(0xFF4CAF50) else Color(0xFFF44336)

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Circular styled icon
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .background(tx.iconBgColor, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = tx.icon,
                                        contentDescription = null,
                                        tint = tx.iconColor,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                
                                Spacer(modifier = Modifier.width(14.dp))
                                
                                Column {
                                    Text(
                                        text = tx.title,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = OnBackgroundLight
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = tx.subtitle,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray
                                    )
                                }
                            }

                            Text(
                                text = "$sign₹${String.format("%.0f", tx.amount)}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = displayColor
                            )
                        }
                    }
                }
            }

            // Linked Payment Methods Header
            item {
                Spacer(modifier = Modifier.height(28.dp))
                Text(
                    text = "Linked Payment Methods",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = OnBackgroundLight,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Payment Methods list
            items(linkedMethods) { method ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(Color.LightGray.copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (method.type == "UPI") Icons.Default.AccountBalance else Icons.Default.CreditCard,
                                    contentDescription = null,
                                    tint = PrimaryLight,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = method.name,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = OnBackgroundLight
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = method.details,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                        }

                        if (method.isDefault) {
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFFCE4EC), RoundedCornerShape(50))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Default",
                                    color = Color(0xFFD81B60),
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp)
                                )
                            }
                        }
                    }
                }
            }

            // Dashed / Dotted "+ Add New Payment Method" Button
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { showNewPaymentMethodDialog = true }
                        .drawBehind {
                            val stroke = androidx.compose.ui.graphics.drawscope.Stroke(
                                width = 3f,
                                pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
                            )
                            drawRoundRect(
                                color = PrimaryLight,
                                style = stroke,
                                cornerRadius = androidx.compose.ui.geometry.CornerRadius(12.dp.toPx())
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = PrimaryLight,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Add New Payment Method",
                            color = PrimaryLight,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}
