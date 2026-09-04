package com.erno.app.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erno.app.ui.theme.ErnoGreen

@Composable
fun WorkerLocationMapView(
    locationName: String,
    distanceText: String,
    latitude: Double = 28.5355,
    longitude: Double = 77.2641,
    isDarkTheme: Boolean = true
) {
    val context = LocalContext.current
    val backgroundColor = if (isDarkTheme) Color(0xFF131D2A) else Color(0xFFE8ECEF)
    val roadColor = if (isDarkTheme) Color(0xFF202D3D) else Color(0xFFFFFFFF)
    val accentPathColor = ErnoGreen

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Drawn Map Grid & Route Graphics
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val height = size.height

                // Main Roads
                drawRect(
                    color = roadColor,
                    topLeft = Offset(0f, height * 0.4f),
                    size = androidx.compose.ui.geometry.Size(width, 28f)
                )
                drawRect(
                    color = roadColor,
                    topLeft = Offset(width * 0.35f, 0f),
                    size = androidx.compose.ui.geometry.Size(28f, height)
                )
                drawRect(
                    color = roadColor,
                    topLeft = Offset(width * 0.7f, 0f),
                    size = androidx.compose.ui.geometry.Size(22f, height)
                )

                // Dashed Route Line from Worker to Store Location
                val path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(width * 0.15f, height * 0.75f)
                    lineTo(width * 0.35f, height * 0.4f)
                    lineTo(width * 0.7f, height * 0.4f)
                }
                drawPath(
                    path = path,
                    color = accentPathColor,
                    style = Stroke(
                        width = 8f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(16f, 12f), 0f)
                    )
                )

                // Start Circle (Worker Location)
                drawCircle(
                    color = Color(0xFF2196F3),
                    radius = 12f,
                    center = Offset(width * 0.15f, height * 0.75f)
                )
                drawCircle(
                    color = Color(0x442196F3),
                    radius = 24f,
                    center = Offset(width * 0.15f, height * 0.75f)
                )
            }

            // Destination Store Marker Overlay
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(x = 40.dp, y = (-10).dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = ErnoGreen,
                    shadowElevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Storefront,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = locationName,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            // Bottom Map Action Bar: Open Google Maps
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .clickable {
                        val gmmIntentUri = Uri.parse("geo:$latitude,$longitude?q=${Uri.encode(locationName)}")
                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
                            setPackage("com.google.android.apps.maps")
                        }
                        if (mapIntent.resolveActivity(context.packageManager) != null) {
                            context.startActivity(mapIntent)
                        } else {
                            val browserIntent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://www.google.com/maps/search/?api=1&query=${Uri.encode(locationName)}")
                            )
                            context.startActivity(browserIntent)
                        }
                    },
                color = Color.Black.copy(alpha = 0.6f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = ErnoGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "$locationName • $distanceText",
                            fontSize = 12.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Directions,
                            contentDescription = null,
                            tint = ErnoGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "Open in Maps",
                            fontSize = 11.sp,
                            color = ErnoGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
