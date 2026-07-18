package com.romisspa.app.presentation.screens.caja

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.romisspa.app.ui.theme.*
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CajaScreen(
    viewModel: CajaViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.loadVentasDia()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Flujo de Caja - Hoy", color = MaterialTheme.colorScheme.onBackground) },
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
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Apertura de Caja
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Monto de Apertura",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold
                            )
                            if (!uiState.isEditingApertura) {
                                IconButton(onClick = { viewModel.enableEditApertura() }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Editar Apertura", tint = RoseGold)
                                }
                            }
                        }

                        if (uiState.isEditingApertura) {
                            var input by remember { mutableStateOf(uiState.montoApertura.toString()) }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = input,
                                    onValueChange = { input = it },
                                    label = { Text("Monto inicial (S/)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f),
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = RoseGold,
                                        unfocusedBorderColor = RoseGoldLight
                                    )
                                )
                                Spacer(Modifier.width(8.dp))
                                Button(
                                    onClick = { 
                                        val monto = input.toDoubleOrNull() ?: 0.0
                                        viewModel.saveMontoApertura(monto)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = RoseGold)
                                ) {
                                    Text("Guardar", color = MaterialTheme.colorScheme.onPrimary)
                                }
                            }
                        } else {
                            Text(
                                text = "S/ ${"%.2f".format(uiState.montoApertura)}",
                                style = MaterialTheme.typography.displaySmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "Este es el fondo o sencillo con el que se abrió la caja hoy.",
                                style = MaterialTheme.typography.bodySmall,
                                color = GreyWarm
                            )
                        }
                    }
                }

                // Resumen de Ventas
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Ingresos del Día",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(16.dp))

                        DesgloseRow(label = "Efectivo", monto = uiState.ventasEfectivo, isHighlight = true)
                        DesgloseRow(label = "Yape", monto = uiState.ventasYape)
                        DesgloseRow(label = "Plin", monto = uiState.ventasPlin)
                        DesgloseRow(label = "Tarjeta", monto = uiState.ventasTarjeta)
                        if (uiState.ventasTransferencia > 0) {
                            DesgloseRow(label = "Transferencia", monto = uiState.ventasTransferencia)
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = RoseGoldLight.copy(alpha = 0.5f))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total Ventas (Todos los medios)", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface)
                            Text("S/ ${"%.2f".format(uiState.totalVentasDia)}", fontWeight = FontWeight.Bold, color = RoseGoldDark)
                        }
                    }
                }

                // Cierre Estimado
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = RoseGold.copy(alpha = 0.1f)),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "EFECTIVO ESPERADO EN CAJA",
                            style = MaterialTheme.typography.labelMedium,
                            color = RoseGoldDark,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "S/ ${"%.2f".format(uiState.totalEsperadoEfectivo)}",
                            style = MaterialTheme.typography.displayMedium,
                            color = RoseGoldDark,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "(Monto de Apertura + Ingresos en Efectivo)",
                            style = MaterialTheme.typography.bodySmall,
                            color = GreyWarm
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DesgloseRow(label: String, monto: Double, isHighlight: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label, 
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Normal,
            color = if (isHighlight) MaterialTheme.colorScheme.onSurface else GreyWarm
        )
        Text(
            text = "S/ ${"%.2f".format(monto)}",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Medium,
            color = if (isHighlight) Color(0xFF4CAF50) else MaterialTheme.colorScheme.onSurface
        )
    }
}
