package com.romisspa.app.presentation.screens.limpieza

import com.romisspa.app.domain.model.Empleado
import com.romisspa.app.domain.model.RutinaDiaria
import com.romisspa.app.domain.model.TareaEspecial

data class LimpiezaUiState(
    val isLoading: Boolean = false,
    val rutinasDiarias: List<RutinaDiaria> = emptyList(),
    val tareasEspeciales: List<TareaEspecial> = emptyList(),
    val empleados: List<Empleado> = emptyList(), // Para poder asignar tareas
    val errorMessage: String? = null
)
