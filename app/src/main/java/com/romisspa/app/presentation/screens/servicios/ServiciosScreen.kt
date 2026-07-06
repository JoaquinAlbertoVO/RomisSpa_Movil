
package com.romisspa.app.presentation.screens.servicios

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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.romisspa.app.domain.model.Servicio
import com.romisspa.app.ui.theme.*

@Composable
fun ServiciosScreen(
    viewModel: ServiciosViewModel,
    onBack: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.getServicios()
    }
    val uiState by viewModel.uiState.collectAsState()
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { visible = true }

    if (uiState.showAddDialog) {
        AddServicioDialog(
            onDismiss = { viewModel.onShowAddDialog(false) },
            onConfirm = { nuevo ->
                viewModel.addServicio(nuevo)
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(colors = listOf(Cream, CreamDark)))
        ) {
            // Buscador
            OutlinedTextField(
                value = uiState.searchText,
                onValueChange = viewModel::onSearchTextChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Buscar servicio...") },
                leadingIcon = { Icon(Icons.Default.Search, null, tint = GreyWarm) },
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RoseGold,
                    unfocusedBorderColor = RoseGoldLight
                )
            )

            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(500)) + slideInVertically(initialOffsetY = { 50 })
            ) {
                val filteredServicios = uiState.servicios.filter {
                    it.nombre.contains(uiState.searchText, ignoreCase = true)
                }

                if (uiState.isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = RoseGold)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filteredServicios) { servicio ->
                            ServicioItem(
                                servicio = servicio,
                                onDelete = { viewModel.deleteServicio(servicio) }
                            )
                        }
                    }
                }
            }
        }

        // Botón Flotante
        FloatingActionButton(
            onClick = { viewModel.onShowAddDialog(true) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            containerColor = RoseGold,
            contentColor = White
        ) {
            Icon(Icons.Default.Add, contentDescription = "Añadir Servicio")
        }
    }
}

@Composable
fun AddServicioDialog(onDismiss: () -> Unit, onConfirm: (Servicio) -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var duracion by remember { mutableStateOf("") }

    // Estado para capturar y mostrar los mensajes de error
    var nombreError by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo Servicio", fontWeight = FontWeight.Bold, color = CharcoalSoft) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {

                // Campo Nombre (Con control de error en tiempo real para números)
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nuevoTexto ->
                        nombre = nuevoTexto

                        // Validar si contiene algún número o dígito
                        val tieneNumeros = nuevoTexto.any { char -> char.isDigit() }

                        nombreError = when {
                            tieneNumeros -> "El nombre del servicio solo puede contener letras."
                            nuevoTexto.isNotBlank() -> null
                            else -> nombreError
                        }
                    },
                    label = { Text("Nombre del servicio") },
                    isError = nombreError != null,
                    supportingText = {
                        if (nombreError != null) {
                            Text(text = nombreError!!, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoseGold, unfocusedBorderColor = RoseGoldLight),
                    modifier = Modifier.fillMaxWidth()
                )

                // Campo Descripción
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción (opcional)") },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoseGold, unfocusedBorderColor = RoseGoldLight),
                    modifier = Modifier.fillMaxWidth()
                )

                // Campo Precio (Libre)
                OutlinedTextField(
                    value = precio,
                    onValueChange = { precio = it },
                    label = { Text("Precio (S/)") },
                    placeholder = { Text("0.00") },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoseGold, unfocusedBorderColor = RoseGoldLight),
                    modifier = Modifier.fillMaxWidth()
                )

                // Campo Duración
                OutlinedTextField(
                    value = duracion,
                    onValueChange = { duracion = it },
                    label = { Text("Duración (Ej: 45 min)") },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoseGold, unfocusedBorderColor = RoseGoldLight),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val tieneNumeros = nombre.any { char -> char.isDigit() }

                    if (nombre.isBlank()) {
                        nombreError = "El nombre no puede estar vacío."
                    } else if (tieneNumeros) {
                        nombreError = "El nombre del servicio solo puede contener letras."
                    } else {
                        onConfirm(Servicio(nombre, descripcion, "S/ $precio", duracion))
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = RoseGold)
            ) {
                Text("Guardar", color = White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar", color = GreyWarm) }
        },
        containerColor = White,
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
fun ServicioItem(
    servicio: Servicio,
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
                Text(
                    text = servicio.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    color = CharcoalSoft,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = servicio.descripcion,
                    style = MaterialTheme.typography.bodySmall,
                    color = GreyWarm
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = servicio.duracion,
                    style = MaterialTheme.typography.labelSmall,
                    color = RoseGold
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                var showOptions by remember { mutableStateOf(false) }

                Text(
                    text = servicio.precio,
                    style = MaterialTheme.typography.titleLarge,
                    color = RoseGoldDark,
                    fontWeight = FontWeight.ExtraBold
                )

                Box {
                    IconButton(onClick = { showOptions = true }) {
                        Icon(Icons.Default.MoreVert, null, tint = GreyWarm, modifier = Modifier.size(20.dp))
                    }

                    DropdownMenu(
                        expanded = showOptions,
                        onDismissRequest = { showOptions = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Eliminar", color = Color.Red) },
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
}