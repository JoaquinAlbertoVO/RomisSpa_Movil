package com.romisspa.app.presentation.screens.citas

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.romisspa.app.domain.model.Cita
import com.romisspa.app.ui.theme.*

@Composable
fun CitasScreen(
    viewModel: CitasViewModel,
    onAddCita: () -> Unit,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(colors = listOf(Cream, CreamDark)))
        ) {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(500)) + slideInVertically(initialOffsetY = { 50 })
            ) {
                if (uiState.isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = RoseGold)
                    }
                } else if (uiState.citas.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "No hay citas programadas", color = GreyWarm)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.citas) { cita ->
                            CitaItem(
                                cita = cita,
                                onDelete = { viewModel.deleteCita(cita) }
                            )
                        }
                    }
                }
            }
        }

        // Botón Flotante para ir a Reservar
        FloatingActionButton(
            onClick = onAddCita,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            containerColor = RoseGold,
            contentColor = White
        ) {
            Icon(Icons.Default.Add, contentDescription = "Nueva Cita")
        }
    }
}

@Composable
fun CitaItem(
    cita: Cita,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = cita.cliente,
                        style = MaterialTheme.typography.titleMedium,
                        color = CharcoalSoft,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.width(8.dp))
                    Surface(
                        color = cita.colorEstado.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = cita.estado,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = cita.colorEstado
                        )
                    }
                }
                Text(
                    text = cita.servicio,
                    style = MaterialTheme.typography.bodyMedium,
                    color = RoseGold
                )
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CalendarMonth, null, modifier = Modifier.size(14.dp), tint = GreyWarm)
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "${cita.fecha} • ${cita.hora}",
                        style = MaterialTheme.typography.labelSmall,
                        color = GreyWarm
                    )
                }
            }

            var showOptions by remember { mutableStateOf(false) }

            Box {
                IconButton(onClick = { showOptions = true }) {
                    Icon(Icons.Default.MoreVert, null, tint = GreyWarm)
                }

                DropdownMenu(
                    expanded = showOptions,
                    onDismissRequest = { showOptions = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Eliminar Cita", color = Color.Red) },
                        onClick = {
                            onDelete()
                            showOptions = false
                        }
                    )
                }
            }
        }
    }
}
