package com.romisspa.app.presentation.screens.caja

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romisspa.app.domain.usecase.SpaUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class CajaViewModel(
    private val useCases: SpaUseCases,
    context: Context
) : ViewModel() {

    private val prefs: SharedPreferences = context.getSharedPreferences("caja_prefs", Context.MODE_PRIVATE)
    
    private val _uiState = MutableStateFlow(CajaUiState())
    val uiState: StateFlow<CajaUiState> = _uiState.asStateFlow()

    init {
        val savedApertura = prefs.getFloat("monto_apertura", 0f).toDouble()
        _uiState.update { it.copy(montoApertura = savedApertura) }
        loadVentasDia()
    }

    fun loadVentasDia() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val hoy = LocalDate.now().toString() // Formato YYYY-MM-DD
                val todasLasVentas = useCases.getVentas()
                
                // Filtrar ventas de hoy
                val ventasHoy = todasLasVentas.filter { it.fecha.startsWith(hoy) }
                
                val totalEfectivo = ventasHoy.filter { it.metodoPago.equals("Efectivo", ignoreCase = true) }.sumOf { it.monto }
                val totalYape = ventasHoy.filter { it.metodoPago.equals("Yape", ignoreCase = true) }.sumOf { it.monto }
                val totalPlin = ventasHoy.filter { it.metodoPago.equals("Plin", ignoreCase = true) }.sumOf { it.monto }
                val totalTarjeta = ventasHoy.filter { it.metodoPago.equals("Tarjeta", ignoreCase = true) }.sumOf { it.monto }
                val totalTransferencia = ventasHoy.filter { it.metodoPago.equals("Transferencia", ignoreCase = true) }.sumOf { it.monto }
                
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        totalVentasDia = ventasHoy.sumOf { v -> v.monto },
                        ventasEfectivo = totalEfectivo,
                        ventasYape = totalYape,
                        ventasPlin = totalPlin,
                        ventasTarjeta = totalTarjeta,
                        ventasTransferencia = totalTransferencia
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun enableEditApertura() {
        _uiState.update { it.copy(isEditingApertura = true) }
    }

    fun saveMontoApertura(nuevoMonto: Double) {
        prefs.edit().putFloat("monto_apertura", nuevoMonto.toFloat()).apply()
        _uiState.update { 
            it.copy(
                montoApertura = nuevoMonto,
                isEditingApertura = false
            )
        }
    }
}
