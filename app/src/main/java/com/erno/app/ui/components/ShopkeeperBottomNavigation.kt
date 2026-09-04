package com.erno.app.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
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

enum class ShopkeeperTab {
    HOME, JOBS, APPLICATIONS, PROFILE
}

@Composable
fun ShopkeeperBottomNavigation(
    selectedTab: ShopkeeperTab,
    onTabSelected: (ShopkeeperTab) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = selectedTab == ShopkeeperTab.HOME,
            onClick = { onTabSelected(ShopkeeperTab.HOME) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = { Text("Home", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ErnoGreen,
                selectedTextColor = ErnoGreen,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            selected = selectedTab == ShopkeeperTab.JOBS,
            onClick = { onTabSelected(ShopkeeperTab.JOBS) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Work,
                    contentDescription = "Jobs",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = { Text("Jobs", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ErnoGreen,
                selectedTextColor = ErnoGreen,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            selected = selectedTab == ShopkeeperTab.APPLICATIONS,
            onClick = { onTabSelected(ShopkeeperTab.APPLICATIONS) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Group,
                    contentDescription = "Applications",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = { Text("Applications", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ErnoGreen,
                selectedTextColor = ErnoGreen,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            selected = selectedTab == ShopkeeperTab.PROFILE,
            onClick = { onTabSelected(ShopkeeperTab.PROFILE) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = { Text("Profile", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ErnoGreen,
                selectedTextColor = ErnoGreen,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )
    }
}
