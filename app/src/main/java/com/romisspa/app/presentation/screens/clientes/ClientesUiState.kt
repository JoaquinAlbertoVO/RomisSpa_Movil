package com.romisspa.app.presentation.screens.clientes

import com.romisspa.app.domain.model.Cliente

data class ClientesUiState(
    val clientes: List<Cliente> = emptyList(),
    val isLoading: Boolean = false,
    val searchText: String = "",
    val error: String? = null
)
