package com.erno.app.ui.screens.shopkeeper

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
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
import com.erno.app.ui.theme.ErnoTextSecondary

data class ApplicationItem(
    val id: String,
    val workerName: String,
    val jobTitle: String,
    val experience: String,
    val distance: String,
    val rating: String,
    var status: String // "Pending", "Accepted", "Declined"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopkeeperApplicationsScreen(
    onBackClick: () -> Unit,
    onNavigateTab: (ShopkeeperTab) -> Unit
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) } // 0: All, 1: Pending, 2: Accepted

    var applicationsList by remember {
        mutableStateOf(
            listOf(
                ApplicationItem("1", "Amit Kumar", "Shop Helper", "2 yrs exp", "1.2 km away", "4.8", "Pending"),
                ApplicationItem("2", "Ramesh Singh", "Shop Helper", "3 yrs exp", "0.8 km away", "4.9", "Pending"),
                ApplicationItem("3", "Sunil Verma", "Counter Staff", "1 yr exp", "2.1 km away", "4.6", "Pending"),
                ApplicationItem("4", "Vikram Patel", "Counter Staff", "4 yrs exp", "1.5 km away", "5.0", "Accepted"),
                ApplicationItem("5", "Pooja Sharma", "Store Assistant", "2 yrs exp", "1.0 km away", "4.7", "Accepted")
            )
        )
    }

    val pendingApps = applicationsList.filter { it.status == "Pending" }
    val acceptedApps = applicationsList.filter { it.status == "Accepted" }

    val filteredList = when (selectedTabIndex) {
        0 -> applicationsList
        1 -> pendingApps
        else -> acceptedApps
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Applications",
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
                selectedTab = ShopkeeperTab.APPLICATIONS,
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
            // Tab Row
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
                            text = "All (${applicationsList.size})",
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
                            text = "Pending (${pendingApps.size})",
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
                            text = "Accepted (${acceptedApps.size})",
                            fontSize = 14.sp,
                            fontWeight = if (selectedTabIndex == 2) FontWeight.Bold else FontWeight.Medium,
                            color = if (selectedTabIndex == 2) ErnoGreen else Color.Gray
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (filteredList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No applications found",
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
                    items(filteredList) { app ->
                        ApplicationCardItem(
                            application = app,
                            onAccept = {
                                applicationsList = applicationsList.map {
                                    if (it.id == app.id) it.copy(status = "Accepted") else it
                                }
                            },
                            onDecline = {
                                applicationsList = applicationsList.filter { it.id != app.id }
                            }
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
fun ApplicationCardItem(
    application: ApplicationItem,
    onAccept: () -> Unit,
    onDecline: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color(0xFFF1F8E9), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = ErnoGreen,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = application.workerName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            text = "Applied for: ${application.jobTitle}",
                            fontSize = 12.sp,
                            color = ErnoTextSecondary
                        )
                    }
                }

                if (application.status == "Accepted") {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFE8F5E9)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color(0xFF2E7D32),
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Accepted",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = application.experience,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = "•",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = application.distance,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = "•",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFB300),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = application.rating,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (application.status == "Pending") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDecline,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, Color.LightGray)
                    ) {
                        Text(
                            text = "Decline",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }

                    Button(
                        onClick = onAccept,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ErnoGreen),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "Accept Worker",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            } else {
                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0E1726)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Call Worker",
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}
