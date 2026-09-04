package com.erno.app.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erno.app.ui.theme.ErnoGreen

enum class WorkerTab {
    HOME, MY_JOBS, EARNINGS, PROFILE
}

@Composable
fun WorkerBottomNavigation(
    selectedTab: WorkerTab,
    onTabSelected: (WorkerTab) -> Unit,
    containerColor: Color = Color.White,
    contentColor: Color = ErnoGreen
) {
    NavigationBar(
        containerColor = containerColor,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = selectedTab == WorkerTab.HOME,
            onClick = { onTabSelected(WorkerTab.HOME) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = { Text("Home", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = contentColor,
                selectedTextColor = contentColor,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            selected = selectedTab == WorkerTab.MY_JOBS,
            onClick = { onTabSelected(WorkerTab.MY_JOBS) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Work,
                    contentDescription = "Jobs",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = { Text("My Jobs", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = contentColor,
                selectedTextColor = contentColor,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            selected = selectedTab == WorkerTab.EARNINGS,
            onClick = { onTabSelected(WorkerTab.EARNINGS) },
            icon = {
                Icon(
                    imageVector = Icons.Default.AccountBalanceWallet,
                    contentDescription = "Earnings",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = { Text("Earnings", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = contentColor,
                selectedTextColor = contentColor,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            selected = selectedTab == WorkerTab.PROFILE,
            onClick = { onTabSelected(WorkerTab.PROFILE) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = { Text("Profile", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = contentColor,
                selectedTextColor = contentColor,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )
    }
}
