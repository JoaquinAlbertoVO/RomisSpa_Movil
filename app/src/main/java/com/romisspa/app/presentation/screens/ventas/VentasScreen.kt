package com.romisspa.app.presentation.screens.ventas

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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.romisspa.app.domain.model.Empleado
import com.romisspa.app.domain.model.Venta
import com.romisspa.app.ui.theme.*
import java.util.*
import android.app.DatePickerDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VentasScreen(
    viewModel: VentasViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var visible by remember { mutableStateOf(false) }
    var showFilterSheet by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadData()
        visible = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Caja y Ventas", color = MaterialTheme.colorScheme.onBackground) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = MaterialTheme.colorScheme.onBackground)
                    }
                },
                actions = {
                    IconButton(onClick = { showFilterSheet = true }) {
                        Icon(
                            Icons.Default.FilterList, 
                            contentDescription = "Filtrar",
                            tint = if (uiState.selectedEmpleadoId != null || uiState.fechaInicio != null) RoseGold else MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Brush.verticalGradient(colors = listOf(MaterialTheme.colorScheme.background, MaterialTheme.colorScheme.surfaceVariant)))
        ) {
            // Resumen de Caja
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = RoseGold)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        if (uiState.selectedEmpleadoId != null || uiState.fechaInicio != null) "Total Filtrado" else "Total Recaudado Histórico",
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f), 
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = uiState.totalRecaudado,
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (uiState.selectedEmpleadoId != null || uiState.fechaInicio != null) {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SuggestionChip(
                        onClick = { 
                            viewModel.onEmpleadoFilterChange(null)
                            viewModel.onDateRangeChange(null, null)
                        },
                        label = { Text("Limpiar Filtros") },
                        icon = { Icon(Icons.Default.Close, null, modifier = Modifier.size(16.dp)) }
                    )
                }
            }

            Text(
                "Historial de Ventas",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )

            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(500)) + slideInVertically(initialOffsetY = { 50 })
            ) {
                if (uiState.isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = RoseGold)
                    }
                } else if (uiState.filteredVentas.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "No hay ventas con estos filtros", color = GreyWarm)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(uiState.filteredVentas) { venta ->
                            val empleado = uiState.empleados.find { it.id == venta.empleadoId }
                            VentaItem(venta, empleado?.nombre)
                        }
                    }
                }
            }
        }
    }

    if (showFilterSheet) {
        FilterBottomSheet(
            uiState = uiState,
            onDismiss = { showFilterSheet = false },
            onEmpleadoSelect = { viewModel.onEmpleadoFilterChange(it) },
            onDateSelect = { inicio, fin -> viewModel.onDateRangeChange(inicio, fin) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    uiState: VentasUiState,
    onDismiss: () -> Unit,
    onEmpleadoSelect: (String?) -> Unit,
    onDateSelect: (String?, String?) -> Unit
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
        ) {
            Text("Filtrar Ventas", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onSurface)
            Spacer(Modifier.height(16.dp))

            Text("Por Empleado", style = MaterialTheme.typography.titleSmall, color = RoseGold)
            Row(modifier = Modifier.padding(vertical = 8.dp)) {
                FilterChip(
                    selected = uiState.selectedEmpleadoId == null,
                    onClick = { onEmpleadoSelect(null) },
                    label = { Text("Todos") }
                )
                Spacer(Modifier.width(8.dp))
                // Mostrar solo los primeros 3 o usar LazyRow si son muchos
                uiState.empleados.take(3).forEach { emp ->
                    FilterChip(
                        selected = uiState.selectedEmpleadoId == emp.id,
                        onClick = { onEmpleadoSelect(emp.id) },
                        label = { Text(emp.nombre.split(" ")[0]) }
                    )
                    Spacer(Modifier.width(8.dp))
                }
            }

            Spacer(Modifier.height(16.dp))
            Text("Por Rango de Fecha", style = MaterialTheme.typography.titleSmall, color = RoseGold)
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = {
                        val calendar = Calendar.getInstance()
                        DatePickerDialog(context, { _, y, m, d ->
                            val date = "%04d-%02d-%02d".format(y, m + 1, d)
                            onDateSelect(date, uiState.fechaFin)
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(uiState.fechaInicio ?: "Desde")
                }
                Spacer(Modifier.width(16.dp))
                OutlinedButton(
                    onClick = {
                        val calendar = Calendar.getInstance()
                        DatePickerDialog(context, { _, y, m, d ->
                            val date = "%04d-%02d-%02d".format(y, m + 1, d)
                            onDateSelect(uiState.fechaInicio, date)
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(uiState.fechaFin ?: "Hasta")
                }
            }

            Spacer(Modifier.height(32.dp))
            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = RoseGold)
            ) {
                Text("Aplicar Filtros", color = MaterialTheme.colorScheme.onPrimary)
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
fun VentaItem(venta: Venta, empleadoNombre: String? = null) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(RoseGold.copy(alpha = 0.1f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.AttachMoney, contentDescription = null, tint = RoseGold)
            }
            
            Spacer(Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = venta.cliente,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = venta.servicio,
                    style = MaterialTheme.typography.bodySmall,
                    color = RoseGold
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CalendarToday, null, modifier = Modifier.size(12.dp), tint = GreyWarm)
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "${venta.fecha} • ${venta.metodoPago}",
                        style = MaterialTheme.typography.labelSmall,
                        color = GreyWarm
                    )
                }
            }
            
            Text(
                text = "S/ ${"%.2f".format(venta.monto)}",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF4CAF50), // Verde para dinero
                fontWeight = FontWeight.Bold
            )
        }
    }
}
