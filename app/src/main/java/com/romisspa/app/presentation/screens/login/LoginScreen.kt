package com.romisspa.app.presentation.screens.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.romisspa.app.ui.theme.*

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {

    var email       by remember { mutableStateOf("") }
    var password    by remember { mutableStateOf("") }
    var showPass    by remember { mutableStateOf(false) }

    // Cambiados a String? para manejar mensajes de error específicos
    var emailError  by remember { mutableStateOf<String?>(null) }
    var passError   by remember { mutableStateOf<String?>(null) }
    var visible     by remember { mutableStateOf(false) }

    // Credenciales del Administrador únicas
    val adminEmail = "admin@romisspa.com"
    val adminPassword = "admin123"

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

                        // Campo Correo
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it; emailError = null },
                            label = { Text("Correo electrónico") },
                            leadingIcon = { Icon(Icons.Default.Person, null, tint = RoseGold) },
                            isError = emailError != null,
                            supportingText = {
                                if (emailError != null) {
                                    Text(text = emailError!!, color = MaterialTheme.colorScheme.error)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoseGold, unfocusedBorderColor = RoseGoldLight)
                        )

                        Spacer(Modifier.height(16.dp))

                        // Campo Contraseña
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it; passError = null },
                            label = { Text("Contraseña") },
                            leadingIcon = { Icon(Icons.Default.Lock, null, tint = RoseGold) },
                            trailingIcon = {
                                IconButton(onClick = { showPass = !showPass }) {
                                    Icon(if (showPass) Icons.Default.VisibilityOff else Icons.Default.Visibility, null, tint = GreyWarm)
                                }
                            },
                            isError = passError != null,
                            supportingText = {
                                if (passError != null) {
                                    Text(text = passError!!, color = MaterialTheme.colorScheme.error)
                                }
                            },
                            visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoseGold, unfocusedBorderColor = RoseGoldLight)
                        )

                        Spacer(Modifier.height(32.dp))

                        // Botón Ingresar con validaciones estrictas
                        Button(
                            onClick = {
                                val emailLimpio = email.trim()
                                var todoValido = true

                                // 1. Validar campos vacíos
                                if (emailLimpio.isBlank()) {
                                    emailError = "El correo no puede estar vacío."
                                    todoValido = false
                                }
                                if (password.isBlank()) {
                                    passError = "La contraseña no puede estar vacía."
                                    todoValido = false
                                }

                                // 2. Si no están vacíos, validar credenciales del administrador
                                if (todoValido) {
                                    if (emailLimpio == adminEmail && password == adminPassword) {
                                        onLoginSuccess()
                                    } else {
                                        if (emailLimpio != adminEmail) {
                                            emailError = "Acceso denegado. Correo de administrador no válido."
                                        }
                                        if (password != adminPassword) {
                                            passError = "Contraseña incorrecta."
                                        }
                                    }
                                }
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