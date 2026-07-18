package com.romisspa.app.presentation.screens.clientes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.romisspa.app.domain.model.Cliente
import com.romisspa.app.domain.model.Venta
import com.romisspa.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientesScreen(
    viewModel: ClientesViewModel,
    onBack: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.getClientes()
    }
    val uiState by viewModel.uiState.collectAsState()
    var visible by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }
    var clienteToShowHistory by remember { mutableStateOf<Cliente?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) { visible = true }

    val filteredClientes = uiState.clientes.filter {
        it.nombre.contains(uiState.searchText, ignoreCase = true) || it.telefono.contains(uiState.searchText)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
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
                    placeholder = { Text("Buscar cliente por nombre o teléfono...") },
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
                    if (uiState.isLoading && uiState.clientes.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = RoseGold)
                        }
                    } else if (filteredClientes.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = "No se encontraron clientes", color = GreyWarm)
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(filteredClientes) { cliente ->
                                ClienteItem(
                                    cliente = cliente,
                                    onViewHistory = { clienteToShowHistory = cliente }
                                )
                            }
                        }
                    }
                }
            }

            // Botón Flotante Activo
            FloatingActionButton(
                onClick = {
                    viewModel.clearError()
                    showAddDialog = true
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp),
                containerColor = RoseGold,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Cliente")
            }

            // Llamada al Diálogo Emergente
            if (showAddDialog) {
                AddClienteDialog(
                    errorMessage = uiState.error,
                    isLoading = uiState.isLoading,
                    onDismiss = { showAddDialog = false },
                    onConfirm = { nombre, telefono ->
                        viewModel.insertarCliente(nombre, telefono, onSuccess = {
                            showAddDialog = false
                        })
                    }
                )
            }

            // Historial del Cliente
            clienteToShowHistory?.let { cliente ->
                val historial = uiState.ventas.filter { it.cliente.equals(cliente.nombre, ignoreCase = true) }
                ClienteHistoryDialog(
                    cliente = cliente,
                    historial = historial,
                    onDismiss = { clienteToShowHistory = null }
                )
            }
        }
    }
}

@Composable
fun AddClienteDialog(
    errorMessage: String?,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }

    // Estados locales para controlar el error visual de cada campo
    var nombreError by remember { mutableStateOf<String?>(null) }
    var telefonoError by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Nuevo Cliente", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                // 1. Campo de Nombre con validación nativa
                OutlinedTextField(
                    value = nombre,
                    onValueChange = {
                        nombre = it
                        // Limpia el error mientras escribe si ya es válido
                        val regexNombre = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]*$".toRegex()
                        nombreError = if (!it.matches(regexNombre)) {
                            "El nombre solo puede contener letras."
                        } else {
                            null
                        }
                    },
                    label = { Text("Nombre Completo") },
                    singleLine = true,
                    enabled = !isLoading,
                    isError = nombreError != null,
                    supportingText = {
                        if (nombreError != null) {
                            Text(text = nombreError!!, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoseGold)
                )

                // 2. Campo de Teléfono con validación nativa
                OutlinedTextField(
                    value = telefono,
                    onValueChange = {
                        telefono = it
                        // Limpia o activa el error según los dígitos
                        val regexTelefono = "^[0-9]*$".toRegex()
                        telefonoError = when {
                            !it.matches(regexTelefono) -> "El teléfono solo puede contener números."
                            it.length > 9 -> "El teléfono no puede tener más de 9 dígitos."
                            else -> null
                        }
                    },
                    label = { Text("Teléfono / Celular") },
                    singleLine = true,
                    enabled = !isLoading,
                    isError = telefonoError != null,
                    supportingText = {
                        if (telefonoError != null) {
                            Text(text = telefonoError!!, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoseGold)
                )

                if (isLoading) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        color = RoseGold
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val nombreLimpio = nombre.trim()
                    val telefonoLimpio = telefono.trim()

                    // Validaciones finales antes de enviar
                    val regexNombre = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$".toRegex()
                    val regexTelefono = "^[0-9]{9}$".toRegex()

                    if (!nombreLimpio.matches(regexNombre)) {
                        nombreError = "El nombre solo puede contener letras."
                    }
                    if (!telefonoLimpio.matches(regexTelefono)) {
                        telefonoError = "El teléfono debe tener exactamente 9 dígitos."
                    }

                    // Si no hay errores locales, procedemos a guardar
                    if (nombreError == null && telefonoError == null && nombreLimpio.isNotBlank() && telefonoLimpio.isNotBlank()) {
                        onConfirm(nombre, telefono)
                    }
                },
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = RoseGold)
            ) {
                Text("Guardar", color = MaterialTheme.colorScheme.onPrimary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !isLoading) {
                Text("Cancelar", color = GreyWarm)
            }
        },
        shape = RoundedCornerShape(16.dp),
        containerColor = MaterialTheme.colorScheme.surface
    )
}
@Composable
fun ClienteItem(
    cliente: Cliente,
    onViewHistory: () -> Unit
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
            Box(
                modifier = Modifier
                    .size(45.dp)
                    .clip(CircleShape)
                    .background(RoseGold.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = cliente.nombre.take(1).uppercase(),
                    color = RoseGold,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = cliente.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Phone, null, modifier = Modifier.size(14.dp), tint = GreyWarm)
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = cliente.telefono,
                        style = MaterialTheme.typography.bodySmall,
                        color = GreyWarm
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                var showOptions by remember { mutableStateOf(false) }

                Text(
                    text = "${cliente.totalVisitas} visitas",
                    style = MaterialTheme.typography.labelSmall,
                    color = RoseGold,
                    fontWeight = FontWeight.Bold
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
                            text = { Text("Ver Historial", color = RoseGold) },
                            onClick = { 
                                showOptions = false
                                onViewHistory()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Eliminar Cliente", color = Color.Red) },
                            onClick = { showOptions = false }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ClienteHistoryDialog(
    cliente: Cliente,
    historial: List<Venta>,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Text(
                "Historial - ${cliente.nombre}", 
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            ) 
        },
        text = {
            if (historial.isEmpty()) {
                Text("No hay servicios registrados para este cliente.", color = GreyWarm)
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.heightIn(max = 300.dp)
                ) {
                    items(historial) { venta ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                            ) {
                                Text(venta.servicio, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Fecha: ${venta.fecha}", style = MaterialTheme.typography.bodySmall, color = GreyWarm)
                                Text("Monto: S/ ${venta.monto}", style = MaterialTheme.typography.bodySmall, color = RoseGold)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar", color = RoseGold)
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(16.dp)
    )
}