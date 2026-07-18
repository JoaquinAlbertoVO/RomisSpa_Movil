package com.romisspa.app.presentation.screens.limpieza

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.romisspa.app.domain.model.Empleado
import com.romisspa.app.ui.theme.CharcoalSoft
import com.romisspa.app.ui.theme.RoseGold
import com.romisspa.app.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRutinaDiariaDialog(
    empleados: List<Empleado>,
    onDismiss: () -> Unit,
    onConfirm: (tarea: String, empleadoId: String, empleadoNombre: String) -> Unit
) {
    var tarea by remember { mutableStateOf("") }
    var selectedEmpleado by remember { mutableStateOf<Empleado?>(empleados.firstOrNull()) }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva Tarea Diaria") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = tarea,
                    onValueChange = { tarea = it },
                    label = { Text("Nombre de la tarea (Ej: Pisos)") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedEmpleado?.nombre ?: "Seleccionar Empleada",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Asignar a") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        empleados.forEach { emp ->
                            DropdownMenuItem(
                                text = { Text(emp.nombre) },
                                onClick = {
                                    selectedEmpleado = emp
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { 
                    if (tarea.isNotBlank() && selectedEmpleado != null) {
                        onConfirm(tarea, selectedEmpleado!!.id, selectedEmpleado!!.nombre)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = RoseGold)
            ) {
                Text("Añadir")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = CharcoalSoft)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTareaEspecialDialog(
    empleados: List<Empleado>,
    onDismiss: () -> Unit,
    onConfirm: (tarea: String, empleadoId: String, empleadoNombre: String, fecha: String) -> Unit
) {
    var tarea by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var selectedEmpleado by remember { mutableStateOf<Empleado?>(empleados.firstOrNull()) }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva Tarea Especial") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = tarea,
                    onValueChange = { tarea = it },
                    label = { Text("Tarea (Ej: Lavar toallas)") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = fecha,
                    onValueChange = { fecha = it },
                    label = { Text("Fecha (Ej: 2026-07-20)") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedEmpleado?.nombre ?: "Seleccionar Empleada",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Asignar a") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        empleados.forEach { emp ->
                            DropdownMenuItem(
                                text = { Text(emp.nombre) },
                                onClick = {
                                    selectedEmpleado = emp
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { 
                    if (tarea.isNotBlank() && selectedEmpleado != null && fecha.isNotBlank()) {
                        onConfirm(tarea, selectedEmpleado!!.id, selectedEmpleado!!.nombre, fecha)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = RoseGold)
            ) {
                Text("Añadir")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = CharcoalSoft)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PinDialog(
    isError: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var pin by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Supervisión de Administrador") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Ingresa el PIN para confirmar que la tarea se completó correctamente.")
                OutlinedTextField(
                    value = pin,
                    onValueChange = { if (it.length <= 4) pin = it },
                    label = { Text("PIN") },
                    isError = isError,
                    supportingText = {
                        if (isError) Text("PIN incorrecto", color = MaterialTheme.colorScheme.error)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(pin) },
                colors = ButtonDefaults.buttonColors(containerColor = RoseGold)
            ) {
                Text("Aprobar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = CharcoalSoft)
            }
        }
    )
}
