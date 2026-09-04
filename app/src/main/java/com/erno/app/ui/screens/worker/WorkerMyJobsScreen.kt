package com.erno.app.ui.screens.worker

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erno.app.data.model.WorkerJob
import com.erno.app.ui.components.WorkerBottomNavigation
import com.erno.app.ui.components.WorkerLocationMapView
import com.erno.app.ui.components.WorkerTab
import com.erno.app.ui.theme.ErnoGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerMyJobsScreen(
    state: WorkerState,
    onBackClick: () -> Unit,
    onNavigateTab: (WorkerTab) -> Unit,
    onJobClick: (WorkerJob) -> Unit = {},
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) } // 0: Upcoming, 1: Ongoing, 2: Completed

    val upcomingJobs = state.myJobs.filter { it.status == "Upcoming" }
    val ongoingJobs = state.myJobs.filter { it.status == "Ongoing" }
    val completedJobs = state.myJobs.filter { it.status == "Completed" }

    val filteredJobs = when (selectedTabIndex) {
        0 -> upcomingJobs
        1 -> ongoingJobs
        else -> completedJobs
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Jobs",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0B121F))
            )
        },
        bottomBar = {
            WorkerBottomNavigation(
                selectedTab = WorkerTab.MY_JOBS,
                onTabSelected = onNavigateTab,
                containerColor = Color(0xFF0E1726),
                contentColor = ErnoGreen
            )
        },
        containerColor = Color(0xFF0B121F)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Tab Row
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color(0xFF0B121F),
                contentColor = ErnoGreen,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = ErnoGreen
                    )
                }
            ) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    text = {
                        Text(
                            text = "Upcoming (${upcomingJobs.size})",
                            fontSize = 13.sp,
                            fontWeight = if (selectedTabIndex == 0) FontWeight.Bold else FontWeight.Medium,
                            color = if (selectedTabIndex == 0) ErnoGreen else Color.Gray
                        )
                    }
                )
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = {
                        Text(
                            text = "Ongoing (${ongoingJobs.size})",
                            fontSize = 13.sp,
                            fontWeight = if (selectedTabIndex == 1) FontWeight.Bold else FontWeight.Medium,
                            color = if (selectedTabIndex == 1) ErnoGreen else Color.Gray
                        )
                    }
                )
                Tab(
                    selected = selectedTabIndex == 2,
                    onClick = { selectedTabIndex = 2 },
                    text = {
                        Text(
                            text = "Completed (${completedJobs.size})",
                            fontSize = 13.sp,
                            fontWeight = if (selectedTabIndex == 2) FontWeight.Bold else FontWeight.Medium,
                            color = if (selectedTabIndex == 2) ErnoGreen else Color.Gray
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (filteredJobs.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No jobs in this section",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredJobs) { job ->
                        WorkerMyJobCardItem(
                            job = job,
                            onJobClick = { onJobClick(job) }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun WorkerMyJobCardItem(
    job: WorkerJob,
    onJobClick: () -> Unit = {}
) {
    var isMapExpanded by remember { mutableStateOf((job.status == "Upcoming") || (job.status == "Ongoing")) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onJobClick() },
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

                when (job.status) {
                    "Upcoming" -> {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFEAF5D8)
                        ) {
                            Text(
                                text = "Upcoming",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = ErnoGreen,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                    "Ongoing" -> {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFFFF3E0)
                        ) {
                            Text(
                                text = "Ongoing",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFE65100),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                    "Completed" -> {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFE8F5E9)
                        ) {
                            Text(
                                text = "Completed",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
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
                    text = "${job.location} • ${job.distance}",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🕒 ${job.selectedHours} Hours",
                    fontSize = 13.sp,
                    color = Color.DarkGray
                )

                Text(
                    text = "₹${job.selectedPay}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            if (job.status == "Upcoming" || job.status == "Ongoing") {
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = Color(0xFFF0F0F0))
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🗓️ ${job.scheduledTime}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { isMapExpanded = !isMapExpanded }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Directions,
                            contentDescription = null,
                            tint = ErnoGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isMapExpanded) "Hide Map" else "Shop Map",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = ErnoGreen
                        )
                    }
                }

                // Expandable Map View showing Shop Direction and Route
                AnimatedVisibility(
                    visible = isMapExpanded,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(12.dp))
                        WorkerLocationMapView(
                            locationName = job.location,
                            distanceText = job.distance,
                            latitude = job.latitude,
                            longitude = job.longitude,
                            isDarkTheme = true
                        )
                    }
                }
            }
        }
    }
}
