package com.romisspa.app.presentation.screens.clientes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.romisspa.app.domain.model.Cliente
import com.romisspa.app.ui.theme.*

@Composable
fun ClientesScreen(
    viewModel: ClientesViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    val filteredClientes = uiState.clientes.filter {
        it.nombre.contains(uiState.searchText, ignoreCase = true) || it.telefono.contains(uiState.searchText)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(colors = listOf(Cream, CreamDark)))
        ) {
            // Buscador
            OutlinedTextField(
                value = uiState.searchText,
                onValueChange = viewModel::onSearchTextChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Buscar cliente por nombre o teléfono...") },
                leadingIcon = { Icon(Icons.Default.Search, null, tint = GreyWarm) },
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RoseGold,
                    unfocusedBorderColor = RoseGoldLight
                )
            )

            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(500)) + slideInVertically(initialOffsetY = { 50 })
            ) {
                if (uiState.isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = RoseGold)
                    }
                } else if (filteredClientes.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "No se encontraron clientes", color = GreyWarm)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filteredClientes) { cliente ->
                            ClienteItem(cliente)
                        }
                    }
                }
            }
        }

        // Botón Flotante para añadir cliente
        FloatingActionButton(
            onClick = { /* TODO: Implementar AddClienteDialog */ },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            containerColor = RoseGold,
            contentColor = White
        ) {
            Icon(Icons.Default.Add, contentDescription = "Añadir Cliente")
        }
    }
}

@Composable
fun ClienteItem(cliente: Cliente) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar con inicial
            Box(
                modifier = Modifier
                    .size(45.dp)
                    .clip(CircleShape)
                    .background(RoseGold.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = cliente.nombre.take(1).uppercase(),
                    color = RoseGold,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = cliente.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    color = CharcoalSoft,
                    fontWeight = FontWeight.SemiBold
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Phone, null, modifier = Modifier.size(14.dp), tint = GreyWarm)
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = cliente.telefono,
                        style = MaterialTheme.typography.bodySmall,
                        color = GreyWarm
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                var showOptions by remember { mutableStateOf(false) }

                Text(
                    text = "${cliente.totalVisitas} visitas",
                    style = MaterialTheme.typography.labelSmall,
                    color = RoseGold,
                    fontWeight = FontWeight.Bold
                )

                Box {
                    IconButton(onClick = { showOptions = true }) {
                        Icon(Icons.Default.MoreVert, null, tint = GreyWarm, modifier = Modifier.size(20.dp))
                    }

                    DropdownMenu(
                        expanded = showOptions,
                        onDismissRequest = { showOptions = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Eliminar Cliente", color = Color.Red) },
                            onClick = {
                                // TODO: Implement delete in ClientesViewModel if needed
                                showOptions = false
                            }
                        )
                    }
                }
            }
        }
    }
}
