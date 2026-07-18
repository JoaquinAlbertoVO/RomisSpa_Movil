package com.romisspa.app.presentation.screens.limpieza

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.romisspa.app.domain.model.RutinaDiaria
import com.romisspa.app.domain.model.TareaEspecial
import com.romisspa.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LimpiezaScreen(
    viewModel: LimpiezaViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Rutina Diaria", "Tareas Especiales")

    var showAddDialog by remember { mutableStateOf(false) }

    // Estado para el PIN
    var showPinDialog by remember { mutableStateOf(false) }
    var selectedRutinaForPin by remember { mutableStateOf<RutinaDiaria?>(null) }
    var selectedEspecialForPin by remember { mutableStateOf<TareaEspecial?>(null) }
    var pinError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mantenimiento y Limpieza", color = MaterialTheme.colorScheme.onBackground) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = MaterialTheme.colorScheme.onBackground)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = RoseGold
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Tarea", tint = MaterialTheme.colorScheme.onPrimary)
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = RoseGold)
                }
            } else {
                if (selectedTabIndex == 0) {
                    // Rutinas Diarias
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.rutinasDiarias) { rutina ->
                            RutinaDiariaCard(
                                rutina = rutina,
                                onClick = {
                                    if (rutina.estadoActual == "PENDIENTE") {
                                        viewModel.solicitarSupervisionRutina(rutina)
                                    } else if (rutina.estadoActual == "POR_SUPERVISAR") {
                                        selectedRutinaForPin = rutina
                                        showPinDialog = true
                                    }
                                },
                                onDelete = { viewModel.deleteRutinaDiaria(rutina.id) }
                            )
                        }
                    }
                } else {
                    // Tareas Especiales
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.tareasEspeciales) { tarea ->
                            TareaEspecialCard(
                                tarea = tarea,
                                onClick = {
                                    if (tarea.estado == "PENDIENTE") {
                                        viewModel.solicitarSupervisionTareaEspecial(tarea)
                                    } else if (tarea.estado == "POR_SUPERVISAR") {
                                        selectedEspecialForPin = tarea
                                        showPinDialog = true
                                    }
                                },
                                onDelete = { viewModel.deleteTareaEspecial(tarea.id) }
                            )
                        }
                    }
                }
            }
        }
    }

    // Diálogo para añadir tarea (Diaria o Especial según el tab)
    if (showAddDialog) {
        if (selectedTabIndex == 0) {
            AddRutinaDiariaDialog(
                empleados = uiState.empleados,
                onDismiss = { showAddDialog = false },
                onConfirm = { tarea, empId, empNombre ->
                    viewModel.addRutinaDiaria(tarea, empId, empNombre)
                    showAddDialog = false
                }
            )
        } else {
            AddTareaEspecialDialog(
                empleados = uiState.empleados,
                onDismiss = { showAddDialog = false },
                onConfirm = { tarea, empId, empNombre, fecha ->
                    viewModel.addTareaEspecial(tarea, empId, empNombre, fecha)
                    showAddDialog = false
                }
            )
        }
    }

    // Diálogo del PIN
    if (showPinDialog) {
        PinDialog(
            isError = pinError,
            onDismiss = { 
                showPinDialog = false
                pinError = false
                selectedRutinaForPin = null
                selectedEspecialForPin = null
            },
            onConfirm = { pinInput ->
                if (selectedRutinaForPin != null) {
                    val exito = viewModel.aprobarSupervisionRutina(selectedRutinaForPin!!, pinInput)
                    if (exito) {
                        showPinDialog = false
                        pinError = false
                        selectedRutinaForPin = null
                    } else {
                        pinError = true
                    }
                } else if (selectedEspecialForPin != null) {
                    val exito = viewModel.aprobarSupervisionTareaEspecial(selectedEspecialForPin!!, pinInput)
                    if (exito) {
                        showPinDialog = false
                        pinError = false
                        selectedEspecialForPin = null
                    } else {
                        pinError = true
                    }
                }
            }
        )
    }
}

@Composable
fun RutinaDiariaCard(
    rutina: RutinaDiaria,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val (bgColor, contentColor) = getStateColors(rutina.estadoActual)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(contentColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.CleaningServices, contentDescription = null, tint = contentColor)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = rutina.tarea, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Text(text = "Asignado a: ${rutina.empleadoNombre}", fontSize = 14.sp, color = GreyWarm)
                Text(
                    text = formatEstadoLabel(rutina.estadoActual),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = contentColor,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = CharcoalSoft.copy(alpha = 0.5f))
            }
        }
    }
}

@Composable
fun TareaEspecialCard(
    tarea: TareaEspecial,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val (bgColor, contentColor) = getStateColors(tarea.estado)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(contentColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.CleaningServices, contentDescription = null, tint = contentColor)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = tarea.tarea, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Text(text = "Para: ${tarea.fechaAsignada}", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                Text(text = "Asignado a: ${tarea.empleadoNombre}", fontSize = 14.sp, color = GreyWarm)
                Text(
                    text = formatEstadoLabel(tarea.estado),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = contentColor,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = CharcoalSoft.copy(alpha = 0.5f))
            }
        }
    }
}

@Composable
fun getStateColors(estado: String): Pair<Color, Color> {
    val isDark = androidx.compose.foundation.isSystemInDarkTheme()
    return when (estado) {
        "PENDIENTE" -> Pair(if (isDark) Color(0xFF3B1E1E) else Color(0xFFFFF0F0), if(isDark) Color(0xFFEF9A9A) else Color(0xFFE53935))
        "POR_SUPERVISAR" -> Pair(if (isDark) Color(0xFF3D3219) else Color(0xFFFFF8E1), if(isDark) Color(0xFFFFE082) else Color(0xFFFBC02D))
        "COMPLETADA" -> Pair(if (isDark) Color(0xFF1B3B1E) else Color(0xFFE8F5E9), if(isDark) Color(0xFFA5D6A7) else Color(0xFF43A047))
        else -> Pair(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.onSurface)
    }
}

fun formatEstadoLabel(estado: String): String {
    return when (estado) {
        "PENDIENTE" -> "PENDIENTE (Toca para marcar lista)"
        "POR_SUPERVISAR" -> "FALTA SUPERVISIÓN (Toca para aprobar)"
        "COMPLETADA" -> "COMPLETADA"
        else -> estado
    }
}
