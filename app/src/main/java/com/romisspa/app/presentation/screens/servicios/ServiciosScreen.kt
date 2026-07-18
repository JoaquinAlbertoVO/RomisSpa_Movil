
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
            productos = uiState.productos,
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
                .background(Brush.verticalGradient(colors = listOf(MaterialTheme.colorScheme.background, MaterialTheme.colorScheme.surfaceVariant)))
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
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(Icons.Default.Add, contentDescription = "Añadir Servicio")
        }
    }
}

@Composable
fun AddServicioDialog(
    productos: List<com.romisspa.app.domain.model.Producto>,
    onDismiss: () -> Unit,
    onConfirm: (Servicio) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var duracion by remember { mutableStateOf("") }
    var nombreError by remember { mutableStateOf<String?>(null) }

    // Insumos de la receta: lista de pares (Producto, cantidad como texto)
    var insumosReceta by remember { mutableStateOf<List<Pair<com.romisspa.app.domain.model.Producto, String>>>(emptyList()) }
    var expandedProducto by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo Servicio", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nuevoTexto ->
                        nombre = nuevoTexto
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
                        if (nombreError != null) Text(text = nombreError!!, color = MaterialTheme.colorScheme.error)
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoseGold, unfocusedBorderColor = RoseGoldLight),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción (opcional)") },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoseGold, unfocusedBorderColor = RoseGoldLight),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = precio,
                    onValueChange = { precio = it },
                    label = { Text("Precio (S/)") },
                    placeholder = { Text("0.00") },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoseGold, unfocusedBorderColor = RoseGoldLight),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = duracion,
                    onValueChange = { duracion = it },
                    label = { Text("Duración (Ej: 45 min)") },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoseGold, unfocusedBorderColor = RoseGoldLight),
                    modifier = Modifier.fillMaxWidth()
                )

                // Sección de Insumos / Receta
                if (productos.isNotEmpty()) {
                    Divider(color = MaterialTheme.colorScheme.surfaceVariant)
                    Text(
                        "🧴 Insumos utilizados (opcional)",
                        style = MaterialTheme.typography.labelMedium,
                        color = GreyWarm
                    )
                    Text(
                        "Indica qué productos se consumen al realizar este servicio",
                        style = MaterialTheme.typography.labelSmall,
                        color = GreyWarm.copy(alpha = 0.7f)
                    )

                    // Lista de insumos ya añadidos
                    insumosReceta.forEachIndexed { index, (producto, cantidadTexto) ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = producto.nombre,
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            OutlinedTextField(
                                value = cantidadTexto,
                                onValueChange = { nuevo ->
                                    if (nuevo.isEmpty() || nuevo.toDoubleOrNull() != null || nuevo.endsWith(".")) {
                                        val nuevaLista = insumosReceta.toMutableList()
                                        nuevaLista[index] = producto to nuevo
                                        insumosReceta = nuevaLista
                                    }
                                },
                                modifier = Modifier.width(80.dp),
                                singleLine = true,
                                label = { Text("Cant.", style = MaterialTheme.typography.labelSmall) },
                                shape = RoundedCornerShape(8.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = RoseGold,
                                    unfocusedBorderColor = RoseGoldLight
                                ),
                                textStyle = MaterialTheme.typography.bodySmall
                            )
                            IconButton(
                                onClick = {
                                    insumosReceta = insumosReceta.filterIndexed { i, _ -> i != index }
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Text("✕", color = Color.Red, style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }

                    // Botón para añadir un producto a la receta
                    Box {
                        OutlinedButton(
                            onClick = { expandedProducto = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = RoseGold)
                        ) {
                            Text("+ Añadir insumo a la receta", style = MaterialTheme.typography.labelMedium)
                        }
                        DropdownMenu(
                            expanded = expandedProducto,
                            onDismissRequest = { expandedProducto = false }
                        ) {
                            val productosDisponibles = productos.filter { p ->
                                insumosReceta.none { (prod, _) -> prod.id == p.id }
                            }
                            if (productosDisponibles.isEmpty()) {
                                DropdownMenuItem(
                                    text = { Text("No hay más productos", color = GreyWarm) },
                                    onClick = { expandedProducto = false }
                                )
                            } else {
                                productosDisponibles.forEach { prod ->
                                    DropdownMenuItem(
                                        text = { Text(prod.nombre) },
                                        onClick = {
                                            insumosReceta = insumosReceta + (prod to "1.0")
                                            expandedProducto = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
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
                        val insumosDominio = insumosReceta.mapNotNull { (prod, cantTexto) ->
                            val cantidad = cantTexto.toDoubleOrNull() ?: return@mapNotNull null
                            if (cantidad <= 0) return@mapNotNull null
                            com.romisspa.app.domain.model.Insumo(
                                productoId = prod.id,
                                nombre = prod.nombre,
                                cantidad = cantidad
                            )
                        }
                        onConfirm(Servicio(nombre = nombre, descripcion = descripcion, precio = "S/ $precio", duracion = duracion, insumos = insumosDominio))
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = RoseGold)
            ) {
                Text("Guardar", color = MaterialTheme.colorScheme.onPrimary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar", color = GreyWarm) }
        },
        containerColor = MaterialTheme.colorScheme.surface,
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
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
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
                    color = MaterialTheme.colorScheme.onSurface,
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