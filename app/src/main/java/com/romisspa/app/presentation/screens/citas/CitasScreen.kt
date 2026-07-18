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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.romisspa.app.domain.model.Cita
import com.romisspa.app.ui.theme.*

@Composable
fun CitasScreen(
    viewModel: CitasViewModel,
    onAddCita: () -> Unit,
    onBack: () -> Unit
) {

    LaunchedEffect(Unit) {
        viewModel.getCitas()
    }
    val uiState by viewModel.uiState.collectAsState()
    var visible by remember { mutableStateOf(false) }
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Pendientes", "Atendidas", "Todas")

    LaunchedEffect(Unit) { visible = true }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(colors = listOf(MaterialTheme.colorScheme.background, MaterialTheme.colorScheme.surfaceVariant)))
        ) {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.Transparent,
                contentColor = RoseGold,
                indicator = { tabPositions ->
                    if (selectedTabIndex < tabPositions.size) {
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                            color = RoseGold
                        )
                    }
                },
                divider = { HorizontalDivider(color = CreamDark) }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { 
                            Text(
                                text = title, 
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal, 
                                color = if (selectedTabIndex == index) RoseGold else GreyWarm
                            ) 
                        }
                    )
                }
            }

            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(500)) + slideInVertically(initialOffsetY = { 50 })
            ) {
                val citasFiltradas = uiState.citas.filter { cita ->
                    when (selectedTabIndex) {
                        0 -> cita.estado.equals("Pendiente", ignoreCase = true) || cita.estado.equals("Confirmada", ignoreCase = true)
                        1 -> cita.estado.equals("Atendida", ignoreCase = true)
                        else -> true
                    }
                }

                if (uiState.isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = RoseGold)
                    }
                } else if (citasFiltradas.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "No hay citas en esta categoría", color = GreyWarm)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(citasFiltradas) { cita ->
                            CitaItem(
                                cita = cita,
                                onConfirm = { viewModel.confirmarCita(cita) },
                                onSendReminder = { viewModel.enviarRecordatorio(cita) },
                                onMarkAttended = { viewModel.showAtenderDialog(cita) },
                                onDelete = { viewModel.deleteCita(cita) }
                            )
                        }
                    }
                }
            }
        }

        if (uiState.showAtenderDialog && uiState.selectedCita != null) {
            AtenderDialog(
                cita = uiState.selectedCita!!,
                servicios = uiState.servicios,
                empleados = uiState.empleados,
                productos = uiState.productos,
                insumosIniciales = uiState.insumosParaAtender,
                onDismiss = { viewModel.dismissAtenderDialog() },
                onConfirm = { monto, metodo, empleadoId, insumosFinales, serviciosFinales ->
                    viewModel.marcarComoAtendida(uiState.selectedCita!!, monto, metodo, empleadoId, insumosFinales, serviciosFinales)
                }
            )
        }

        // Botón Flotante para ir a Reservar
        FloatingActionButton(
            onClick = onAddCita,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            containerColor = RoseGold,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(Icons.Default.Add, contentDescription = "Nueva Cita")
        }
    }
}

@Composable
fun AtenderDialog(
    cita: Cita,
    servicios: List<com.romisspa.app.domain.model.Servicio>,
    empleados: List<com.romisspa.app.domain.model.Empleado>,
    productos: List<com.romisspa.app.domain.model.Producto>,
    insumosIniciales: List<com.romisspa.app.domain.model.Insumo>,
    onDismiss: () -> Unit,
    onConfirm: (Double, String, String?, List<com.romisspa.app.domain.model.Insumo>, String) -> Unit
) {
    val precioSugerido = remember(cita.servicio, servicios) {
        servicios.find { it.nombre.equals(cita.servicio, ignoreCase = true) }?.precio ?: ""
    }

    var monto by remember { mutableStateOf(precioSugerido) }
    var metodoPago by remember { mutableStateOf("Efectivo") }
    var selectedEmpleadoId by remember { mutableStateOf(cita.empleadoId) }
    var error by remember { mutableStateOf<String?>(null) }
    var expandedPago by remember { mutableStateOf(false) }
    var expandedEmpleado by remember { mutableStateOf(false) }

    // Estado de servicios adicionales
    var serviciosAdicionales by remember { mutableStateOf<List<com.romisspa.app.domain.model.Servicio>>(emptyList()) }
    var expandedServicioExtra by remember { mutableStateOf(false) }

    // Estado editable de insumos: lista de pares (insumo, cantidadTexto)
    var insumosEditables by remember(insumosIniciales) {
        mutableStateOf(insumosIniciales.map { it to it.cantidad.toString() })
    }
    var expandedInsumoExtra by remember { mutableStateOf(false) }

    // Actualiza el monto si se agregan servicios
    LaunchedEffect(precioSugerido, serviciosAdicionales) {
        val base = precioSugerido.replace(Regex("[^0-9.]"), "").toDoubleOrNull() ?: 0.0
        val extras = serviciosAdicionales.sumOf { it.precio.replace(Regex("[^0-9.]"), "").toDoubleOrNull() ?: 0.0 }
        monto = (base + extras).toString()
    }

    val metodos = listOf("Efectivo", "Yape", "Plin", "Tarjeta", "Transferencia")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Finalizar y Cobrar", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Column {
                    Text("Resumen:", style = MaterialTheme.typography.labelMedium, color = GreyWarm)
                    Text("${cita.cliente} - ${cita.servicio}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                    
                    serviciosAdicionales.forEach { serv ->
                        Text("+ ${serv.nombre}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                    }
                }

                // Selector de Servicios Extra
                Column {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        TextButton(
                            onClick = { expandedServicioExtra = true },
                            colors = ButtonDefaults.textButtonColors(contentColor = RoseGold)
                        ) {
                            Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Añadir otro servicio")
                        }
                        DropdownMenu(expanded = expandedServicioExtra, onDismissRequest = { expandedServicioExtra = false }) {
                            servicios.forEach { serv ->
                                DropdownMenuItem(
                                    text = { Text(serv.nombre) },
                                    onClick = { 
                                        serviciosAdicionales = serviciosAdicionales + serv
                                        val nuevosInsumos = serv.insumos.map { it to it.cantidad.toString() }
                                        insumosEditables = insumosEditables + nuevosInsumos
                                        expandedServicioExtra = false 
                                    }
                                )
                            }
                        }
                    }
                }

                Divider(color = MaterialTheme.colorScheme.surfaceVariant)

                // Selector de Empleada
                Column {
                    Text("Asignar a Trabajadora", style = MaterialTheme.typography.labelMedium, color = GreyWarm)
                    Box(modifier = Modifier.fillMaxWidth().padding(top = 4.dp)) {
                        val currentEmpleado = empleados.find { it.id == selectedEmpleadoId }
                        OutlinedButton(
                            onClick = { expandedEmpleado = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onSurface)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(currentEmpleado?.nombre ?: "Seleccionar Trabajadora")
                                Icon(Icons.Default.MoreVert, null, modifier = Modifier.size(18.dp))
                            }
                        }
                        DropdownMenu(expanded = expandedEmpleado, onDismissRequest = { expandedEmpleado = false }) {
                            empleados.forEach { emp ->
                                DropdownMenuItem(
                                    text = { Text(emp.nombre) },
                                    onClick = { selectedEmpleadoId = emp.id; expandedEmpleado = false }
                                )
                            }
                        }
                    }
                }

                // Selector de Método de Pago
                Column {
                    Text("Método de Pago", style = MaterialTheme.typography.labelMedium, color = GreyWarm)
                    Box(modifier = Modifier.fillMaxWidth().padding(top = 4.dp)) {
                        OutlinedButton(
                            onClick = { expandedPago = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onSurface)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(metodoPago)
                                Icon(Icons.Default.MoreVert, null, modifier = Modifier.size(18.dp))
                            }
                        }
                        DropdownMenu(expanded = expandedPago, onDismissRequest = { expandedPago = false }) {
                            metodos.forEach { metodo ->
                                DropdownMenuItem(
                                    text = { Text(metodo) },
                                    onClick = { metodoPago = metodo; expandedPago = false }
                                )
                            }
                        }
                    }
                }

                // Input de Monto
                OutlinedTextField(
                    value = monto,
                    onValueChange = {
                        if (it.isEmpty() || it.toDoubleOrNull() != null || it.endsWith(".")) {
                            monto = it; error = null
                        }
                    },
                    label = { Text("Monto cobrado (S/)") },
                    placeholder = { Text("0.00") },
                    singleLine = true,
                    isError = error != null,
                    supportingText = { if (error != null) Text(error!!, color = MaterialTheme.colorScheme.error) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    prefix = { Text("S/ ") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RoseGold,
                        unfocusedBorderColor = RoseGoldLight
                    )
                )

                // Sección de Insumos utilizados
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "🧴 Insumos utilizados",
                            style = MaterialTheme.typography.labelMedium,
                            color = GreyWarm
                        )
                        Box {
                            TextButton(
                                onClick = { expandedInsumoExtra = true },
                                colors = ButtonDefaults.textButtonColors(contentColor = RoseGold)
                            ) {
                                Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Añadir", fontSize = 12.sp)
                            }
                            DropdownMenu(expanded = expandedInsumoExtra, onDismissRequest = { expandedInsumoExtra = false }) {
                                productos.forEach { prod ->
                                    DropdownMenuItem(
                                        text = { Text(prod.nombre) },
                                        onClick = { 
                                            val nuevoInsumo = com.romisspa.app.domain.model.Insumo(
                                                productoId = prod.id,
                                                nombre = prod.nombre,
                                                cantidad = 1.0
                                            )
                                            insumosEditables = insumosEditables + (nuevoInsumo to "1.0")
                                            expandedInsumoExtra = false 
                                        }
                                    )
                                }
                            }
                        }
                    }

                    if (insumosEditables.isNotEmpty()) {
                        Text(
                            "Puedes editar las cantidades o eliminarlos",
                            style = MaterialTheme.typography.labelSmall,
                            color = GreyWarm.copy(alpha = 0.7f)
                        )
                        insumosEditables.forEachIndexed { index, (insumo, cantidadTexto) ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                IconButton(
                                    onClick = { 
                                        val nuevaLista = insumosEditables.toMutableList()
                                        nuevaLista.removeAt(index)
                                        insumosEditables = nuevaLista
                                    },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(Icons.Default.Close, null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(16.dp))
                                }
                                Text(
                                    text = insumo.nombre,
                                    modifier = Modifier.weight(1f),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                OutlinedTextField(
                                    value = cantidadTexto,
                                    onValueChange = { nuevo ->
                                        if (nuevo.isEmpty() || nuevo.toDoubleOrNull() != null || nuevo.endsWith(".")) {
                                            val nuevaLista = insumosEditables.toMutableList()
                                            nuevaLista[index] = insumo to nuevo
                                            insumosEditables = nuevaLista
                                        }
                                    },
                                    modifier = Modifier.width(90.dp),
                                    singleLine = true,
                                    shape = RoundedCornerShape(8.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = RoseGold,
                                        unfocusedBorderColor = RoseGoldLight
                                    ),
                                    textStyle = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    } else {
                        Text("No hay insumos registrados.", style = MaterialTheme.typography.bodySmall, color = GreyWarm)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val valor = monto.toDoubleOrNull()
                    if (valor != null && valor > 0 && selectedEmpleadoId != null) {
                        val insumosFinales = insumosEditables.map { (insumo, cantidadTexto) ->
                            insumo.copy(cantidad = cantidadTexto.toDoubleOrNull() ?: insumo.cantidad)
                        }
                        val serviciosNombres = mutableListOf(cita.servicio)
                        serviciosNombres.addAll(serviciosAdicionales.map { it.nombre })
                        val serviciosFinalesStr = serviciosNombres.joinToString(" + ")

                        onConfirm(valor, metodoPago, selectedEmpleadoId, insumosFinales, serviciosFinalesStr)
                    } else if (selectedEmpleadoId == null) {
                        error = "Debe seleccionar una trabajadora"
                    } else {
                        error = "Ingresa un monto válido"
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = RoseGold),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Confirmar Pago", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = GreyWarm)
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(20.dp)
    )
}



@Composable
fun CitaItem(
    cita: Cita,
    onConfirm: () -> Unit,
    onSendReminder: () -> Unit,
    onMarkAttended: () -> Unit,
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = cita.cliente,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
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
                Row (verticalAlignment = Alignment.CenterVertically) {
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
                    if (cita.estado == "Pendiente") {
                        DropdownMenuItem(
                            text = { Text("Confirmar Cita", color = Color(0xFF4CAF50)) },
                            onClick = {
                                onConfirm()
                                showOptions = false
                            }
                        )
                    }
                    DropdownMenuItem(
                        text = { Text("Enviar Recordatorio", color = RoseGold) },
                        onClick = {
                            onSendReminder()
                            showOptions = false
                        }
                    )
                    if (cita.estado == "Confirmada") {
                        DropdownMenuItem(
                            text = { Text("Marcar como Atendida", color = Color(0xFF2196F3)) },
                            onClick = {
                                onMarkAttended()
                                showOptions = false
                            }
                        )
                    }
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
