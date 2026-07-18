package com.romisspa.app.presentation.screens.servicios

import com.romisspa.app.domain.model.Producto
import com.romisspa.app.domain.model.Servicio

data class ServiciosUiState(
    val servicios: List<Servicio> = emptyList(),
    val productos: List<Producto> = emptyList(),
    val isLoading: Boolean = false,
    val searchText: String = "",
    val showAddDialog: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)
