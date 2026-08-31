package com.erno.app.ui.screens.role

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erno.app.ui.components.ErnoLogo
import com.erno.app.ui.theme.ErnoLime
import com.erno.app.ui.theme.ErnoTextSecondary

enum class Role(val title: String, val description: String, val icon: ImageVector) {
    SHOP_OWNER("Shop Owner", "Hire workers for your store", Icons.Default.Storefront),
    WORKER("Worker", "Find daily wage work easily", Icons.Default.Construction)
}

@Composable
fun RoleSelectionScreen(
    onShopkeeperClick: () -> Unit,
    onWorkerClick: () -> Unit,
    onCreateAccount: () -> Unit = {}
) {
    var selectedRole by remember { mutableStateOf(Role.SHOP_OWNER) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Official Logo - Centered
            ErnoLogo(modifier = Modifier.height(32.dp))

            Spacer(modifier = Modifier.height(32.dp))

            // Welcome Text
            Text(
                text = buildAnnotatedString {
                    append("Welcome to ")
                    withStyle(SpanStyle(color = ErnoLime, fontWeight = FontWeight.Bold)) {
                        append("ERRNO")
                    }
                },
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "On-demand workforce platform for work-based jobs.",
                fontSize = 14.sp,
                color = ErnoTextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Illustration Placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(Color(0xFFF1F8E9), RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                // Placeholder illustration content
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = if (selectedRole == Role.SHOP_OWNER) Icons.Default.Storefront else Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = ErnoLime.copy(alpha = 0.6f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Login as",
                modifier = Modifier.align(Alignment.Start),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Roles Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Role.entries.forEach { role ->
                    RoleCard(
                        role = role,
                        isSelected = selectedRole == role,
                        modifier = Modifier.weight(1f),
                        onClick = { selectedRole = role }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Continue Button
            Button(
                onClick = {
                    when (selectedRole) {
                        Role.SHOP_OWNER -> onShopkeeperClick()
                        Role.WORKER -> onWorkerClick()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ErnoLime),
                shape = RoundedCornerShape(14.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                Text(
                    text = "Continue",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Create Account Link
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onCreateAccount() }
            ) {
                Text(
                    text = "New to ERRNO? ",
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Text(
                    text = "Create an account",
                    color = ErnoLime,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Safety Info
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Your number is safe with us",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Footer Links
            Text(
                text = buildAnnotatedString {
                    append("By continuing, you agree to our ")
                    withStyle(SpanStyle(color = ErnoLime, fontWeight = FontWeight.SemiBold)) {
                        append("Terms of Service")
                    }
                    append(" and ")
                    withStyle(SpanStyle(color = ErnoLime, fontWeight = FontWeight.SemiBold)) {
                        append("Privacy Policy")
                    }
                },
                fontSize = 11.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}

@Composable
fun RoleCard(
    role: Role,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) ErnoLime else Color(0xFFF0F0F0)
    val titleColor = if (isSelected) ErnoLime else Color.Black
    val backgroundColor = if (isSelected) Color.White else Color(0xFFFAFAFA)

    Surface(
        onClick = onClick,
        modifier = modifier
            .height(130.dp)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        shadowElevation = if (isSelected) 2.dp else 0.dp
    ) {
        Box(modifier = Modifier.padding(8.dp)) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = ErnoLime,
                    modifier = Modifier
                        .size(18.dp)
                        .align(Alignment.TopEnd)
                )
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = role.icon,
                    contentDescription = null,
                    tint = if (isSelected) ErnoLime else Color.Gray,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = role.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = titleColor,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = role.description,
                    fontSize = 9.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    lineHeight = 11.sp
                )
            }
        }
    }
}
