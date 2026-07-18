package com.romisspa.app.presentation.screens.inventario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romisspa.app.presentation.event.EventBus
import com.romisspa.app.presentation.event.UiEvent
import com.romisspa.app.domain.model.Producto
import com.romisspa.app.domain.usecase.SpaUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class InventarioViewModel(private val useCases: SpaUseCases) : ViewModel() {

    private val _uiState = MutableStateFlow(InventarioUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getProductos()
    }

    fun getProductos() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val productos = useCases.getProductos()
                _uiState.update { it.copy(productos = productos, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
                EventBus.send(UiEvent.Error("Error al cargar productos: ${e.message}"))
            }
        }
    }

    fun addProducto(producto: Producto) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            try {
                useCases.addProducto(producto)
                kotlinx.coroutines.delay(1000) // Esperar a que el backend se sincronice
                getProductos()
                EventBus.send(UiEvent.Success("Producto agregado con éxito"))
            } catch (e: Exception) {
                EventBus.send(UiEvent.Error("Error al agregar producto: ${e.message}"))
            } finally {
                _uiState.update { it.copy(isSaving = false) }
            }
        }
    }

    fun updateProducto(producto: Producto) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            try {
                useCases.updateProducto(producto)
                kotlinx.coroutines.delay(500)
                getProductos()
                EventBus.send(UiEvent.Success("Producto actualizado"))
            } catch (e: Exception) {
                EventBus.send(UiEvent.Error("Error al actualizar: ${e.message}"))
            } finally {
                _uiState.update { it.copy(isSaving = false) }
            }
        }
    }

    fun deleteProducto(productoId: String) {
        viewModelScope.launch {
            try {
                useCases.deleteProducto(productoId)
                kotlinx.coroutines.delay(500)
                getProductos()
                EventBus.send(UiEvent.Success("Producto eliminado"))
            } catch (e: Exception) {
                EventBus.send(UiEvent.Error("Error al eliminar: ${e.message}"))
            }
        }
    }
}
