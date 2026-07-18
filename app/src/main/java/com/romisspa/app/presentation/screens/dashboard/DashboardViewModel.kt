package com.romisspa.app.presentation.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romisspa.app.domain.usecase.SpaUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class DashboardViewModel(
    private val useCases: SpaUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadDashboardData()
    }

    fun loadDashboardData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val today = LocalDate.now().toString() // Formato YYYY-MM-DD
                
                val ventas = useCases.getVentas()
                val clientes = useCases.getClientes()
                val citas = useCases.getCitas()
                val productos = useCases.getProductos()

                val ventasHoy = ventas.filter { it.fecha.contains(today) }
                val totalMontoHoy = ventasHoy.sumOf { it.monto }
                
                val citasHoyPendientes = citas.count { 
                    it.fecha.contains(today) && (it.estado == "Pendiente" || it.estado == "Confirmada") 
                }

                val stockBajo = productos.filter { it.stock <= 5 }

                _uiState.update { state ->
                    state.copy(
                        totalVentas = "S/ ${"%.2f".format(totalMontoHoy)}",
                        totalClientes = clientes.size.toString(),
                        citasPendientes = citasHoyPendientes.toString(),
                        stockAlerts = stockBajo,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
