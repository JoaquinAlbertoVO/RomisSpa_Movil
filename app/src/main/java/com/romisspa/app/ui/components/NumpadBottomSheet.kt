package com.romisspa.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.romisspa.app.ui.theme.RoseGold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumpadBottomSheet(
    currentValue: String,
    onValueChange: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "S/ $currentValue",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Numpad(
                onKeyPress = { key ->
                    when (key) {
                        "C" -> onValueChange("0")
                        "⌫" -> {
                            if (currentValue.length > 1) {
                                onValueChange(currentValue.dropLast(1))
                            } else {
                                onValueChange("0")
                            }
                        }
                        "." -> {
                            if (!currentValue.contains(".")) {
                                onValueChange(currentValue + key)
                            }
                        }
                        else -> { // Numeros
                            if (currentValue == "0" && key != ".") {
                                onValueChange(key)
                            } else {
                                // Limitar a dos decimales
                                val parts = currentValue.split(".")
                                if (parts.size == 2 && parts[1].length >= 2) {
                                    // ya tiene 2 decimales, no agregar mas
                                } else {
                                    onValueChange(currentValue + key)
                                }
                            }
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RoseGold),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Confirmar Monto", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun Numpad(onKeyPress: (String) -> Unit) {
    val keys = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf(".", "0", "⌫")
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        for (row in keys) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (key in row) {
                    NumpadKey(key = key, onClick = { onKeyPress(key) })
                }
            }
        }
    }
}

@Composable
fun NumpadKey(key: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (key == "⌫") {
            Icon(
                imageVector = Icons.Default.Backspace,
                contentDescription = "Borrar",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(28.dp)
            )
        } else {
            Text(
                text = key,
                fontSize = 28.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
