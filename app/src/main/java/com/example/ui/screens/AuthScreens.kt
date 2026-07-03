package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.HQGradientButton
import com.example.ui.components.HQTextField
import com.example.ui.theme.*
import com.example.viewmodel.HealthViewModel
import kotlinx.coroutines.delay
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.icons.automirrored.filled.ArrowForward

// 1. Splash Screen
@Composable
fun SplashScreen(
    onTimeout: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        delay(2000)
        onTimeout()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(GradientPinkStart, GradientPurpleEnd))),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            // Elegant medical shield or emblem with cross icon
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.LocalHospital,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(56.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "HEALTH QUEST",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Integrity • Diagnostics • Compassion • Growth",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.8f),
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        }
    }
}

// 2. Welcome Screen
@Composable
fun WelcomeScreen(
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    var progress by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        val durationMs = 3000L
        val stepMs = 30L
        val totalSteps = durationMs / stepMs
        for (step in 1..totalSteps) {
            delay(stepMs)
            progress = step.toFloat() / totalSteps
        }
        onNext()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF7B1FA2), Color(0xFFE91E63))))
            .clickable { onNext() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // Central White Card with Logo
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .background(Color.White, shape = RoundedCornerShape(24.dp))
                    .padding(vertical = 32.dp, horizontal = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Logo Graphic (HQ with diagnostic imaging / MRI circle)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "H",
                            style = MaterialTheme.typography.displayMedium.copy(
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF7B1FA2)
                            )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        androidx.compose.foundation.Canvas(
                            modifier = Modifier
                                .size(44.dp)
                                .padding(top = 4.dp)
                        ) {
                            val strokeWidth = 5.dp.toPx()
                            // Outer scanner circle for Q
                            drawArc(
                                color = Color(0xFFE91E63),
                                startAngle = -45f,
                                sweepAngle = 270f,
                                useCenter = false,
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                            )
                            // Inner concentric ring detail
                            drawCircle(
                                color = Color(0xFF7B1FA2),
                                radius = size.minDimension / 4f,
                                style = Stroke(width = 2.dp.toPx())
                            )
                            // Scanning scanner dot
                            drawCircle(
                                color = Color(0xFFE91E63),
                                radius = size.minDimension / 8f
                            )
                            // Q bottom right accent/sliding table
                            drawLine(
                                color = Color(0xFFE91E63),
                                start = Offset(size.width * 0.5f, size.height * 0.5f),
                                end = Offset(size.width * 1.0f, size.height * 1.0f),
                                strokeWidth = strokeWidth,
                                cap = StrokeCap.Round
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row {
                        Text(
                            text = "HEALTH",
                            color = Color(0xFF7B1FA2),
                            fontWeight = FontWeight.ExtraBold,
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp, letterSpacing = 1.sp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "QUEST",
                            color = Color(0xFFE91E63),
                            fontWeight = FontWeight.ExtraBold,
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp, letterSpacing = 1.sp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Subheadings: Imaging & Diagnostics / Gurugram's Trusted Chain
            Text(
                text = "Imaging & Diagnostics",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Gurugram's Trusted Chain",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.9f)
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Dots indicators (one pill active, one dot inactive)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .width(24.dp)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.White)
                )
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.5f))
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Linear Progress Bar matching image.png
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .height(5.dp)
                    .clip(RoundedCornerShape(2.5.dp))
                    .background(Color.White.copy(alpha = 0.3f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(progress)
                        .background(Color.White)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Loading your experience...",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Medium
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

// 3. Get Started Screen
@Composable
fun GetStartedScreen(
    onGetStarted: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Upper half with soft pink background and translucent visual accent circles
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.1f)
                .background(Color(0xFFFFF0F6))
        ) {
            androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                // Soft background circle - top right
                drawCircle(
                    color = Color(0xFFFCE4EC).copy(alpha = 0.5f),
                    radius = size.minDimension * 0.45f,
                    center = Offset(size.width * 0.9f, size.height * 0.2f)
                )
                // Soft background circle - bottom left
                drawCircle(
                    color = Color(0xFFFCE4EC).copy(alpha = 0.6f),
                    radius = size.minDimension * 0.55f,
                    center = Offset(size.width * 0.1f, size.height * 0.8f)
                )
            }

            // High-fidelity replicated avatar/profile card centered in the upper section
            Box(
                modifier = Modifier.align(Alignment.Center)
            ) {
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(RoundedCornerShape(36.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF9C27B0), // Deep Purple
                                    Color(0xFFE91E63)  // Vibrant Pink
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    // White rounded container
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            // Avatar head
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE91E63))
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            // Avatar shoulders/base
                            Box(
                                modifier = Modifier
                                    .width(38.dp)
                                    .height(14.dp)
                                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                    .background(Color(0xFFE91E63))
                            )
                        }
                    }
                }
            }
        }

        // Lower half with clean white background, title, description, indicators, and get started button
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.2f)
                .background(Color.White)
                .padding(horizontal = 32.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Title
                Text(
                    text = "Book Tests &\nTrack Your Health",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0C233A), // Match the exact dark navy in image.png
                        lineHeight = 36.sp
                    )
                )

                // Subtitle / Description text
                Text(
                    text = "Schedule CT scans, X-rays,\nUltrasounds & more from home.\nAccess reports instantly, anytime.",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 15.sp,
                        color = Color(0xFF6B7E90), // Match the elegant slate blue-gray
                        lineHeight = 22.sp
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Page indicator bars
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Page 1: Active (Pink/Magenta)
                    Box(
                        modifier = Modifier
                            .width(64.dp)
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color(0xFFE91E63))
                    )
                    // Page 2: Inactive (Light Lavender-Gray)
                    Box(
                        modifier = Modifier
                            .width(64.dp)
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color(0xFFECEFF1))
                    )
                    // Page 3: Inactive (Light Lavender-Gray)
                    Box(
                        modifier = Modifier
                            .width(64.dp)
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color(0xFFECEFF1))
                    )
                }
            }

            // Bottom CTA section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Beautiful gradient primary action button
                Button(
                    onClick = onGetStarted,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFF9C27B0), // Elegant violet/purple
                                        Color(0xFFE91E63)  // Vibrant magenta/pink
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Get Started →",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                // Inline Already have an account? Sign In text link
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Already have an account? ",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFF8A90A6)
                        )
                    )
                    Text(
                        text = "Sign In",
                        modifier = Modifier.clickable { onGetStarted() },
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFFE91E63),
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

// Support Contact Dialog
@Composable
fun SupportContactDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.LocalHospital,
                    contentDescription = null,
                    tint = PrimaryLight,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("HQ Support Desk", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    "Our specialists are available 24/7 to help you with OTP verification, report tracking, bookings, or wallet queries.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray
                )
                Divider(color = Color.LightGray.copy(alpha = 0.5f))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Phone, contentDescription = null, tint = PrimaryLight, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Toll-Free Helpline", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                        Text("1800-123-4567 (Tap to Mock Call)", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Healing, contentDescription = null, tint = PrimaryLight, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("WhatsApp Chat Assist", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                        Text("+91 98765 43210 (Mock Chat Active)", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = PrimaryLight, fontWeight = FontWeight.Bold)
            }
        },
        shape = RoundedCornerShape(16.dp),
        containerColor = Color.White
    )
}

// Beautiful OTP Input custom visual container
@Composable
fun OtpInputRow(
    otp: String,
    onOtpChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val maxChar = 4
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.foundation.text.BasicTextField(
            value = otp,
            onValueChange = {
                if (it.length <= maxChar && it.all { char -> char.isDigit() }) {
                    onOtpChange(it)
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            decorationBox = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 0 until maxChar) {
                        val char = otp.getOrNull(i)
                        val isFocused = otp.length == i
                        val boxColor = if (isFocused) PrimaryLight else Color.LightGray
                        val boxBg = if (isFocused) PrimaryLight.copy(alpha = 0.05f) else Color.White
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(boxBg)
                                .border(
                                    width = if (isFocused) 2.dp else 1.dp,
                                    color = boxColor,
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = char?.toString() ?: "",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = OnBackgroundLight,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        )
    }
}

// 4. Sign In Screen (with dynamic OTP support & interactive state toggles)
@Composable
fun GoogleLogo(modifier: Modifier = Modifier) {
    androidx.compose.foundation.Canvas(modifier = modifier.size(20.dp)) {
        val sizePx = size.width
        val strokeWidth = sizePx * 0.22f
        val innerSize = sizePx - strokeWidth
        val offset = strokeWidth / 2f
        
        // 1. Red (Top segment: starts at 180 and sweeps 105)
        drawArc(
            color = Color(0xFFEA4335),
            startAngle = 180f,
            sweepAngle = 105f,
            useCenter = false,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Butt),
            topLeft = Offset(offset, offset),
            size = androidx.compose.ui.geometry.Size(innerSize, innerSize)
        )
        // 2. Yellow (Left segment: starts at 120 and sweeps 60)
        drawArc(
            color = Color(0xFFFBBC05),
            startAngle = 120f,
            sweepAngle = 60f,
            useCenter = false,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Butt),
            topLeft = Offset(offset, offset),
            size = androidx.compose.ui.geometry.Size(innerSize, innerSize)
        )
        // 3. Green (Bottom segment: starts at 0 and sweeps 120)
        drawArc(
            color = Color(0xFF34A853),
            startAngle = 0f,
            sweepAngle = 120f,
            useCenter = false,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Butt),
            topLeft = Offset(offset, offset),
            size = androidx.compose.ui.geometry.Size(innerSize, innerSize)
        )
        // 4. Blue (Right segment: starts at -75 and sweeps 75)
        drawArc(
            color = Color(0xFF4285F4),
            startAngle = -75f,
            sweepAngle = 75f,
            useCenter = false,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Butt),
            topLeft = Offset(offset, offset),
            size = androidx.compose.ui.geometry.Size(innerSize, innerSize)
        )
        
        // Horizontal bar
        val barY = sizePx / 2f
        drawLine(
            color = Color(0xFF4285F4),
            start = Offset(sizePx / 2f, barY),
            end = Offset(sizePx - strokeWidth / 2f, barY),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Square
        )
    }
}

@Composable
fun MobileNumberField(
    value: String,
    onValueChange: (String) -> Unit,
    onSendOtp: () -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.foundation.text.BasicTextField(
        value = value,
        onValueChange = { input ->
            if (input.length <= 10 && input.all { it.isDigit() }) {
                onValueChange(input)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF0C233A),
            fontSize = 16.sp
        ),
        modifier = modifier.fillMaxWidth(),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(Color(0xFFFFF5F8), shape = RoundedCornerShape(16.dp))
                    .border(
                        width = 1.5.dp,
                        color = Color(0xFFF48FB1),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = null,
                    tint = Color(0xFFE91E63),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "+91 ",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF0C233A),
                        fontSize = 16.sp
                    )
                )
                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        Text(
                            text = "Enter mobile number",
                            color = Color.Gray.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp)
                        )
                    }
                    innerTextField()
                }
                
                if (value.length == 10) {
                    IconButton(
                        onClick = onSendOtp,
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFF9C27B0),
                                        Color(0xFFE91E63)
                                    )
                                ),
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Send OTP",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun CustomOtpRow(
    otp: String,
    onOtpChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val maxChar = 4
    val focusRequester = remember { FocusRequester() }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                focusRequester.requestFocus()
            }
    ) {
        // Transparent BasicTextField over everything
        androidx.compose.foundation.text.BasicTextField(
            value = otp,
            onValueChange = { input ->
                if (input.length <= maxChar && input.all { it.isDigit() }) {
                    onOtpChange(input)
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .focusRequester(focusRequester)
                .alpha(0.01f) // Hidden from view but fully focusable/clickable
        )

        // Visual boxes row underneath
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            for (i in 0 until maxChar) {
                val char = otp.getOrNull(i)
                val isFocused = otp.length == i || (otp.length == maxChar && i == maxChar - 1)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                        .background(Color(0xFFFFF5F8), shape = RoundedCornerShape(16.dp))
                        .border(
                            width = if (isFocused) 2.dp else 1.5.dp,
                            color = if (isFocused) Color(0xFFE91E63) else Color(0xFFF48FB1),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = char?.toString() ?: "",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0C233A)
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun SignInScreen(
    onSignInSuccess: () -> Unit,
    viewModel: HealthViewModel,
    modifier: Modifier = Modifier
) {
    var phone by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var isOtpRequested by remember { mutableStateOf(false) }
    var timerSeconds by remember { mutableStateOf(0) }
    var showSupportDialog by remember { mutableStateOf(false) }
    var successMsg by remember { mutableStateOf("") }

    // Reset all UI state when the Sign In screen is opened
    LaunchedEffect(Unit) {
        phone = ""
        otp = ""
        isOtpRequested = false
        timerSeconds = 0
        successMsg = ""
    }

    // Resend OTP countdown starts only after OTP is requested and timer is active
    LaunchedEffect(isOtpRequested, timerSeconds) {
        if (isOtpRequested && timerSeconds > 0) {
            delay(1000L)
            timerSeconds--
        }
    }

    LaunchedEffect(successMsg) {
        if (successMsg.isNotEmpty()) {
            delay(3000L)
            successMsg = ""
        }
    }

    if (showSupportDialog) {
        SupportContactDialog(onDismiss = { showSupportDialog = false })
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Upper half with elegant purple/magenta gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF7B1FA2), // Deep Purple
                            Color(0xFFE91E63)  // Vibrant Pink
                        )
                    )
                )
        ) {
            // Translucent glowing circle decoration
            androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = Color.White.copy(alpha = 0.08f),
                    radius = size.minDimension * 0.5f,
                    center = Offset(size.width * 0.95f, size.height * 0.5f)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp, vertical = 24.dp)
                    .statusBarsPadding(),
                verticalArrangement = Arrangement.Bottom
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Welcome Back",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color.White.copy(alpha = 0.9f),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "👋",
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Sign In to\nHealth Quest",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 32.sp,
                        lineHeight = 38.sp
                    )
                )
            }
        }

        // Form Section with precise styling
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.8f)
                .background(Color.White)
                .padding(horizontal = 32.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (successMsg.isNotEmpty()) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Healing,
                            contentDescription = null,
                            tint = Color(0xFF2E7D32),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = successMsg,
                            color = Color(0xFF2E7D32),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // MOBILE NUMBER Label
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "MOBILE NUMBER",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1E3A5F),
                        letterSpacing = 0.5.sp
                    )
                )
                MobileNumberField(
                    value = phone,
                    onValueChange = { phone = it },
                    onSendOtp = {
                        timerSeconds = 30
                        isOtpRequested = true
                        otp = "" // start empty, wait for user manual entry or simulated arrival
                        successMsg = "OTP sent successfully to +91 $phone"
                    }
                )
            }

            // ENTER OTP Label
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "ENTER OTP",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1E3A5F),
                        letterSpacing = 0.5.sp
                    )
                )
                CustomOtpRow(
                    otp = otp,
                    onOtpChange = { otp = it }
                )
            }

            // Resend OTP Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isOtpRequested && timerSeconds > 0) "Resend OTP in " else "Didn't receive? ",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = if (isOtpRequested) Color(0xFF8A90A6) else Color(0xFF8A90A6).copy(alpha = 0.5f)
                    )
                )
                if (isOtpRequested) {
                    if (timerSeconds > 0) {
                        Text(
                            text = "0:${if (timerSeconds < 10) "0" else ""}$timerSeconds",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color(0xFFE91E63),
                                fontWeight = FontWeight.Bold
                            )
                        )
                    } else {
                        Text(
                            text = "Resend OTP",
                            modifier = Modifier.clickable {
                                timerSeconds = 30
                                otp = "" // keep clean
                                successMsg = "OTP resent successfully to +91 $phone"
                            },
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color(0xFFE91E63),
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                } else {
                    Text(
                        text = "Resend OTP",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFFE91E63).copy(alpha = 0.5f),
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Verify & Login CTA
            Button(
                onClick = {
                    if (phone.length == 10) {
                        if (!isOtpRequested) {
                            // Fluid UX: Automatically trigger send OTP if they click submit and haven't sent yet
                            timerSeconds = 30
                            isOtpRequested = true
                            otp = ""
                            successMsg = "OTP sent successfully to +91 $phone"
                        } else {
                            if (otp.length == 4) {
                                viewModel.login(phone)
                                onSignInSuccess()
                            } else {
                                successMsg = "Please enter the 4-digit OTP."
                            }
                        }
                    } else {
                        successMsg = "Please enter a valid 10-digit mobile number."
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(),
                shape = RoundedCornerShape(18.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF9C27B0), // Elegant violet/purple
                                    Color(0xFFE91E63)  // Vibrant magenta/pink
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Verify & Login",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            // Divider or
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Divider(
                    modifier = Modifier.weight(1f),
                    color = Color.LightGray.copy(alpha = 0.5f)
                )
                Text(
                    text = "or",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF8A90A6)
                    )
                )
                Divider(
                    modifier = Modifier.weight(1f),
                    color = Color.LightGray.copy(alpha = 0.5f)
                )
            }

            // Google Sign In CTA
            Button(
                onClick = {
                    viewModel.login("google_user")
                    onSignInSuccess()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .border(
                        width = 1.dp,
                        color = Color(0xFFCFD8DC),
                        shape = RoundedCornerShape(18.dp)
                    ),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(18.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    GoogleLogo()
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Continue with Google",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color(0xFF0C233A),
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Footer Support Text
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Need help? Contact Support",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF8A90A6),
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.clickable { showSupportDialog = true }
                )
            }
        }
    }
}
