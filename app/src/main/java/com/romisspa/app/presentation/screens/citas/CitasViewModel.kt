package com.romisspa.app.presentation.screens.citas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romisspa.app.domain.model.Cita
import com.romisspa.app.domain.model.Insumo
import com.romisspa.app.domain.usecase.SpaUseCases
import com.romisspa.app.presentation.event.EventBus
import com.romisspa.app.presentation.event.UiEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CitasViewModel(
    private val useCases: SpaUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(CitasUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getCitas()
        getServicios()
        getEmpleados()
        getProductos()
    }

    fun getCitas() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val citas = useCases.getCitas()
                _uiState.update { it.copy(citas = citas, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
                EventBus.send(UiEvent.Error("Error al cargar citas: ${e.message}"))
            }
        }
    }

    fun getServicios() {
        viewModelScope.launch {
            try {
                val servicios = useCases.getServicios()
                _uiState.update { it.copy(servicios = servicios) }
            } catch (e: Exception) {
                // No es crítico para la carga de citas
            }
        }
    }

    fun getEmpleados() {
        viewModelScope.launch {
            try {
                val empleados = useCases.getEmpleados()
                _uiState.update { it.copy(empleados = empleados) }
            } catch (e: Exception) {
                // No es crítico
            }
        }
    }

    fun getProductos() {
        viewModelScope.launch {
            try {
                val productos = useCases.getProductos()
                _uiState.update { it.copy(productos = productos) }
            } catch (e: Exception) {
                // No es crítico
            }
        }
    }

    fun deleteCita(cita: Cita) {
        viewModelScope.launch {
            try {
                useCases.deleteCita(cita)
                _uiState.update { state ->
                    state.copy(citas = state.citas.filter { it != cita })
                }
                EventBus.send(UiEvent.Success("Cita eliminada correctamente"))
            } catch (e: Exception) {
                EventBus.send(UiEvent.Error("No se pudo eliminar la cita: ${e.message}"))
            }
        }
    }

    fun confirmarCita(cita: Cita) {
        viewModelScope.launch {
            try {
                useCases.updateCitaStatus(cita.id, "Confirmada")
                _uiState.update { state ->
                    state.copy(citas = state.citas.map {
                        if (it.id == cita.id) it.copy(estado = "Confirmada", colorEstado = androidx.compose.ui.graphics.Color(0xFF4CAF50))
                        else it
                    })
                }
                EventBus.send(UiEvent.Success("Cita confirmada correctamente"))
            } catch (e: Exception) {
                EventBus.send(UiEvent.Error("No se pudo confirmar la cita: ${e.message}"))
            }
        }
    }

    fun enviarRecordatorio(cita: Cita) {
        val mensaje = "Hola ${cita.cliente}, te recordamos tu cita para ${cita.servicio} el día ${cita.fecha} a las ${cita.hora}. ¡Te esperamos!"
        viewModelScope.launch {
            EventBus.send(UiEvent.OpenWhatsApp(cita.telefono, mensaje))
        }
    }

    /**
     * Abre el diálogo de Atender y pre-carga los insumos de la receta del servicio.
     */
    fun showAtenderDialog(cita: Cita) {
        val servicioEncontrado = _uiState.value.servicios.find {
            it.nombre.equals(cita.servicio, ignoreCase = true)
        }
        _uiState.update {
            it.copy(
                showAtenderDialog = true,
                selectedCita = cita,
                insumosParaAtender = servicioEncontrado?.insumos ?: emptyList()
            )
        }
    }

    fun dismissAtenderDialog() {
        _uiState.update { it.copy(showAtenderDialog = false, selectedCita = null, insumosParaAtender = emptyList()) }
    }

    /**
     * Marca la cita como Atendida, registra la Venta y descuenta los insumos del inventario.
     * [insumosFinales] es la lista editada por la recepcionista (puede diferir de la receta).
     */
    fun marcarComoAtendida(
        cita: Cita,
        monto: Double,
        metodoPago: String,
        empleadoId: String? = null,
        insumosFinales: List<Insumo>,
        serviciosFinales: String
    ) {
        viewModelScope.launch {
            try {
                // 1. Actualizar estado de la cita
                useCases.updateCitaStatus(cita.id, "Atendida")

                // 2. Registrar la venta
                val nuevaVenta = com.romisspa.app.domain.model.Venta(
                    cliente = cita.cliente,
                    servicio = serviciosFinales,
                    monto = monto,
                    metodoPago = metodoPago,
                    empleadoId = empleadoId ?: cita.empleadoId,
                    fecha = java.time.LocalDate.now().toString()
                )
                useCases.addVenta(nuevaVenta)

                // 3. Descontar insumos del inventario (solo los que tengan productoId)
                val insumosValidos = insumosFinales.filter { it.productoId.isNotEmpty() && it.cantidad > 0 }
                if (insumosValidos.isNotEmpty()) {
                    useCases.descontarInsumos(insumosValidos)
                }

                // 4. Actualizar UI
                _uiState.update { state ->
                    state.copy(
                        citas = state.citas.filter { it.id != cita.id },
                        showAtenderDialog = false,
                        selectedCita = null,
                        insumosParaAtender = emptyList()
                    )
                }
                EventBus.send(UiEvent.Success("Cita atendida y venta registrada por S/ $monto"))
            } catch (e: Exception) {
                EventBus.send(UiEvent.Error("Error al procesar atención: ${e.message}"))
            }
        }
    }
}
