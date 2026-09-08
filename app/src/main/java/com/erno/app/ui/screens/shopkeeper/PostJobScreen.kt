package com.erno.app.ui.screens.shopkeeper

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erno.app.data.model.DurationPay
import com.erno.app.ui.components.WorkerLocationMapView
import com.erno.app.ui.theme.ErnoGreen
import com.erno.app.ui.theme.ErnoTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostJobScreen(
    state: ShopkeeperState,
    onBackClick: () -> Unit,
    onTitleChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onLocationChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onRemoveDuration: (DurationPay) -> Unit,
    onAddMoreDurationClick: () -> Unit,
    onPreviewJobClick: () -> Unit
) {
    var categoryExpanded by remember { mutableStateOf(false) }
    val categories = listOf("Shop Helper", "Counter Staff", "Store Assistant", "Packing Staff", "Delivery Boy", "Cashier")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Post a New Job",
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
            Surface(
                color = Color.White,
                tonalElevation = 8.dp
            ) {
                Box(modifier = Modifier.padding(16.dp)) {
                    Button(
                        onClick = onPreviewJobClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ErnoGreen),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Preview Job",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        },
        containerColor = Color(0xFFF9FAFB)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Job Details Header
            item {
                Text(
                    text = "Job Details",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            // Job Title
            item {
                Column {
                    Text(
                        text = "Job Title",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = state.draftTitle,
                        onValueChange = onTitleChange,
                        placeholder = { Text("e.g. Shop Helper", color = Color.Gray) },
                        textStyle = LocalTextStyle.current.copy(color = Color.Black),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedBorderColor = ErnoGreen,
                            unfocusedBorderColor = Color(0xFFE0E0E0),
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        ),
                        singleLine = true
                    )
                }
            }

            // Job Category Dropdown
            item {
                Column {
                    Text(
                        text = "Job Category",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    ExposedDropdownMenuBox(
                        expanded = categoryExpanded,
                        onExpandedChange = { categoryExpanded = !categoryExpanded }
                    ) {
                        OutlinedTextField(
                            value = state.draftCategory,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = Color.Black
                                )
                            },
                            textStyle = LocalTextStyle.current.copy(color = Color.Black),
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                                focusedBorderColor = ErnoGreen,
                                unfocusedBorderColor = Color(0xFFE0E0E0),
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = categoryExpanded,
                            onDismissRequest = { categoryExpanded = false }
                        ) {
                            categories.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category, color = Color.Black) },
                                    onClick = {
                                        onCategoryChange(category)
                                        categoryExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Job Location
            item {
                Column {
                    Text(
                        text = "Job Location & Shop Map",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = state.draftLocation,
                        onValueChange = onLocationChange,
                        placeholder = { Text("e.g. Okhla Market Area", color = Color.Gray) },
                        textStyle = LocalTextStyle.current.copy(color = Color.Black),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = Color.Gray
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedBorderColor = ErnoGreen,
                            unfocusedBorderColor = Color(0xFFE0E0E0),
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Map Location Preview for Shopkeeper
                    WorkerLocationMapView(
                        locationName = state.draftLocation.ifBlank { "Okhla Market Area" },
                        distanceText = "Shop Location Map Pin",
                        isDarkTheme = false
                    )
                }
            }

            // Job Description
            item {
                Column {
                    Text(
                        text = "Job Description",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = state.draftDescription,
                        onValueChange = { if (it.length <= 200) onDescriptionChange(it) },
                        placeholder = { Text("Describe duties, requirements, and store details...", color = Color.Gray) },
                        textStyle = LocalTextStyle.current.copy(color = Color.Black),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedBorderColor = ErnoGreen,
                            unfocusedBorderColor = Color(0xFFE0E0E0),
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${state.draftDescription.length}/200",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }

            // Working Hours & Pay Structure Header
            item {
                Column {
                    Text(
                        text = "Working Hours & Fixed Time Limit",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "Set fixed time limits and hourly pay. Workers will see these exact options.",
                        fontSize = 12.sp,
                        color = ErnoTextSecondary
                    )
                }
            }

            // List of Pay Options
            items(state.draftPayStructure) { item ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEFEFEF))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${item.hours} Hour${if (item.hours > 1) "s" else ""} Fixed Limit",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "₹${item.price}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Remove",
                                tint = Color.Gray,
                                modifier = Modifier
                                    .size(18.dp)
                                    .clickable { onRemoveDuration(item) }
                            )
                        }
                    }
                }
            }

            // "+ Add More Duration" link
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onAddMoreDurationClick() }
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = ErnoGreen,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Add More Fixed Time Options",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ErnoGreen
                    )
                }
            }
        }
    }
}
