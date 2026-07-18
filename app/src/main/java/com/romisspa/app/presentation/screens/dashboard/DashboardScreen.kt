package com.romisspa.app.presentation.screens.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.romisspa.app.ui.theme.*

data class SpaModule(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val accent: Color,
    val onClick: () -> Unit = {}
)

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onReservaClick: () -> Unit,
    onCitasClick: () -> Unit,
    onServiciosClick: () -> Unit,
    onClientesClick: () -> Unit,
    onVentasClick: () -> Unit,
    onInventarioClick: () -> Unit,
    onEmpleadosClick: () -> Unit,
    onLimpiezaClick: () -> Unit,
    onReportesClick: () -> Unit,
    onCajaClick: () -> Unit,
    isDarkTheme: Boolean,
    onThemeToggle: (Boolean) -> Unit,
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadDashboardData()
        visible = true
    }

    val modules = listOf(
        SpaModule("Reservar Cita", "Agenda tu próxima visita", Icons.Default.CalendarMonth, RoseGold, onReservaClick),
        SpaModule("Mis Citas", "Ver historial y próximas", Icons.Default.EventNote, WarmGold, onCitasClick),
        SpaModule("Servicios", "Catálogo de tratamientos", Icons.Default.Spa, Color(0xFF7BA7BC), onServiciosClick),
        SpaModule("Clientes", "Gestión de clientes", Icons.Default.People, Color(0xFF9C7BB5), onClientesClick),
        SpaModule("Empleados", "Equipo de profesionales", Icons.Default.Badge, Color(0xFF7BAF8C), onEmpleadosClick),
        SpaModule("Limpieza", "Mantenimiento y rutinas", Icons.Default.CleaningServices, Color(0xFFC08C78), onLimpiezaClick),
        SpaModule("Inventario", "Productos y stock", Icons.Default.Inventory2, Color(0xFFB5896A), onInventarioClick),
        SpaModule("Ventas", "Registro de ventas", Icons.Default.PointOfSale, Color(0xFF7B9BAF), onVentasClick),
        SpaModule("Reportes", "Estadísticas del negocio", Icons.Default.BarChart, Color(0xFFAF7B9C), onReportesClick),
        SpaModule("Flujo de Caja", "Control diario y cierre", Icons.Default.AttachMoney, Color(0xFF4CAF50), onCajaClick)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(MaterialTheme.colorScheme.background, MaterialTheme.colorScheme.surfaceVariant)))
    ) {
        // Bienvenida
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(400)) + slideInVertically(initialOffsetY = { -it / 3 })
        ) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(modifier = Modifier.fillMaxWidth().background(Brush.horizontalGradient(colors = listOf(RoseGold, RoseGoldDark))).padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Box(modifier = Modifier.size(52.dp).clip(CircleShape).background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.25f)), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Person, null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(30.dp))
                        }
                        Spacer(Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Bienvenida, Administradora", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onPrimary)
                            Text("Hoy tienes ${uiState.citasPendientes} citas pendientes", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f))
                        }
                        IconButton(onClick = { onThemeToggle(!isDarkTheme) }) {
                            Icon(
                                imageVector = if (isDarkTheme) Icons.Default.WbSunny else Icons.Default.Bedtime,
                                contentDescription = "Modo Oscuro",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
        }

        // Stats
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatChip(uiState.totalClientes, "Clientes", Modifier.weight(1f))
            StatChip(uiState.totalVentas, "Ventas", Modifier.weight(1f))
            StatChip(uiState.citasPendientes, "Pendientes", Modifier.weight(1f))
        }

        // Alertas de Stock
        if (uiState.stockAlerts.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)), // Rojo muy suave
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = Color.Red)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("Alerta de Stock Bajo", style = MaterialTheme.typography.titleSmall, color = Color.Red)
                        Text(
                            "Hay ${uiState.stockAlerts.size} productos con stock insuficiente.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    TextButton(onClick = onInventarioClick) {
                        Text("Ver", color = Color.Red)
                    }
                }
            }
        }

        Text("Módulos del sistema", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.padding(24.dp, 16.dp, 24.dp, 8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(modules) { module -> ModuleCard(module) }
        }
    }
}

@Composable
private fun StatChip(value: String, label: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier, shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(modifier = Modifier.fillMaxWidth().padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun ModuleCard(module: SpaModule) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { module.onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(18.dp)) {
            Box(modifier = Modifier.size(48.dp).clip(RoundedCornerShape(14.dp)).background(module.accent.copy(alpha = 0.12f)), contentAlignment = Alignment.Center) {
                Icon(module.icon, null, tint = module.accent, modifier = Modifier.size(26.dp))
            }
            Spacer(Modifier.height(14.dp))
            Text(module.title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
            Text(module.subtitle, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(12.dp))
            Icon(Icons.Default.ArrowForward, null, tint = module.accent, modifier = Modifier.size(14.dp).align(Alignment.End))
        }
    }
}
