package com.erno.app.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PhonelinkLock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erno.app.ui.theme.ErnoGreen
import com.erno.app.ui.theme.ErnoTextSecondary
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyOTPScreen(
    phoneNumber: String,
    onBackClick: () -> Unit,
    onVerifyClick: (String) -> Unit,
    onEditClick: () -> Unit,
    onResendClick: () -> Unit
) {
    var otpValues by remember { mutableStateOf(List(6) { "" }) }
    val focusRequesters = remember { List(6) { FocusRequester() } }
    var timerSeconds by remember { mutableIntStateOf(30) }

    // Dynamic Countdown Timer
    LaunchedEffect(timerSeconds) {
        if (timerSeconds > 0) {
            delay(1000L)
            timerSeconds--
        }
    }

    val formattedPhoneNumber = remember(phoneNumber) {
        when {
            phoneNumber.startsWith("+") -> phoneNumber
            phoneNumber.length == 10 -> "+91 $phoneNumber"
            else -> phoneNumber
        }
    }

    val isOtpComplete = otpValues.all { it.isNotBlank() } && otpValues.joinToString("").length == 6

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Header with Back Button and Title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
                Text(
                    text = "Verify OTP",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.width(48.dp))
            }

            // Illustration Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .background(Color(0xFFF1F8E9), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PhonelinkLock,
                        contentDescription = null,
                        modifier = Modifier.size(70.dp),
                        tint = ErnoGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main OTP Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(24.dp),
                color = Color.White,
                shadowElevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Enter OTP",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Enter the 6-digit OTP sent to",
                        fontSize = 14.sp,
                        color = ErnoTextSecondary
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = formattedPhoneNumber,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Edit",
                            color = ErnoGreen,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { onEditClick() }
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // OTP Input Fields with Auto-Advance Focus
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        otpValues.forEachIndexed { index, value ->
                            OutlinedTextField(
                                value = value,
                                onValueChange = { newValue ->
                                    if (newValue.length <= 1) {
                                        val newList = otpValues.toMutableList()
                                        newList[index] = newValue
                                        otpValues = newList

                                        // Auto advance focus to next box
                                        if (newValue.isNotEmpty() && index < 5) {
                                            focusRequesters[index + 1].requestFocus()
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .focusRequester(focusRequesters[index])
                                    .onKeyEvent { keyEvent ->
                                        if (keyEvent.type == KeyEventType.KeyUp && keyEvent.key == Key.Backspace) {
                                            if (value.isEmpty() && index > 0) {
                                                focusRequesters[index - 1].requestFocus()
                                                true
                                            } else false
                                        } else false
                                    },
                                textStyle = LocalTextStyle.current.copy(
                                    color = Color.Black,
                                    textAlign = TextAlign.Center,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.Black,
                                    focusedBorderColor = ErnoGreen,
                                    unfocusedBorderColor = Color(0xFFD0D0D0)
                                ),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    if (timerSeconds > 0) {
                        Text(
                            text = buildAnnotatedString {
                                append("Resend OTP in ")
                                withStyle(SpanStyle(color = ErnoGreen, fontWeight = FontWeight.Bold)) {
                                    append("00:${timerSeconds.toString().padStart(2, '0')}")
                                }
                            },
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    } else {
                        Text(
                            text = "OTP expired. You can resend now.",
                            fontSize = 14.sp,
                            color = Color.Red
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Verify Button
                    Button(
                        onClick = { onVerifyClick(otpValues.joinToString("")) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ErnoGreen),
                        shape = RoundedCornerShape(12.dp),
                        enabled = isOtpComplete
                    ) {
                        Text(
                            text = "Verify OTP",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row {
                        Text(
                            text = "Didn't receive OTP? ",
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                        Text(
                            text = "Resend OTP",
                            color = if (timerSeconds == 0) ErnoGreen else Color.Gray,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable(enabled = timerSeconds == 0) {
                                timerSeconds = 30
                                otpValues = List(6) { "" }
                                onResendClick()
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Bottom Safety Banner
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFF1F8E9)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color.White, CircleShape)
                            .border(1.dp, ErnoGreen, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = ErnoGreen,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Your number is safe with us",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            text = "We never share your number with anyone.\nIt is used only for verification purposes.",
                            fontSize = 11.sp,
                            color = Color.Gray,
                            lineHeight = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}
