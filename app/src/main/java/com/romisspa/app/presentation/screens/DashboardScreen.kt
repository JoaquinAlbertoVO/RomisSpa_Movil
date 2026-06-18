package com.romisspa.app.presentation.screens

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
    onReservaClick: () -> Unit,
    onCitasClick: () -> Unit,
    onServiciosClick: () -> Unit,
    onClientesClick: () -> Unit,
    onLogout: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    val modules = listOf(
        SpaModule("Reservar Cita", "Agenda tu próxima visita", Icons.Default.CalendarMonth, RoseGold, onReservaClick),
        SpaModule("Mis Citas", "Ver historial y próximas", Icons.Default.EventNote, WarmGold, onCitasClick),
        SpaModule("Servicios", "Catálogo de tratamientos", Icons.Default.Spa, Color(0xFF7BA7BC), onServiciosClick),
        SpaModule("Clientes", "Gestión de clientes", Icons.Default.People, Color(0xFF9C7BB5), onClientesClick),
        SpaModule("Empleados", "Equipo de profesionales", Icons.Default.Badge, Color(0xFF7BAF8C)),
        SpaModule("Inventario", "Productos y stock", Icons.Default.Inventory2, Color(0xFFB5896A)),
        SpaModule("Ventas", "Registro de ventas", Icons.Default.PointOfSale, Color(0xFF7B9BAF)),
        SpaModule("Reportes", "Estadísticas del negocio", Icons.Default.BarChart, Color(0xFFAF7B9C))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(Cream, CreamDark)))
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(52.dp).clip(CircleShape).background(White.copy(alpha = 0.25f)), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Person, null, tint = White, modifier = Modifier.size(30.dp))
                        }
                        Spacer(Modifier.width(16.dp))
                        Column {
                            Text("Bienvenida, Administradora", style = MaterialTheme.typography.titleMedium, color = White)
                            Text("Hoy tienes 5 citas programadas", style = MaterialTheme.typography.bodyMedium, color = White.copy(alpha = 0.85f))
                        }
                    }
                }
            }
        }

        // Stats
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatChip("12", "Clientes", Modifier.weight(1f))
            StatChip("S/ 1,240", "Ventas", Modifier.weight(1f))
            StatChip("3", "Pendientes", Modifier.weight(1f))
        }

        Text("Módulos del sistema", style = MaterialTheme.typography.titleMedium, color = CharcoalSoft, modifier = Modifier.padding(24.dp, 16.dp, 24.dp, 8.dp))

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
    Card(modifier = modifier, shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = White)) {
        Column(modifier = Modifier.fillMaxWidth().padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, style = MaterialTheme.typography.titleLarge, color = RoseGoldDark, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.labelSmall, color = GreyWarm)
        }
    }
}

@Composable
private fun ModuleCard(module: SpaModule) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { module.onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(18.dp)) {
            Box(modifier = Modifier.size(48.dp).clip(RoundedCornerShape(14.dp)).background(module.accent.copy(alpha = 0.12f)), contentAlignment = Alignment.Center) {
                Icon(module.icon, null, tint = module.accent, modifier = Modifier.size(26.dp))
            }
            Spacer(Modifier.height(14.dp))
            Text(module.title, style = MaterialTheme.typography.titleMedium, color = CharcoalSoft)
            Text(module.subtitle, style = MaterialTheme.typography.labelSmall, color = GreyWarm)
            Spacer(Modifier.height(12.dp))
            Icon(Icons.Default.ArrowForward, null, tint = module.accent, modifier = Modifier.size(14.dp).align(Alignment.End))
        }
    }
}
