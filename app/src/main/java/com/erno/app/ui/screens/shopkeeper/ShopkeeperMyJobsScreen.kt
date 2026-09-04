package com.erno.app.ui.screens.shopkeeper

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erno.app.ui.components.ShopkeeperBottomNavigation
import com.erno.app.ui.components.ShopkeeperTab
import com.erno.app.ui.theme.ErnoGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopkeeperMyJobsScreen(
    state: ShopkeeperState,
    onBackClick: () -> Unit,
    onNavigateTab: (ShopkeeperTab) -> Unit
) {
    var selectedTabIndex by remember { mutableIntStateOf(1) } // 0: All, 1: Active, 2: Completed

    val activeJobs = state.recentJobs.filter { it.status == "Active" }
    val completedJobs = state.recentJobs.filter { it.status == "Completed" }

    val filteredJobs = when (selectedTabIndex) {
        0 -> state.recentJobs
        1 -> activeJobs
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
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            ShopkeeperBottomNavigation(
                selectedTab = ShopkeeperTab.JOBS,
                onTabSelected = onNavigateTab
            )
        },
        containerColor = Color(0xFFF9FAFB)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Tabs Row
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.White,
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
                            text = "All (${state.recentJobs.size})",
                            fontSize = 14.sp,
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
                            text = "Active (${activeJobs.size})",
                            fontSize = 14.sp,
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
                            fontSize = 14.sp,
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
                        text = "No jobs found",
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
                        JobCardItem(job = job)
                    }
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}
