package com.erno.app.ui.screens.worker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erno.app.data.model.WorkerJob
import com.erno.app.ui.components.WorkerBottomNavigation
import com.erno.app.ui.components.WorkerTab
import com.erno.app.ui.theme.ErnoGreen
import com.erno.app.ui.theme.ErnoTextSecondary

@Composable
fun WorkerHomeScreen(
    state: WorkerState,
    onJobSelect: (WorkerJob) -> Unit,
    onNavigateTab: (WorkerTab) -> Unit
) {
    Scaffold(
        bottomBar = {
            WorkerBottomNavigation(
                selectedTab = WorkerTab.HOME,
                onTabSelected = onNavigateTab,
                containerColor = Color(0xFF0E1726),
                contentColor = ErnoGreen
            )
        },
        containerColor = Color(0xFF0B121F) // Dark Navy theme as seen in bottom row
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header: Greeting & Notification
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Hello, ${state.workerName} 👋",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Find work near you",
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }

                    IconButton(
                        onClick = { },
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFF1B2738), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsNone,
                            contentDescription = "Notifications",
                            tint = Color.White
                        )
                    }
                }
            }

            // Location Picker Pill
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF182436),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = ErnoGreen,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = state.currentLocation,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            }

            // Section Header
            item {
                Text(
                    text = "Available Jobs Near You",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Available Job Cards
            items(state.availableJobs) { job ->
                WorkerAvailableJobCard(
                    job = job,
                    onClick = { onJobSelect(job) }
                )
            }
        }
    }
}

@Composable
fun WorkerAvailableJobCard(
    job: WorkerJob,
    onClick: () -> Unit
) {
    val minPrice = job.payStructure.minOfOrNull { it.price } ?: 99
    val maxPrice = job.payStructure.maxOfOrNull { it.price } ?: 629
    val minHours = job.payStructure.minOfOrNull { it.hours } ?: 1
    val maxHours = job.payStructure.maxOfOrNull { it.hours } ?: 8

    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = job.title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                if (job.isNew) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFEAF5D8)
                    ) {
                        Text(
                            text = "New",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ErnoGreen,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = job.location,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
                if (job.distance.isNotBlank()) {
                    Text(
                        text = " • ${job.distance}",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "🕒 $minHours - $maxHours Hours",
                        fontSize = 13.sp,
                        color = Color.DarkGray
                    )
                }

                Text(
                    text = "₹$minPrice - ₹$maxPrice",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}
