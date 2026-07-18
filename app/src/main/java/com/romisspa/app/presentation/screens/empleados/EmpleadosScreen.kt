package com.romisspa.app.presentation.screens.empleados

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.romisspa.app.domain.model.Empleado
import com.romisspa.app.domain.model.Venta
import com.romisspa.app.ui.theme.CharcoalSoft
import com.romisspa.app.ui.theme.GreyWarm
import com.romisspa.app.ui.theme.RoseGold
import com.romisspa.app.ui.theme.White
import java.time.LocalDate
import java.time.DayOfWeek
import java.time.temporal.TemporalAdjusters

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmpleadosScreen(
    viewModel: EmpleadosViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var empleadoToEdit by remember { mutableStateOf<Empleado?>(null) }
    var empleadoToShowHistory by remember { mutableStateOf<Empleado?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Empleados") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Empleado")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.empleados) { empleado ->
                        EmpleadoItem(
                            empleado = empleado,
                            ventas = uiState.ventas,
                            onEdit = { empleadoToEdit = empleado },
                            onDelete = { viewModel.deleteEmpleado(empleado.id) },
                            onViewHistory = { empleadoToShowHistory = empleado }
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        EmpleadoDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { nombre, especialidad, telefono, comision ->
                viewModel.addEmpleado(nombre, especialidad, telefono, comision)
                showAddDialog = false
            }
        )
    }

    empleadoToEdit?.let { empleado ->
        EmpleadoDialog(
            empleado = empleado,
            onDismiss = { empleadoToEdit = null },
            onConfirm = { nombre, especialidad, telefono, comision ->
                viewModel.updateEmpleado(empleado.copy(
                    nombre = nombre,
                    especialidad = especialidad,
                    telefono = telefono,
                    comision = comision
                ))
                empleadoToEdit = null
            }
        )
    }

    empleadoToShowHistory?.let { empleado ->
        val historial = uiState.ventas.filter { it.empleadoId == empleado.id }
        EmpleadoHistoryDialog(
            empleado = empleado,
            historial = historial,
            onDismiss = { empleadoToShowHistory = null }
        )
    }
}

@Composable
fun EmpleadoItem(
    empleado: Empleado,
    ventas: List<Venta>,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onViewHistory: () -> Unit
) {
    val comisionSemanal = remember(ventas, empleado) {
        val hoy = LocalDate.now()
        val inicioSemana = hoy.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val finSemana = hoy.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

        val ventasSemana = ventas.filter { it.empleadoId == empleado.id }
            .filter {
                try {
                    val fechaVenta = LocalDate.parse(it.fecha)
                    !fechaVenta.isBefore(inicioSemana) && !fechaVenta.isAfter(finSemana)
                } catch (e: Exception) {
                    false
                }
            }
        
        val totalVentas = ventasSemana.sumOf { it.monto }
        totalVentas * (empleado.comision / 100.0)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = empleado.nombre, style = MaterialTheme.typography.titleMedium)
                Text(text = empleado.especialidad, style = MaterialTheme.typography.bodyMedium)
                Text(text = "Tel: ${empleado.telefono}", style = MaterialTheme.typography.bodySmall)
                Text(text = "Comisión: ${empleado.comision}%", style = MaterialTheme.typography.bodySmall)
                
                Surface(
                    color = RoseGold.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                ) {
                    Text(
                        text = "Acumulado Semanal: S/ ${String.format("%.2f", comisionSemanal)}", 
                        style = MaterialTheme.typography.labelMedium,
                        color = RoseGold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                TextButton(
                    onClick = onViewHistory,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("Ver Historial", color = RoseGold, style = MaterialTheme.typography.labelMedium)
                }
            }
            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmpleadoDialog(
    empleado: Empleado? = null,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, Double) -> Unit
) {
    var nombre by remember { mutableStateOf(empleado?.nombre ?: "") }
    var especialidad by remember { mutableStateOf(empleado?.especialidad ?: "") }
    var telefono by remember { mutableStateOf(empleado?.telefono ?: "") }
    var comision by remember { mutableStateOf(empleado?.comision?.toString() ?: "40.0") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (empleado == null) "Agregar Empleado" else "Editar Empleado") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = especialidad,
                    onValueChange = { especialidad = it },
                    label = { Text("Especialidad") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = telefono,
                    onValueChange = { telefono = it },
                    label = { Text("Teléfono") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = comision,
                    onValueChange = { comision = it },
                    label = { Text("Comisión %") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirm(nombre, especialidad, telefono, comision.toDoubleOrNull() ?: 0.0)
            }) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun EmpleadoHistoryDialog(
    empleado: Empleado,
    historial: List<Venta>,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Text(
                "Historial - ${empleado.nombre}", 
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            ) 
        },
        text = {
            if (historial.isEmpty()) {
                Text("No hay servicios registrados para este empleado.", color = GreyWarm)
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
                                Text("Cliente: ${venta.cliente}", style = MaterialTheme.typography.bodySmall, color = GreyWarm)
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
