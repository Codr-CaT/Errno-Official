package com.erno.app.ui.screens.shopkeeper

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
    onPickupLocationChange: (String) -> Unit = {},
    onDropLocationChange: (String) -> Unit = {},
    onPackageTypeChange: (String) -> Unit = {},
    onDescriptionChange: (String) -> Unit,
    onRemoveDuration: (DurationPay) -> Unit,
    onAddMoreDurationClick: () -> Unit,
    onPreviewJobClick: () -> Unit
) {
    var currentStep by remember { mutableIntStateOf(1) } // Step 1: Details, Step 2: Time & Hours, Step 3: Location & Map
    var categoryExpanded by remember { mutableStateOf(false) }
    val categories = listOf("Delivery Partner", "Shop Helper", "Counter Staff", "Store Assistant", "Packing Staff", "Delivery Boy", "Cashier")

    // Standard hours list with estimated rates
    val availableHourOptions = listOf(
        DurationPay(1, 149),
        DurationPay(2, 249),
        DurationPay(3, 349),
        DurationPay(4, 449),
        DurationPay(6, 649),
        DurationPay(8, 849)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Post a New Job",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            text = "Step $currentStep of 3 • " + when (currentStep) {
                                1 -> "Basic Details"
                                2 -> "Select Time & Hours"
                                else -> "Location & Map"
                            },
                            fontSize = 12.sp,
                            color = ErnoGreen,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (currentStep > 1) {
                            currentStep--
                        } else {
                            onBackClick()
                        }
                    }) {
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
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (currentStep > 1) {
                        OutlinedButton(
                            onClick = { currentStep-- },
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color(0xFFD0D0D0))
                        ) {
                            Text(
                                text = "← Back",
                                fontSize = 15.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Button(
                        onClick = {
                            if (currentStep < 3) {
                                currentStep++
                            } else {
                                onPreviewJobClick()
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ErnoGreen),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = when (currentStep) {
                                1 -> "Next: Select Time →"
                                2 -> "Next: Location →"
                                else -> "Preview & Post →"
                            },
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        },
        containerColor = Color(0xFFF9FAFB)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Step Progress Indicator Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val steps = listOf("1. Details", "2. Select Time", "3. Map Location")
                steps.forEachIndexed { index, title ->
                    val stepNum = index + 1
                    val isActive = currentStep >= stepNum
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        color = if (isActive) Color(0xFFEAF5D8) else Color(0xFFF0F0F0)
                    ) {
                        Box(
                            modifier = Modifier.padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = title,
                                fontSize = 12.sp,
                                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
                                color = if (isActive) ErnoGreen else Color.Gray
                            )
                        }
                    }
                }
            }

            HorizontalDivider(color = Color(0xFFEFEFEF))

            // Scrollable Content depending on Current Step
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .widthIn(max = 800.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // STEP 1: JOB TITLE & DESCRIPTION
                    if (currentStep == 1) {
                        item {
                            Text(
                                text = "Basic Information",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }

                        // Category Dropdown
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
                                        onDismissRequest = { categoryExpanded = false },
                                        modifier = Modifier.background(Color.White)
                                    ) {
                                        categories.forEach { category ->
                                            DropdownMenuItem(
                                                text = { Text(category, color = Color.Black, fontWeight = FontWeight.Medium) },
                                                onClick = {
                                                    onCategoryChange(category)
                                                    categoryExpanded = false
                                                },
                                                modifier = Modifier.background(Color.White)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Job Title Input
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
                                    placeholder = { Text("e.g. Delivery Partner / Shop Helper", color = Color.Gray) },
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

                        // Description Input
                        item {
                            Column {
                                Text(
                                    text = "Job Description & Requirements",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Black
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                OutlinedTextField(
                                    value = state.draftDescription,
                                    onValueChange = { if (it.length <= 250) onDescriptionChange(it) },
                                    placeholder = { Text("Enter work details, tasks, or requirements...", color = Color.Gray) },
                                    textStyle = LocalTextStyle.current.copy(color = Color.Black),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(110.dp),
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
                                    text = "${state.draftDescription.length}/250",
                                    fontSize = 11.sp,
                                    color = Color.Gray,
                                    modifier = Modifier.align(Alignment.End)
                                )
                            }
                        }
                    }

                    // STEP 2: TIME & HOURS SELECTION WITH "SELECT / SELECTED" BUTTONS
                    if (currentStep == 2) {
                        item {
                            Column {
                                Text(
                                    text = "Select Time Limit & Hours Needed",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Text(
                                    text = "Tap the Select button on the time duration options you want workers to choose from.",
                                    fontSize = 12.sp,
                                    color = ErnoTextSecondary
                                )
                            }
                        }

                        // Available Hour Duration Option Cards
                        items(availableHourOptions) { option ->
                            val isSelected = state.draftPayStructure.any { it.hours == option.hours }
                            val currentPayItem = state.draftPayStructure.find { it.hours == option.hours } ?: option

                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                color = Color.White,
                                border = BorderStroke(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) ErnoGreen else Color(0xFFE0E0E0)
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp, vertical = 14.dp)
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .background(if (isSelected) Color(0xFFEAF5D8) else Color(0xFFF5F5F5), CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Schedule,
                                                contentDescription = null,
                                                tint = if (isSelected) ErnoGreen else Color.Gray,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(12.dp))

                                        Column {
                                            Text(
                                                text = "${option.hours} Hour${if (option.hours > 1) "s" else ""} Duration",
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Black
                                            )
                                            Text(
                                                text = "Pay Rate: ₹${currentPayItem.price}",
                                                fontSize = 12.sp,
                                                color = ErnoTextSecondary
                                            )
                                        }
                                    }

                                    // SELECT / SELECTED BUTTON
                                    if (isSelected) {
                                        Button(
                                            onClick = { onRemoveDuration(currentPayItem) },
                                            colors = ButtonDefaults.buttonColors(containerColor = ErnoGreen),
                                            shape = RoundedCornerShape(10.dp),
                                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "Selected",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }
                                    } else {
                                        OutlinedButton(
                                            onClick = { onAddMoreDurationClick() },
                                            shape = RoundedCornerShape(10.dp),
                                            border = BorderStroke(1.dp, ErnoGreen),
                                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                                        ) {
                                            Text(
                                                text = "+ Select",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = ErnoGreen
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Add Custom Time Limit Option Button
                        item {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onAddMoreDurationClick() },
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White,
                                border = BorderStroke(1.dp, ErnoGreen)
                            ) {
                                Row(
                                    modifier = Modifier.padding(vertical = 12.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = null,
                                        tint = ErnoGreen,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "+ Select Custom Time Limit & Pay",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ErnoGreen
                                    )
                                }
                            }
                        }
                    }

                    // STEP 3: LOCATION & MAP PIN
                    if (currentStep == 3) {
                        item {
                            Text(
                                text = "Shop Location & Map Pin",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }

                        item {
                            Column {
                                Text(
                                    text = "Shop / Work Address",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Black
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                OutlinedTextField(
                                    value = state.draftLocation,
                                    onValueChange = onLocationChange,
                                    placeholder = { Text("e.g. Shop #12, Okhla Market Area", color = Color.Gray) },
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
                            }
                        }

                        if (state.draftIsDelivery) {
                            item {
                                Column {
                                    Text(
                                        text = "Pickup Location",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.Black
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    OutlinedTextField(
                                        value = state.draftPickupLocation,
                                        onValueChange = onPickupLocationChange,
                                        placeholder = { Text("Pickup Store Address", color = Color.Gray) },
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

                            item {
                                Column {
                                    Text(
                                        text = "Customer Drop Location",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.Black
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    OutlinedTextField(
                                        value = state.draftDropLocation,
                                        onValueChange = onDropLocationChange,
                                        placeholder = { Text("Delivery Address", color = Color.Gray) },
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
                        }

                        item {
                            Column {
                                Text(
                                    text = "Interactive Location Map Pin:",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                WorkerLocationMapView(
                                    locationName = state.draftLocation.ifBlank { "Okhla Market Area" },
                                    distanceText = "Shop Location Map Pin",
                                    isDarkTheme = false
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
