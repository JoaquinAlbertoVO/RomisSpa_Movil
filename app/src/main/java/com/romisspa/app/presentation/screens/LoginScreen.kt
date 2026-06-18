package com.romisspa.app.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.romisspa.app.ui.theme.*

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {

    var email       by remember { mutableStateOf("") }
    var password    by remember { mutableStateOf("") }
    var showPass    by remember { mutableStateOf(false) }
    var emailError  by remember { mutableStateOf(false) }
    var passError   by remember { mutableStateOf(false) }
    var visible     by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { visible = true }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Cream, CreamDark, RoseGoldLight.copy(alpha = 0.3f))
                )
            )
    ) {
        // Decoración
        Box(modifier = Modifier.size(300.dp).offset(x = (-60).dp, y = (-60).dp).clip(RoundedCornerShape(50)).background(RoseGold.copy(alpha = 0.08f)))
        Box(modifier = Modifier.size(200.dp).align(Alignment.BottomEnd).offset(x = 60.dp, y = 60.dp).clip(RoundedCornerShape(50)).background(WarmGold.copy(alpha = 0.10f)))

        AnimatedVisibility(
            visible = visible,
            enter   = fadeIn() + slideInVertically(initialOffsetY = { it / 4 })
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text  = "✦", fontSize = 28.sp, color = WarmGold)
                Text(text = "Romi's", style = MaterialTheme.typography.displayMedium, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Light, color = RoseGoldDark, letterSpacing = 4.sp)
                Text(text = "SALON & SPA", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Medium, color = GreyWarm, letterSpacing = 5.sp)

                Spacer(Modifier.height(48.dp))

                Card(
                    modifier  = Modifier.fillMaxWidth(),
                    shape     = RoundedCornerShape(24.dp),
                    colors    = CardDefaults.cardColors(containerColor = White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(horizontal = 28.dp, vertical = 32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text  = "Iniciar Sesión", style = MaterialTheme.typography.headlineMedium, color = CharcoalSoft)
                        Spacer(Modifier.height(28.dp))

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it; emailError = false },
                            label = { Text("Correo electrónico") },
                            leadingIcon = { Icon(Icons.Default.Person, null, tint = RoseGold) },
                            isError = emailError,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            singleLine = true
                        )

                        Spacer(Modifier.height(16.dp))

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it; passError = false },
                            label = { Text("Contraseña") },
                            leadingIcon = { Icon(Icons.Default.Lock, null, tint = RoseGold) },
                            trailingIcon = {
                                IconButton(onClick = { showPass = !showPass }) {
                                    Icon(if (showPass) Icons.Default.VisibilityOff else Icons.Default.Visibility, null, tint = GreyWarm)
                                }
                            },
                            isError = passError,
                            visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            singleLine = true
                        )

                        Spacer(Modifier.height(32.dp))

                        Button(
                            onClick = {
                                if (email.isNotBlank() && password.isNotBlank()) onLoginSuccess()
                                else { emailError = email.isBlank(); passError = password.isBlank() }
                            },
                            modifier = Modifier.fillMaxWidth().height(52.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = RoseGold)
                        ) {
                            Text("INGRESAR", letterSpacing = 2.sp)
                        }
                    }
                }
            }
        }
    }
}
