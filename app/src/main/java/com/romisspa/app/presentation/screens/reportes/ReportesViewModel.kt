package com.romisspa.app.presentation.screens.reportes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romisspa.app.domain.usecase.SpaUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

class ReportesViewModel(
    private val useCases: SpaUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReportesUiState())
    val uiState: StateFlow<ReportesUiState> = _uiState.asStateFlow()

    init {
        loadReportes()
    }

    fun loadReportes() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val ventas = useCases.getVentas()
                val citas = useCases.getCitas()
                val empleados = useCases.getEmpleados()
                
                val today = LocalDate.now()
                
                // Cálculo de la semana actual (Domingo a Sábado)
                val startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
                val endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY))
                
                val formatter = DateTimeFormatter.ISO_LOCAL_DATE
                
                val ventasSemana = ventas.filter { 
                    val fechaVenta = LocalDate.parse(it.fecha, formatter)
                    !fechaVenta.isBefore(startOfWeek) && !fechaVenta.isAfter(endOfWeek)
                }

                // Cálculo del mes actual
                val currentMonth = today.format(DateTimeFormatter.ofPattern("yyyy-MM"))
                val ventasMes = ventas.filter { it.fecha.startsWith(currentMonth) }
                
                val totalMes = ventasMes.sumOf { it.monto }
                val totalSemana = ventasSemana.sumOf { it.monto }
                
                val porMetodo = ventasMes.groupBy { it.metodoPago }
                    .mapValues { entry -> entry.value.sumOf { it.monto } }

                // Comisiones de la SEMANA (Lo que se paga el Sábado)
                val comisionesSemana = ventasSemana.filter { it.empleadoId != null }
                    .groupBy { it.empleadoId!! }
                    .mapValues { entry -> 
                        val emp = empleados.find { it.id == entry.key }
                        val porcentaje = (emp?.comision ?: 40.0) / 100.0
                        entry.value.sumOf { it.monto * porcentaje }
                    }
                    .mapKeys { entry -> 
                        empleados.find { it.id == entry.key }?.nombre ?: "Desconocido"
                    }

                val detallesComisionesSemana = ventasSemana.filter { it.empleadoId != null }
                    .groupBy { it.empleadoId!! }
                    .mapKeys { entry -> 
                        empleados.find { it.id == entry.key }?.nombre ?: "Desconocido"
                    }

                // Comisiones del MES (Histórico)
                val comisionesMes = ventasMes.filter { it.empleadoId != null }
                    .groupBy { it.empleadoId!! }
                    .mapValues { entry -> 
                        val emp = empleados.find { it.id == entry.key }
                        val porcentaje = (emp?.comision ?: 40.0) / 100.0
                        entry.value.sumOf { it.monto * porcentaje }
                    }
                    .mapKeys { entry -> 
                        empleados.find { it.id == entry.key }?.nombre ?: "Desconocido"
                    }

                val serviciosCount = citas.groupBy { it.servicio }
                    .map { (servicio, list) -> servicio to list.size }
                    .sortedByDescending { it.second }
                    .take(5)

                val gananciaAdminSemana = totalSemana - comisionesSemana.values.sum()
                val gananciaAdminMes = totalMes - comisionesMes.values.sum()

                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        totalVentasMes = totalMes,
                        totalVentasSemana = totalSemana,
                        comisionesSemana = comisionesSemana,
                        ventasPorMetodoPago = porMetodo,
                        comisionesPorEmpleado = comisionesMes,
                        detallesComisionesSemana = detallesComisionesSemana,
                        gananciaAdministracionMes = gananciaAdminMes,
                        gananciaAdministracionSemana = gananciaAdminSemana,
                        serviciosMasSolicitados = serviciosCount
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}
