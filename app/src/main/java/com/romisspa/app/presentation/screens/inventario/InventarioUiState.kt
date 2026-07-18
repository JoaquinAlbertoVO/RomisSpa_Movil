package com.romisspa.app.presentation.screens.inventario

import com.romisspa.app.domain.model.Producto

data class InventarioUiState(
    val productos: List<Producto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSaving: Boolean = false
)
