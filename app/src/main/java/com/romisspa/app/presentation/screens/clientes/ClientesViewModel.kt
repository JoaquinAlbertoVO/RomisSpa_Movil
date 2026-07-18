package com.romisspa.app.presentation.screens.clientes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romisspa.app.domain.model.Cliente
import com.romisspa.app.domain.usecase.SpaUseCases
import com.romisspa.app.presentation.event.EventBus
import com.romisspa.app.presentation.event.UiEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ClientesViewModel(
    private val useCases: SpaUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(ClientesUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getClientes()
        loadVentas()
    }

    fun getClientes() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val clientes = useCases.getClientes()
                _uiState.update { it.copy(clientes = clientes, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
                EventBus.send(UiEvent.Error("Error al cargar clientes: ${e.message}"))
            }
        }
    }

    private fun loadVentas() {
        viewModelScope.launch {
            try {
                val ventas = useCases.getVentas()
                _uiState.update { it.copy(ventas = ventas) }
            } catch (e: Exception) {
                // Ignore error, fallback empty list is used
            }
        }
    }

    fun insertarCliente(nombre: String, telefono: String, onSuccess: () -> Unit) {
        val nombreLimpio = nombre.trim()
        val telefonoLimpio = telefono.trim()

        // Validación: Campos vacíos
        if (nombreLimpio.isBlank() || telefonoLimpio.isBlank()) {
            _uiState.update { it.copy(error = "Por favor, completa todos los campos.") }
            return
        }

        // Validación: El nombre no debe contener números
        val regexNombre = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$".toRegex()
        if (!nombreLimpio.matches(regexNombre)) {
            _uiState.update { it.copy(error = "El nombre no debe contener números ni caracteres especiales.") }
            return
        }

        // Validación: El teléfono debe tener exactamente 9 números
        val regexTelefono = "^[0-9]{9}$".toRegex()
        if (!telefonoLimpio.matches(regexTelefono)) {
            _uiState.update { it.copy(error = "El teléfono debe tener exactamente 9 dígitos numéricos.") }
            return
        }

        // Si pasa las validaciones, limpia errores previos e inicia la inserción
        _uiState.update { it.copy(error = null, isLoading = true) }

        viewModelScope.launch {
            try {
                val fechaHoy = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

                val nuevoCliente = Cliente(
                    nombre = nombreLimpio,
                    telefono = telefonoLimpio,
                    ultimaVisita = fechaHoy,
                    totalVisitas = 0
                )

                useCases.addOrUpdateCliente(nuevoCliente)
                getClientes()
                onSuccess() // Cierra el diálogo solo si se guardó con éxito en el servidor
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
                EventBus.send(UiEvent.Error("Error al guardar cliente: ${e.message}"))
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun onSearchTextChange(text: String) {
        _uiState.update { it.copy(searchText = text) }
    }
}