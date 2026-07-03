package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.entity.*
import com.example.ui.components.*
import com.example.ui.navigation.Screen
import com.example.ui.theme.*
import com.example.viewmodel.HealthViewModel

// 1. Home Screen
@Composable
fun HomeScreen(
    viewModel: HealthViewModel,
    onNavigateTo: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.userProfile.collectAsStateWithLifecycle()
    val appts by viewModel.appointments.collectAsStateWithLifecycle()
    val packages by viewModel.healthPackages.collectAsStateWithLifecycle()

    val name = profile?.name ?: "Rajesh Kumar"
    
    // Dynamic initials for the top-right avatar
    val initials = name.split(" ")
        .mapNotNull { it.firstOrNull()?.uppercaseChar() }
        .joinToString("")
        .take(2)
        .ifEmpty { "RK" }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFCF8FA)) // Premium soft off-white/rosy background
    ) {
        // Upper Gradient Header Block with Search Bar
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFBA2D81), // Rich magenta
                                Color(0xFFE91E63)  // Hot pink
                            )
                        )
                    )
                    .statusBarsPadding()
                    .padding(horizontal = 24.dp)
                    .padding(top = 16.dp, bottom = 28.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Profile Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Good Morning,",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "$name 👋",
                                fontSize = 21.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        
                        // RK Avatar Circle
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .border(1.dp, Color.White.copy(alpha = 0.6f), CircleShape)
                                .background(Color.White.copy(alpha = 0.15f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = initials,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    // Search Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .background(Color.White.copy(alpha = 0.25f), RoundedCornerShape(24.dp))
                            .clickable { onNavigateTo(Screen.HealthPackages.route) }
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.White.copy(alpha = 0.9f),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Search tests, packages...",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }
            }
        }

        // LIMITED OFFER Promo Card Section
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 20.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF7B1FA2), // Violet
                                    Color(0xFFE91E63)  // Pink
                                )
                            ),
                            shape = RoundedCornerShape(24.dp)
                        )
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        // Decorative ambient circle on the top right
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .align(Alignment.TopEnd)
                                .offset(x = 20.dp, y = (-20).dp)
                                .background(Color.White.copy(alpha = 0.08f), CircleShape)
                        )
                        
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "LIMITED OFFER",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White.copy(alpha = 0.9f),
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Full Body\nHealth Checkup",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    lineHeight = 24.sp
                                )
                            }
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Save ₹800 • Valid till 31 May",
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                                
                                // Book Now pill button
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color.White)
                                        .clickable { onNavigateTo(Screen.HealthPackages.route) }
                                        .padding(horizontal = 16.dp, vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Book Now ➔",
                                        color = Color(0xFFE91E63),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Our Services Section
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Our Services",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0C233A)
                    )
                    Text(
                        text = "View All",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE91E63),
                        modifier = Modifier.clickable { onNavigateTo(Screen.HealthPackages.route) }
                    )
                }
                
                Spacer(modifier = Modifier.height(14.dp))
                
                // Horizontal scrolling Services row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ServiceCard(
                        title = "X-Ray",
                        icon = Icons.Default.Person,
                        iconBgColor = Color(0xFFE3F2FD),
                        iconTint = Color(0xFF1E88E5),
                        onClick = { onNavigateTo(Screen.BookAppointment.route) }
                    )
                    ServiceCard(
                        title = "Ultrasound",
                        icon = Icons.Default.PersonalVideo,
                        iconBgColor = Color(0xFFE8F5E9),
                        iconTint = Color(0xFF4CAF50),
                        onClick = { onNavigateTo(Screen.BookAppointment.route) }
                    )
                    ServiceCard(
                        title = "CT Scan",
                        icon = Icons.Default.AddCircle,
                        iconBgColor = Color(0xFFFFF3E0),
                        iconTint = Color(0xFFFB8C00),
                        onClick = { onNavigateTo(Screen.BookAppointment.route) }
                    )
                    ServiceCard(
                        title = "MRI",
                        icon = Icons.Default.BlurCircular,
                        iconBgColor = Color(0xFFF3E5F5),
                        iconTint = Color(0xFF9C27B0),
                        onClick = { onNavigateTo(Screen.BookAppointment.route) }
                    )
                }
            }
        }

        // Upcoming Appointments Section
        item {
            val upcoming = appts.filter { it.status != "Cancelled" && it.status != "Completed" }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Upcoming Appointments",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0C233A)
                    )
                    Text(
                        text = "History",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE91E63),
                        modifier = Modifier.clickable { onNavigateTo(Screen.BookAppointment.route) }
                    )
                }
                
                Spacer(modifier = Modifier.height(14.dp))
                
                if (upcoming.isEmpty()) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(16.dp)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(24.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.EventBusy,
                                contentDescription = null,
                                tint = Color.LightGray,
                                modifier = Modifier.size(44.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No upcoming consultations",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFF0C233A)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Book an appointment with top specialists",
                                color = Color.Gray,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        upcoming.forEach { appt ->
                            MocAppointmentCard(
                                appointment = appt,
                                onCancelClick = { viewModel.cancelAppointment(appt.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ServiceCard(
    title: String,
    icon: ImageVector,
    iconBgColor: Color,
    iconTint: Color,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .width(96.dp)
            .height(115.dp)
            .clickable(onClick = onClick)
            .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(16.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
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
                    .background(iconBgColor, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun MocAppointmentCard(
    appointment: AppointmentEntity,
    onCancelClick: () -> Unit
) {
    val isCt = appointment.doctorName.contains("CT", ignoreCase = true)
    val isUltrasound = appointment.doctorName.contains("Ultrasound", ignoreCase = true)
    
    val (iconBgColor, iconTint, icon) = when {
        isCt -> Triple(Color(0xFFFFF0F5), Color(0xFFE91E63), Icons.Default.AddCircle)
        isUltrasound -> Triple(Color(0xFFF3E5F5), Color(0xFF9C27B0), Icons.Default.TripOrigin)
        else -> Triple(Color(0xFFE3F2FD), Color(0xFF1E88E5), Icons.Default.Event)
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(16.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(iconBgColor, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(14.dp))
            
            // Text Column
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = appointment.doctorName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0C233A)
                )
                Spacer(modifier = Modifier.height(4.dp))
                
                // Calendar/Clock/Sector text
                val dateString = buildString {
                    append("📆 ")
                    append(appointment.date)
                    append(" • ")
                    append(appointment.timeSlot)
                    if (appointment.notes.isNotEmpty()) {
                        append(" • ")
                        append(appointment.notes)
                    }
                }
                Text(
                    text = dateString,
                    fontSize = 12.sp,
                    color = Color(0xFF8A90A6),
                    fontWeight = FontWeight.Medium
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Status Badge
            val status = appointment.status
            val badgeBg = if (status == "Confirmed") Color(0xFFFFF5F8) else Color(0xFFFFF3E0)
            val badgeTextColor = if (status == "Confirmed") Color(0xFFE91E63) else Color(0xFFFB8C00)
            
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(badgeBg)
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = status,
                    color = badgeTextColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// 2. Reports Screen
@Composable
fun ReportsScreen(
    viewModel: HealthViewModel,
    modifier: Modifier = Modifier
) {
    val reports by viewModel.reports.collectAsStateWithLifecycle()
    val backDispatcher = androidx.activity.compose.LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    var selectedFilter by remember { mutableStateOf("All") }

    // Dialog States
    var showViewDialog by remember { mutableStateOf<ReportEntity?>(null) }
    var showPdfDialog by remember { mutableStateOf<ReportEntity?>(null) }
    var isDownloading by remember { mutableStateOf(false) }

    LaunchedEffect(showPdfDialog) {
        if (showPdfDialog != null) {
            isDownloading = true
            kotlinx.coroutines.delay(1500)
            isDownloading = false
        }
    }

    val filteredReports = reports.filter { rep ->
        when (selectedFilter) {
            "Ready" -> rep.status.equals("Available", ignoreCase = true)
            "Pending" -> rep.status.equals("Pending", ignoreCase = true)
            else -> true
        }
    }

    // PDF Dialog
    if (showPdfDialog != null) {
        val rep = showPdfDialog!!
        AlertDialog(
            onDismissRequest = { if (!isDownloading) showPdfDialog = null },
            title = {
                Text(
                    text = if (isDownloading) "Downloading Report..." else "Download Complete",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0C233A)
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (isDownloading) {
                        Spacer(modifier = Modifier.height(16.dp))
                        CircularProgressIndicator(color = Color(0xFFE91E63))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Generating and downloading secure PDF...",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    } else {
                        Spacer(modifier = Modifier.height(8.dp))
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "${rep.title}.pdf has been saved to your downloads.",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF0C233A),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "You can view it offline at any time.",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            },
            confirmButton = {
                if (!isDownloading) {
                    Button(
                        onClick = { showPdfDialog = null },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63))
                    ) {
                        Text("Awesome", fontWeight = FontWeight.Bold)
                    }
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White
        )
    }

    // View Dialog
    if (showViewDialog != null) {
        val rep = showViewDialog!!
        AlertDialog(
            onDismissRequest = { showViewDialog = null },
            title = {
                Text(
                    text = rep.title,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0C233A)
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "Patient: ${rep.patientName}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF8A90A6)
                    )
                    Text(
                        text = "Date: ${rep.date} • Location: ${rep.doctorName}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Color(0xFFE2E8F0))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "LABORATORY FINDINGS & OBSERVATIONS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE91E63)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    val observations = when {
                        rep.title.contains("CT Scan", ignoreCase = true) -> {
                            "• LUNGS: Lung fields show normal expansion. No focal consolidation, pleural effusion, or pneumothorax is seen.\n" +
                            "• TRACHEA: Tracheobronchial tree is normal in caliber. No endobronchial lesion.\n" +
                            "• MEDIASTINUM: No significant mediastinal, hilar, or axillary lymphadenopathy.\n" +
                            "• IMPRESSION: Normal CT scan of the chest. No active cardiopulmonary disease detected."
                        }
                        rep.title.contains("X-Ray", ignoreCase = true) -> {
                            "• BONES: Bony structures of the left knee are intact with normal mineralization.\n" +
                            "• JOINTS: Joint spaces are well preserved. No signs of narrowing, fracture, or subluxation.\n" +
                            "• SOFT TISSUES: Normal soft tissue plane. No joint effusion is visualized.\n" +
                            "• IMPRESSION: Radiographically normal left knee joint. No acute fracture or dislocation."
                        }
                        else -> {
                            "All tested parameters are within physiological range. Recommendations include maintaining a balanced diet, proper hydration, and routine physical checkups."
                        }
                    }
                    Text(
                        text = observations,
                        fontSize = 13.sp,
                        color = Color(0xFF0C233A),
                        lineHeight = 18.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showViewDialog = null },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63))
                ) {
                    Text("Close", fontWeight = FontWeight.Bold)
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFCF8FA))
    ) {
        // Header Row
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
                text = "My Reports",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0C233A)
            )
        }

        // Filters chips row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            listOf("All", "Ready", "Pending").forEach { filter ->
                val isSelected = selectedFilter == filter
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            if (isSelected) {
                                Brush.horizontalGradient(colors = listOf(Color(0xFFBA2D81), Color(0xFFE91E63)))
                            } else {
                                Brush.horizontalGradient(colors = listOf(Color.White, Color.White))
                            }
                        )
                        .border(
                            width = if (isSelected) 0.dp else 1.dp,
                            color = if (isSelected) Color.Transparent else Color(0xFFE2E8F0),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable { selectedFilter = filter }
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = filter,
                        color = if (isSelected) Color.White else Color(0xFF8A90A6),
                        fontSize = 14.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }

        // Reports list or empty state
        if (filteredReports.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.FindInPage,
                        contentDescription = null,
                        tint = Color.LightGray,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No reports found in this category",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                items(filteredReports) { rep ->
                    val isAvailable = rep.status.equals("Available", ignoreCase = true)
                    
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(20.dp))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.Top
                            ) {
                                // Icon
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .background(Color(0xFFFFF0F5), RoundedCornerShape(12.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Description,
                                        contentDescription = null,
                                        tint = Color(0xFFE91E63),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = rep.title,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0C233A)
                                    )
                                    
                                    Spacer(modifier = Modifier.height(4.dp))

                                    // Subtitle details
                                    val prefix = if (isAvailable) "Completed" else "Test"
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "📅 $prefix: ${rep.date} - ${rep.doctorName}",
                                            fontSize = 12.sp,
                                            color = Color(0xFF8A90A6),
                                            fontWeight = FontWeight.Medium
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Badge Pill
                                    if (isAvailable) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(Color(0xFFE0F2F1))
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = null,
                                                    tint = Color(0xFF00796B),
                                                    modifier = Modifier.size(12.dp)
                                                )
                                                Text(
                                                    text = "Report Ready",
                                                    color = Color(0xFF00796B),
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    } else {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(Color(0xFFFFF3E0))
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Text(
                                                    text = "⏳ Processing...",
                                                    color = Color(0xFFE65100),
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }

                                    if (isAvailable) {
                                        Spacer(modifier = Modifier.height(14.dp))
                                        
                                        // Action buttons side-by-side
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            // View Button
                                            Box(
                                                modifier = Modifier
                                                    .height(36.dp)
                                                    .width(105.dp)
                                                    .clip(RoundedCornerShape(18.dp))
                                                    .background(Color(0xFFFFF0F5))
                                                    .clickable { showViewDialog = rep },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Visibility,
                                                        contentDescription = null,
                                                        tint = Color(0xFF00796B),
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                    Text(
                                                        text = "View",
                                                        color = Color(0xFF00796B),
                                                        fontSize = 13.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                            }

                                            // PDF Button
                                            Box(
                                                modifier = Modifier
                                                    .height(36.dp)
                                                    .width(105.dp)
                                                    .clip(RoundedCornerShape(18.dp))
                                                    .background(Color(0xFFE0F7FA))
                                                    .clickable { showPdfDialog = rep },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Download,
                                                        contentDescription = null,
                                                        tint = Color(0xFF0C233A),
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                    Text(
                                                        text = "PDF",
                                                        color = Color(0xFF0C233A),
                                                        fontSize = 13.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// 3. Health Packages Screen
@Composable
fun HealthPackagesScreen(
    viewModel: HealthViewModel,
    modifier: Modifier = Modifier
) {
    val packages by viewModel.healthPackages.collectAsStateWithLifecycle()
    val walletBal by viewModel.walletBalance.collectAsStateWithLifecycle()
    val selectedPkg by viewModel.selectedPackage.collectAsStateWithLifecycle()
    val backDispatcher = androidx.activity.compose.LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    var buySuccess by remember { mutableStateOf(false) }

    LaunchedEffect(selectedPkg) {
        if (selectedPkg != null) {
            showDialog = true
            buySuccess = false
            dialogMessage = "Would you like to purchase ${selectedPkg?.name} for ₹${String.format("%,.0f", selectedPkg?.price)} using your Health Wallet?\n\nCurrent Balance: ₹${String.format("%,.2f", walletBal)}"
        }
    }

    if (showDialog && selectedPkg != null) {
        AlertDialog(
            onDismissRequest = { 
                showDialog = false
                viewModel.setSelectedPackage(null)
            },
            title = { 
                Text(
                    text = if (buySuccess) "Purchase Successful!" else "Confirm Booking",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0C233A)
                ) 
            },
            text = { 
                Text(
                    text = dialogMessage,
                    color = Color(0xFF4A5568),
                    fontSize = 14.sp
                ) 
            },
            confirmButton = {
                if (!buySuccess) {
                    Button(
                        onClick = {
                            viewModel.buyHealthPackage(
                                selectedPkg!!,
                                onSuccess = {
                                    buySuccess = true
                                    dialogMessage = "Your package has been successfully booked. Our sample collection partner will call you in 2 hours to coordinate. A placeholder report has been added to 'Reports'!"
                                },
                                onFailure = { err ->
                                    dialogMessage = err
                                }
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Pay from Wallet", fontWeight = FontWeight.Bold)
                    }
                } else {
                    Button(
                        onClick = {
                            showDialog = false
                            viewModel.setSelectedPackage(null)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Close", fontWeight = FontWeight.Bold)
                    }
                }
            },
            dismissButton = {
                if (!buySuccess) {
                    TextButton(onClick = { 
                        showDialog = false
                        viewModel.setSelectedPackage(null)
                    }) {
                        Text("Cancel", color = Color.Gray, fontWeight = FontWeight.Medium)
                    }
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White
        )
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFCF8FA))
    ) {
        val isWide = maxWidth >= 600.dp

        Column(modifier = Modifier.fillMaxSize()) {
            // Header Row
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
                    text = "Health Packages",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0C233A)
                )
            }

            // LazyColumn for Packages
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                if (isWide) {
                    val chunkedPackages = packages.chunked(2)
                    items(chunkedPackages) { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            rowItems.forEach { pkg ->
                                Box(modifier = Modifier.weight(1f)) {
                                    HealthPackageCard(
                                        pkg = pkg,
                                        onBookClick = { viewModel.setSelectedPackage(pkg) }
                                    )
                                }
                            }
                            if (rowItems.size < 2) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                } else {
                    items(packages) { pkg ->
                        HealthPackageCard(
                            pkg = pkg,
                            onBookClick = { viewModel.setSelectedPackage(pkg) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HealthPackageCard(
    pkg: HealthPackageEntity,
    onBookClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val id = pkg.id
    val isPopular = id == "pkg_1"
    val isCardiac = id == "pkg_2"
    val isWellness = id == "pkg_3"

    // Map styling specs
    val badgeText = when {
        isPopular -> "⭐ MOST POPULAR"
        isCardiac -> "💝 CARDIAC CARE"
        isWellness -> "🌸 WOMEN'S WELLNESS"
        else -> "HEALTH PACKAGE"
    }

    val badgeBgColor = when {
        isPopular -> Color(0xFFFFF0F5)
        isCardiac -> Color(0xFFFFF3E0)
        isWellness -> Color(0xFFE8F5E9)
        else -> Color(0xFFF1F5F9)
    }

    val badgeTextColor = when {
        isPopular -> Color(0xFFE91E63)
        isCardiac -> Color(0xFFE65100)
        isWellness -> Color(0xFF2E7D32)
        else -> Color(0xFF475569)
    }

    val discountPercent = when {
        isPopular -> "24% OFF"
        isCardiac -> "21% OFF"
        isWellness -> "19% OFF"
        else -> "15% OFF"
    }

    val chips = pkg.testListString.split(",").map { it.trim() }.filter { it.isNotEmpty() }

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFFDEEF4), RoundedCornerShape(24.dp))
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Badge Pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(badgeBgColor)
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Text(
                    text = badgeText,
                    color = badgeTextColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Title
            Text(
                text = pkg.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0C233A)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Chips Layout elegantly chunked
            if (chips.isNotEmpty()) {
                val chunked = chips.chunked(2)
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    chunked.forEach { rowChips ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowChips.forEach { chip ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(Color(0xFFF1F5F9))
                                        .padding(horizontal = 14.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = chip,
                                        color = Color(0xFF64748B),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            HorizontalDivider(color = Color(0xFFF8FAFC), thickness = 1.dp)

            Spacer(modifier = Modifier.height(18.dp))

            // Price Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "₹${String.format("%,.0f", pkg.price)}",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF0C233A)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "₹${String.format("%,.0f", pkg.originalPrice)}",
                    style = MaterialTheme.typography.bodyMedium.copy(textDecoration = TextDecoration.LineThrough),
                    color = Color(0xFF94A3B8),
                    modifier = Modifier.padding(bottom = 3.dp),
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = discountPercent,
                    color = if (isCardiac) Color(0xFFE65100) else Color(0xFFE91E63),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(bottom = 3.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Book This Package Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .then(
                        when {
                            isPopular -> Modifier.background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFFBA2D81),
                                        Color(0xFFE91E63)
                                    )
                                )
                            )
                            isCardiac -> Modifier
                                .background(Color(0xFFFFFBEB))
                                .border(
                                    width = 1.dp,
                                    color = Color(0xFFFCD34D),
                                    shape = RoundedCornerShape(24.dp)
                                )
                            else -> Modifier.background(Color(0xFFF0FDF4))
                        }
                    )
                    .clickable { onBookClick() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Book This Package",
                    color = when {
                        isPopular -> Color.White
                        isCardiac -> Color(0xFFD97706)
                        else -> Color(0xFF15803D)
                    },
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// 4. Profile Screen
@Composable
fun ProfileScreen(
    viewModel: HealthViewModel,
    onLogout: () -> Unit,
    onNavigateTo: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.userProfile.collectAsStateWithLifecycle()
    val appointments by viewModel.appointments.collectAsStateWithLifecycle()
    val reports by viewModel.reports.collectAsStateWithLifecycle()

    val name = profile?.name ?: "Rajesh Kumar"
    val email = profile?.email ?: "rajesh.kumar@healthquest.com"
    val phone = profile?.phone ?: "+91 98765 43210"

    val bookingsCount = if (appointments.isEmpty()) 6 else appointments.size
    val reportsCount = if (reports.isEmpty()) 4 else reports.size
    val upcomingCount = appointments.count { it.status == "Upcoming" }.let { if (appointments.isEmpty()) 2 else it }

    // Dialog State for Edit Profile
    var showEditDialog by remember { mutableStateOf(false) }
    var editName by remember(profile) { mutableStateOf(profile?.name ?: "Rajesh Kumar") }
    var editPhone by remember(profile) { mutableStateOf(profile?.phone ?: "+91 98765 43210") }
    var editEmail by remember(profile) { mutableStateOf(profile?.email ?: "rajesh.kumar@healthquest.com") }
    var editAge by remember(profile) { mutableStateOf(profile?.age?.toString() ?: "32") }
    var editGender by remember(profile) { mutableStateOf(profile?.gender ?: "Male") }
    var editWeight by remember(profile) { mutableStateOf(profile?.weight ?: "72 kg") }
    var editHeight by remember(profile) { mutableStateOf(profile?.height ?: "176 cm") }

    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = {
                Text(
                    text = "Edit Profile",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0C233A)
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = editPhone,
                        onValueChange = { editPhone = it },
                        label = { Text("Phone") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = editEmail,
                        onValueChange = { editEmail = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = editAge,
                            onValueChange = { editAge = it },
                            label = { Text("Age") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        OutlinedTextField(
                            value = editGender,
                            onValueChange = { editGender = it },
                            label = { Text("Gender") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = editWeight,
                            onValueChange = { editWeight = it },
                            label = { Text("Weight") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        OutlinedTextField(
                            value = editHeight,
                            onValueChange = { editHeight = it },
                            label = { Text("Height") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateUserProfile(
                            UserProfileEntity(
                                id = 1,
                                name = editName,
                                email = editEmail,
                                phone = editPhone,
                                age = editAge.toIntOrNull() ?: 32,
                                gender = editGender,
                                weight = editWeight,
                                height = editHeight
                            )
                        )
                        showEditDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Save Changes", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFCF8FA))
    ) {
        // Gradient Banner / Header Block with RK Avatar
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF7B1FA2), Color(0xFFE91E63))
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(top = 28.dp, bottom = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Profile avatar: Circle with white border containing "RK"
                    Box(
                        modifier = Modifier
                            .size(84.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.White, CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        val initials = if (name.length >= 2) {
                            val parts = name.split(" ")
                            if (parts.size >= 2) {
                                "${parts[0].take(1)}${parts[1].take(1)}".uppercase()
                            } else {
                                name.take(2).uppercase()
                            }
                        } else {
                            "RK"
                        }
                        Text(
                            text = initials,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = name,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "$phone • Gurugram",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.85f),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Stats card: 6 Bookings, 4 Reports, 2 Upcoming
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .offset(y = (-16).dp) // Beautiful negative margin effect to overlap the gradient header!
            ) {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Bookings
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "$bookingsCount",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFE91E63)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Bookings",
                                fontSize = 12.sp,
                                color = Color(0xFF64748B),
                                fontWeight = FontWeight.Medium
                            )
                        }

                        // Divider 1
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(32.dp)
                                .background(Color(0xFFF1F5F9))
                        )

                        // Reports
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "$reportsCount",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFE91E63)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Reports",
                                fontSize = 12.sp,
                                color = Color(0xFF64748B),
                                fontWeight = FontWeight.Medium
                            )
                        }

                        // Divider 2
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(32.dp)
                                .background(Color(0xFFF1F5F9))
                        )

                        // Upcoming
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "$upcomingCount",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFE91E63)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Upcoming",
                                fontSize = 12.sp,
                                color = Color(0xFF64748B),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        // Settings items Card containing: Edit Profile, Appointment History, My Wallet, Privacy & Security
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFFDEEF4), RoundedCornerShape(24.dp))
                ) {
                    Column {
                        ProfileListItem(
                            title = "Edit Profile",
                            subtitle = "Name, address, DOB",
                            icon = Icons.Default.Person,
                            onClick = { showEditDialog = true }
                        )
                        HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)
                        ProfileListItem(
                            title = "Appointment History",
                            subtitle = "All past & upcoming",
                            icon = Icons.Default.DateRange,
                            onClick = { onNavigateTo(Screen.Home.route) }
                        )
                        HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)
                        ProfileListItem(
                            title = "My Wallet",
                            subtitle = "Balance, payments, cashback",
                            icon = Icons.Default.Favorite,
                            onClick = { onNavigateTo(Screen.Wallet.route) }
                        )
                        HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)
                        ProfileListItem(
                            title = "Privacy & Security",
                            subtitle = "Data, permissions",
                            icon = Icons.Default.Shield,
                            onClick = { /* Security action */ }
                        )
                    }
                }

                // Help & Support is in its own Card
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFFDEEF4), RoundedCornerShape(24.dp))
                ) {
                    ProfileListItem(
                        title = "Help & Support",
                        subtitle = "WhatsApp, call, FAQs",
                        icon = Icons.Default.ChatBubble,
                        onClick = { onNavigateTo(Screen.Support.route) }
                    )
                }

                // Sign Out Button styled as light pink background with thin border and red text
                Button(
                    onClick = {
                        viewModel.logout()
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFF5F5),
                        contentColor = Color(0xFFE53E3E)
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFED7D7)),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text(
                        text = "Sign Out",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileListItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFFFF0F5), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFFE91E63),
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0C233A)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Color(0xFF94A3B8)
                )
            }
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Color(0xFF94A3B8),
            modifier = Modifier.size(18.dp)
        )
    }
}
