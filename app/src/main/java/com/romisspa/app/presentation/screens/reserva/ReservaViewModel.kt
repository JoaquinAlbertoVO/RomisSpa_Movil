package com.romisspa.app.presentation.screens.reserva

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romisspa.app.domain.model.Cita
import com.romisspa.app.domain.model.Cliente
import com.romisspa.app.domain.model.Empleado
import com.romisspa.app.domain.model.Servicio
import com.romisspa.app.domain.usecase.SpaUseCases
import com.romisspa.app.presentation.event.EventBus
import com.romisspa.app.presentation.event.UiEvent
import com.romisspa.app.ui.theme.WarmGold
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReservaViewModel(
    private val useCases: SpaUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReservaUiState())
    val uiState = _uiState.asStateFlow()

    private val _serviciosAvailable = MutableStateFlow<List<Servicio>>(emptyList())
    val serviciosAvailable = _serviciosAvailable.asStateFlow()

    private val _empleadosAvailable = MutableStateFlow<List<Empleado>>(emptyList())
    val empleadosAvailable = _empleadosAvailable.asStateFlow()

    private val _clientesAvailable = MutableStateFlow<List<Cliente>>(emptyList())
    val clientesAvailable = _clientesAvailable.asStateFlow()

    init {
        loadServicios()
        loadEmpleados()
        loadClientes()
    }

    private fun loadServicios() {
        viewModelScope.launch {
            try {
                val servicios = useCases.getServicios()
                _serviciosAvailable.value = servicios
            } catch (e: Exception) {
                EventBus.send(UiEvent.Error("Error al cargar servicios: ${e.message}"))
            }
        }
    }

    private fun loadEmpleados() {
        viewModelScope.launch {
            try {
                val empleados = useCases.getEmpleados()
                _empleadosAvailable.value = empleados
            } catch (e: Exception) {
                EventBus.send(UiEvent.Error("Error al cargar empleados: ${e.message}"))
            }
        }
    }

    private fun loadClientes() {
        viewModelScope.launch {
            try {
                val clientes = useCases.getClientes()
                _clientesAvailable.value = clientes
            } catch (e: Exception) {
                EventBus.send(UiEvent.Error("Error al cargar clientes: ${e.message}"))
            }
        }
    }

    fun onIsNuevoClienteChange(isNuevo: Boolean) {
        _uiState.update { it.copy(isNuevoCliente = isNuevo, clienteNombre = "", clienteTelefono = "") }
    }

    fun onClienteExistenteSelected(cliente: Cliente) {
        _uiState.update { it.copy(clienteNombre = cliente.nombre, clienteTelefono = cliente.telefono) }
    }

    fun onNombreChange(value: String) {
        _uiState.update { it.copy(clienteNombre = value) }
    }

    fun onTelefonoChange(value: String) {
        _uiState.update { it.copy(clienteTelefono = value) }
    }

    fun onServicioChange(value: String) {
        _uiState.update { it.copy(servicioSelected = value) }
    }

    fun onEmpleadaChange(value: String) {
        _uiState.update { it.copy(empleadaSelected = value) }
    }

    fun onFechaChange(value: String) {
        _uiState.update { it.copy(fechaSelected = value) }
    }

    fun onHoraChange(value: String) {
        _uiState.update { it.copy(horaSelected = value) }
    }

    fun onNotasChange(value: String) {
        _uiState.update { it.copy(notasExtra = value) }
    }

    fun saveReserva() {
        viewModelScope.launch {
            if (!validateForm()) return@launch

            try {
                _uiState.update { it.copy(isLoading = true) }

                val state = _uiState.value

                // 1. Agregar Cita
                useCases.addCita(
                    Cita(
                        cliente = state.clienteNombre,
                        telefono = state.clienteTelefono,
                        servicio = state.servicioSelected,
                        fecha = state.fechaSelected,
                        hora = state.horaSelected,
                        estado = "Pendiente",
                        colorEstado = WarmGold
                    )
                )

                // 2. Agregar o actualizar cliente
                val clienteExistente = useCases.getClientes().find {
                    it.nombre.equals(state.clienteNombre, ignoreCase = true)
                }

                if (clienteExistente != null) {
                    useCases.addOrUpdateCliente(clienteExistente.copy(
                        telefono = state.clienteTelefono,
                        ultimaVisita = state.fechaSelected,
                        totalVisitas = clienteExistente.totalVisitas + 1
                    ))
                } else {
                    useCases.addOrUpdateCliente(
                        com.romisspa.app.domain.model.Cliente(
                            state.clienteNombre,
                            state.clienteTelefono,
                            state.fechaSelected,
                            1
                        )
                    )
                }

                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                EventBus.send(UiEvent.Success("Reserva confirmada con éxito"))

            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
                EventBus.send(UiEvent.Error("Error al procesar la reserva: ${e.message}"))
            }
        }
    }

    private suspend fun validateForm(): Boolean {
        val state = _uiState.value
        return when {
            state.clienteNombre.isBlank() -> {
                EventBus.send(UiEvent.Warning("Ingrese el nombre del cliente"))
                false
            }
            state.clienteTelefono.isBlank() -> {
                EventBus.send(UiEvent.Warning("Ingrese el teléfono"))
                false
            }
            state.servicioSelected.isBlank() -> {
                EventBus.send(UiEvent.Warning("Seleccione un servicio"))
                false
            }
            state.fechaSelected.isBlank() -> {
                EventBus.send(UiEvent.Warning("Seleccione la fecha"))
                false
            }
            state.horaSelected.isBlank() -> {
                EventBus.send(UiEvent.Warning("Seleccione la hora"))
                false
            }
            else -> true
        }
    }

    fun clearSuccess() {
        _uiState.update { it.copy(isSuccess = false) }
    }
}
