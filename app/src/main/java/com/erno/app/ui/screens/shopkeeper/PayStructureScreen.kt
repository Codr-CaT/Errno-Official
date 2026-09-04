package com.erno.app.ui.screens.shopkeeper

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
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
import com.erno.app.ui.theme.ErnoGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayStructureScreen(
    state: ShopkeeperState,
    onBackClick: () -> Unit,
    onUpdatePrice: (hours: Int, newPrice: Int) -> Unit,
    onAddMoreDuration: (hours: Int, price: Int) -> Unit,
    onDoneClick: () -> Unit
) {
    var editingItem by remember { mutableStateOf<DurationPay?>(null) }
    var editPriceText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Pay Structure",
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
                        onClick = onDoneClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ErnoGreen),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Done",
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Info Alert Banner
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF1F8E9),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDCEDC8))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = ErnoGreen,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Set pay for multiple duration options. Workers can choose as per their availability.",
                            fontSize = 12.sp,
                            color = Color(0xFF33691E),
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            // Duration Rows
            items(state.draftPayStructure) { item ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            editingItem = item
                            editPriceText = item.price.toString()
                        },
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEFEFEF))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${item.hours} Hour${if (item.hours > 1) "s" else ""}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "₹${item.price}",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = Color.Gray,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            // "+ Add More" button
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val nextHour = (state.draftPayStructure.maxOfOrNull { it.hours } ?: 0) + 1
                            val lastPrice = state.draftPayStructure.lastOrNull()?.price ?: 99
                            onAddMoreDuration(nextHour, lastPrice + 70)
                        }
                        .padding(vertical = 10.dp),
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
                        text = "Add More",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ErnoGreen
                    )
                }
            }
        }
    }

    // Edit Price Dialog
    editingItem?.let { item ->
        AlertDialog(
            onDismissRequest = { editingItem = null },
            title = { Text("Edit Pay for ${item.hours} Hour(s)") },
            text = {
                OutlinedTextField(
                    value = editPriceText,
                    onValueChange = { editPriceText = it },
                    label = { Text("Price (₹)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val newPrice = editPriceText.toIntOrNull()
                        if (newPrice != null && newPrice > 0) {
                            onUpdatePrice(item.hours, newPrice)
                        }
                        editingItem = null
                    }
                ) {
                    Text("Save", color = ErnoGreen, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { editingItem = null }) {
                    Text("Cancel", color = Color.Gray)
                }
            }
        )
    }
}
