package com.erno.app.ui.screens.worker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erno.app.data.model.EarningRecord
import com.erno.app.ui.components.WorkerBottomNavigation
import com.erno.app.ui.components.WorkerTab
import com.erno.app.ui.theme.ErnoGreen
import com.erno.app.ui.theme.ErnoTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerEarningsScreen(
    state: WorkerState,
    onBackClick: () -> Unit,
    onNavigateTab: (WorkerTab) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Earnings",
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
                selectedTab = WorkerTab.EARNINGS,
                onTabSelected = onNavigateTab,
                containerColor = Color(0xFF0E1726),
                contentColor = ErnoGreen
            )
        },
        containerColor = Color(0xFF0B121F)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Total Earnings Banner Card
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Total Earnings",
                            fontSize = 13.sp,
                            color = ErnoTextSecondary
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "₹${state.totalEarnings}",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "This Month",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            // Section Header
            item {
                Text(
                    text = "Recent Earnings",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // List of Earnings Records
            items(state.earningsHistory) { record ->
                EarningRecordItem(record = record)
            }
        }
    }
}

@Composable
fun EarningRecordItem(record: EarningRecord) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = record.jobTitle,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = record.hours,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "₹${record.amount}",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = ErnoGreen
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = record.date,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
