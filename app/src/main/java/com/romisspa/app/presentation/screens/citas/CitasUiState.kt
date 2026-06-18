package com.romisspa.app.presentation.screens.citas

import com.romisspa.app.domain.model.Cita

data class CitasUiState(
    val citas: List<Cita> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
