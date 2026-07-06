package com.romisspa.app.presentation.screens.reserva

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.romisspa.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservaScreen(
    viewModel: ReservaViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val serviciosAvailable by viewModel.serviciosAvailable.collectAsState()
    var visible by remember { mutableStateOf(false) }
    var showConfirm by remember { mutableStateOf(false) }

    // ── Estados para validaciones y control del DatePicker ──
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    var nombreError by remember { mutableStateOf<String?>(null) }
    var telefonoError by remember { mutableStateOf<String?>(null) }
    var camposIncompletosError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) { visible = true }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            viewModel.clearSuccess()
            onBack()
        }
    }

    // Diálogo de DatePicker para seleccionar fecha visualmente
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val seleccionado = datePickerState.selectedDateMillis
                        if (seleccionado != null) {
                            val fechaFormateada = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                                .format(java.util.Date(seleccionado))
                            viewModel.onFechaChange(fechaFormateada)
                        }
                        showDatePicker = false
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = RoseGold)
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar", color = GreyWarm)
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = RoseGold,
                    todayContentColor = RoseGold
                )
            )
        }
    }

    if (showConfirm) {
        ConfirmacionDialog(
            onDismiss = { showConfirm = false },
            onConfirm = {
                showConfirm = false
                viewModel.saveReserva()
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(Cream, CreamDark)))
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(400)) + slideInVertically(initialOffsetY = { it / 5 })
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // ── Sección: Datos del cliente ────────────────────────
                SectionHeader(title = "Datos del Cliente", icon = "👤")

                SpaTextField(
                    value = uiState.clienteNombre,
                    onValueChange = { nuevoTexto ->
                        val tieneNumeros = nuevoTexto.any { char -> char.isDigit() }
                        nombreError = if (tieneNumeros) "El nombre no puede contener números." else null
                        viewModel.onNombreChange(nuevoTexto)
                    },
                    label = "Nombre completo",
                    isError = nombreError != null,
                    supportingText = nombreError,
                    leadingIcon = { Icon(Icons.Default.Person, null, tint = RoseGold) }
                )

                SpaTextField(
                    value = uiState.clienteTelefono,
                    onValueChange = { nuevoTexto ->
                        val soloNumeros = nuevoTexto.all { char -> char.isDigit() }
                        if (soloNumeros && nuevoTexto.length <= 9) {
                            viewModel.onTelefonoChange(nuevoTexto)
                            telefonoError = if (nuevoTexto.length < 9) {
                                "El número celular debe tener exactamente 9 dígitos."
                            } else {
                                null
                            }
                        }
                    },
                    label = "Teléfono",
                    isError = telefonoError != null,
                    supportingText = telefonoError,
                    leadingIcon = { Icon(Icons.Default.Phone, null, tint = RoseGold) }
                )

                // ── Sección: Servicio ─────────────────────────────────
                SectionHeader(title = "Servicio", icon = "✨")

                DropdownSpa(
                    label = "Selecciona un servicio",
                    options = serviciosAvailable.map { it.nombre },
                    selected = uiState.servicioSelected,
                    onSelect = viewModel::onServicioChange,
                    icon = { Icon(Icons.Default.Spa, null, tint = RoseGold) }
                )

                DropdownSpa(
                    label = "Especialista",
                    options = listOf("Rosa Mendoza", "Carmen Flores", "Lucia Torres", "María Quispe"),
                    selected = uiState.empleadaSelected,
                    onSelect = viewModel::onEmpleadaChange,
                    icon = { Icon(Icons.Default.Badge, null, tint = RoseGold) }
                )

                // ── Sección: Fecha y hora ─────────────────────────────
                SectionHeader(title = "Fecha y Hora", icon = "📅")

                SpaTextField(
                    value = uiState.fechaSelected,
                    onValueChange = {},
                    label = "Fecha de la cita",
                    leadingIcon = { Icon(Icons.Default.CalendarMonth, null, tint = RoseGold) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDatePicker = true } // El Box exterior captura el clic perfectamente
                )

                Text(
                    text = "Horario disponible",
                    style = MaterialTheme.typography.bodyMedium,
                    color = GreyWarm,
                    modifier = Modifier.padding(top = 4.dp)
                )

                val horas = listOf("09:00 AM", "10:00 AM", "11:00 AM", "12:00 PM", "02:00 PM", "03:00 PM", "04:00 PM", "05:00 PM")
                val rows = horas.chunked(4)
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    rows.forEach { row ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            row.forEach { hora ->
                                val isSelected = uiState.horaSelected == hora
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSelected) RoseGold else White)
                                        .border(
                                            width = 1.dp,
                                            color = if (isSelected) RoseGold else RoseGoldLight,
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .clickable { viewModel.onHoraChange(hora) }
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = hora,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (isSelected) White else CharcoalSoft,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                            repeat(4 - row.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }

                SectionHeader(title = "Notas adicionales", icon = "📝")

                OutlinedTextField(
                    value = uiState.notasExtra,
                    onValueChange = viewModel::onNotasChange,
                    label = { Text("Indicaciones especiales (opcional)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RoseGold,
                        unfocusedBorderColor = RoseGoldLight,
                        focusedLabelColor = RoseGold,
                        cursorColor = RoseGold
                    ),
                    maxLines = 4
                )

                if (camposIncompletosError != null) {
                    Text(
                        text = camposIncompletosError!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }

                Spacer(Modifier.height(8.dp))

                Button(
                    onClick = {
                        var todoValido = true

                        if (uiState.clienteNombre.isBlank()) {
                            nombreError = "El nombre no puede estar vacío."
                            todoValido = false
                        }
                        if (uiState.clienteTelefono.isBlank()) {
                            telefonoError = "El teléfono no puede estar vacío."
                            todoValido = false
                        } else if (uiState.clienteTelefono.length != 9) {
                            telefonoError = "El número celular debe tener exactamente 9 dígitos."
                            todoValido = false
                        }

                        if (uiState.servicioSelected.isBlank() ||
                            uiState.empleadaSelected.isBlank() ||
                            uiState.fechaSelected.isBlank() ||
                            uiState.horaSelected.isBlank()) {
                            camposIncompletosError = "Por favor, completa todos los campos obligatorios (Servicio, Especialista, Fecha y Hora)."
                            todoValido = false
                        } else {
                            camposIncompletosError = null
                        }

                        if (todoValido && nombreError == null && telefonoError == null) {
                            showConfirm = true
                        }
                    },
                    enabled = !uiState.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RoseGold,
                        contentColor = White
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(color = White, modifier = Modifier.size(24.dp))
                    } else {
                        Icon(Icons.Default.CheckCircle, null, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = "CONFIRMAR RESERVA",
                            style = MaterialTheme.typography.labelLarge,
                            letterSpacing = 1.5.sp
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String, icon: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = icon, fontSize = 16.sp)
        Spacer(Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = CharcoalSoft
        )
        Spacer(Modifier.width(12.dp))
        HorizontalDivider(color = RoseGoldLight, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun SpaTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean = false,
    supportingText: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val esFecha = label.contains("Fecha")

    Box(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            leadingIcon = leadingIcon,
            isError = isError,
            enabled = !esFecha, // Desactivado si es fecha para transferir el clic al Box
            supportingText = {
                if (supportingText != null) {
                    Text(text = supportingText, color = MaterialTheme.colorScheme.error)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = RoseGold,
                unfocusedBorderColor = RoseGoldLight,
                disabledBorderColor = RoseGoldLight,
                focusedLabelColor = RoseGold,
                disabledLabelColor = CharcoalSoft,
                disabledTextColor = CharcoalSoft,
                cursorColor = RoseGold
            ),
            singleLine = true
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownSpa(
    label: String,
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit,
    icon: @Composable (() -> Unit)? = null
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            leadingIcon = icon,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = RoseGold,
                unfocusedBorderColor = RoseGoldLight,
                focusedLabelColor = RoseGold
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, color = CharcoalSoft) },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun ConfirmacionDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = White,
        shape = RoundedCornerShape(24.dp),
        icon = {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = null,
                tint = RoseGold,
                modifier = Modifier.size(40.dp)
            )
        },
        title = {
            Text(
                text = "Confirmar Reserva",
                fontFamily = FontFamily.Serif,
                color = CharcoalSoft
            )
        },
        text = {
            Text(
                text = "¿Deseas confirmar la reserva? Se registrará la cita en el sistema.",
                style = MaterialTheme.typography.bodyMedium,
                color = GreyWarm
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = RoseGold),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Confirmar", color = White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = GreyWarm)
            }
        }
    )
}