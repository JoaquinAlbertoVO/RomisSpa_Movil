package com.romisspa.app.presentation.screens.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.romisspa.app.ui.theme.*

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {

    var pinInput by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf<String?>(null) }
    var visible by remember { mutableStateOf(false) }

    val adminPin = "0626"

    LaunchedEffect(Unit) { visible = true }

    LaunchedEffect(pinInput) {
        if (pinInput.length == 4) {
            if (pinInput == adminPin) {
                onLoginSuccess()
            } else {
                pinError = "PIN incorrecto"
                pinInput = "" 
            }
        }
    }

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
        Box(modifier = Modifier.size(200.dp).align(Alignment.BottomEnd).offset(x = 60.dp, y = 60.dp).clip(RoundedCornerShape(50)).background(WarmGold.copy(alpha = 0.25f)))

        AnimatedVisibility(
            visible = visible,
            enter   = fadeIn() + slideInVertically(initialOffsetY = { it / 4 })
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp, vertical = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text(text  = "✦", fontSize = 28.sp, color = WarmGold)
                Text(text = "Romi's", style = MaterialTheme.typography.displayMedium, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Light, color = RoseGoldDark, letterSpacing = 4.sp)
                Text(text = "SALON & SPA", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Medium, color = GreyWarm, letterSpacing = 5.sp)

                Spacer(modifier = Modifier.weight(1f))

                // PIN indicators
                Text(
                    text = pinError ?: "Ingresa tu PIN",
                    color = if (pinError != null) MaterialTheme.colorScheme.error else CharcoalSoft,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 0 until 4) {
                        val isFilled = i < pinInput.length
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(if (isFilled) CharcoalSoft else CharcoalSoft.copy(alpha = 0.15f))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(48.dp))

                // Numpad Grid
                CustomNumpad(
                    onNumberClick = { num ->
                        if (pinInput.length < 4) {
                            pinError = null
                            pinInput += num
                        }
                    },
                    onDeleteClick = {
                        if (pinInput.isNotEmpty()) {
                            pinInput = pinInput.dropLast(1)
                        }
                    }
                )

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun CustomNumpad(onNumberClick: (String) -> Unit, onDeleteClick: () -> Unit) {
    val padData = listOf(
        listOf(Pair("1", ""), Pair("2", "ABC"), Pair("3", "DEF")),
        listOf(Pair("4", "GHI"), Pair("5", "JKL"), Pair("6", "MNO")),
        listOf(Pair("7", "PQRS"), Pair("8", "TUV"), Pair("9", "WXYZ")),
        listOf(Pair("", ""), Pair("0", "+"), Pair("del", ""))
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (row in padData) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (item in row) {
                    val number = item.first
                    val letters = item.second

                    if (number.isEmpty()) {
                        Spacer(modifier = Modifier.size(72.dp))
                    } else if (number == "del") {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .clickable { onDeleteClick() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Backspace,
                                contentDescription = "Eliminar",
                                tint = CharcoalSoft,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(White.copy(alpha = 0.6f))
                                .clickable { onNumberClick(number) },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = number,
                                    fontSize = 32.sp,
                                    color = CharcoalSoft,
                                    fontWeight = FontWeight.Normal
                                )
                                if (letters.isNotEmpty()) {
                                    Text(
                                        text = letters,
                                        fontSize = 10.sp,
                                        color = CharcoalSoft.copy(alpha = 0.6f),
                                        letterSpacing = 1.sp
                                    )
                                } else {
                                    Spacer(modifier = Modifier.height(14.dp)) // Maintain alignment for '1'
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}