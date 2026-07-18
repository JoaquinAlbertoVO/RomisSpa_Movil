package com.romisspa.app.presentation.screens.reportes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.romisspa.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportesScreen(
    viewModel: ReportesViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Semanal", "Mensual", "Administración")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reportes y Analítica", color = MaterialTheme.colorScheme.onBackground) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = MaterialTheme.colorScheme.onBackground)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = RoseGold)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Brush.verticalGradient(colors = listOf(MaterialTheme.colorScheme.background, MaterialTheme.colorScheme.surfaceVariant)))
            ) {
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = RoseGoldDark
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = { Text(title, fontWeight = FontWeight.SemiBold) },
                            selectedContentColor = RoseGoldDark,
                            unselectedContentColor = GreyWarm
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (selectedTabIndex == 0) {
                        // Resumen Semanal
                        item {
                            ReportCard(title = "Ingresos de la Semana") {
                                Text(
                                    text = "S/ ${"%.2f".format(uiState.totalVentasSemana)}",
                                    style = MaterialTheme.typography.displaySmall,
                                    color = RoseGoldDark,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        item {
                            ReportCard(title = "Comisiones por Pagar (Semana)") {
                                if (uiState.comisionesSemana.isEmpty()) {
                                    Text("No hay comisiones registradas esta semana.", color = GreyWarm)
                                } else {
                                    uiState.comisionesSemana.forEach { (nombre, comisionTotal) ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(nombre, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                                            Text("S/ ${"%.2f".format(comisionTotal)}", color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                                        }

                                        uiState.detallesComisionesSemana[nombre]?.forEach { venta ->
                                            Row(
                                                modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 2.dp, bottom = 2.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(venta.servicio, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                                                    Text("Clienta: ${venta.cliente}", style = MaterialTheme.typography.bodySmall, color = GreyWarm)
                                                }
                                                Text("S/ ${"%.2f".format(venta.monto)}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                                            }
                                        }
                                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = GreyWarm.copy(alpha = 0.2f))
                                    }
                                }
                            }
                        }

                    } else if (selectedTabIndex == 1) {
                        // Resumen Mensual
                        item {
                            ReportCard(title = "Ventas del Mes Actual") {
                                Text(
                                    text = "S/ ${"%.2f".format(uiState.totalVentasMes)}",
                                    style = MaterialTheme.typography.displaySmall,
                                    color = RoseGoldDark,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        item {
                            ReportCard(title = "Ventas por Método de Pago") {
                                if (uiState.ventasPorMetodoPago.isEmpty()) {
                                    Text("No hay registros.", color = GreyWarm)
                                } else {
                                    uiState.ventasPorMetodoPago.forEach { (metodo, monto) ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(metodo, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
                                            Text("S/ ${"%.2f".format(monto)}", fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                                        }
                                        HorizontalDivider(color = GreyWarm.copy(alpha = 0.2f))
                                    }
                                }
                            }
                        }

                        item {
                            ReportCard(title = "Comisiones Acumuladas (Mes)") {
                                if (uiState.comisionesPorEmpleado.isEmpty()) {
                                    Text("No hay comisiones registradas este mes.", color = GreyWarm)
                                } else {
                                    uiState.comisionesPorEmpleado.forEach { (nombre, comision) ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(nombre, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
                                            Text("S/ ${"%.2f".format(comision)}", color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold)
                                        }
                                        HorizontalDivider(color = GreyWarm.copy(alpha = 0.2f))
                                    }
                                }
                            }
                        }

                        item {
                            ReportCard(title = "Top 5 Servicios Solicitados") {
                                if (uiState.serviciosMasSolicitados.isEmpty()) {
                                    Text("No hay citas registradas.", color = GreyWarm)
                                } else {
                                    uiState.serviciosMasSolicitados.forEachIndexed { index, pair ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text("${index + 1}. ${pair.first}", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
                                            Text("${pair.second} citas", fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                                        }
                                    }
                                }
                            }
                        }

                    } else if (selectedTabIndex == 2) {
                        // Administración (Ruth)
                        item {
                            ReportCard(title = "Ganancia de Administración (Ruth)") {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Ingresos Brutos (Semana)", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
                                    Text("S/ ${"%.2f".format(uiState.totalVentasSemana)}", fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Comisiones Pagadas", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
                                    Text("- S/ ${"%.2f".format(uiState.comisionesSemana.values.sum())}", fontWeight = FontWeight.Medium, color = Color(0xFFE53935))
                                }
                                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = RoseGoldLight.copy(alpha = 0.5f))
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Ganancia Neta (Semana)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                    Text("S/ ${"%.2f".format(uiState.gananciaAdministracionSemana)}", fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50), style = MaterialTheme.typography.titleMedium)
                                }
                                
                                Spacer(Modifier.height(32.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Ingresos Brutos (Mes)", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
                                    Text("S/ ${"%.2f".format(uiState.totalVentasMes)}", fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Comisiones Pagadas", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
                                    Text("- S/ ${"%.2f".format(uiState.comisionesPorEmpleado.values.sum())}", fontWeight = FontWeight.Medium, color = Color(0xFFE53935))
                                }
                                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = RoseGoldLight.copy(alpha = 0.5f))
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Ganancia Neta (Mes)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                    Text("S/ ${"%.2f".format(uiState.gananciaAdministracionMes)}", fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50), style = MaterialTheme.typography.titleMedium)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReportCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(12.dp))
            content()
        }
    }
}
