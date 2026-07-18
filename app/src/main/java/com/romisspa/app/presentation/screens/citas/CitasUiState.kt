package com.romisspa.app.presentation.screens.citas

import com.romisspa.app.domain.model.Cita
import com.romisspa.app.domain.model.Empleado
import com.romisspa.app.domain.model.Insumo
import com.romisspa.app.domain.model.Producto
import com.romisspa.app.domain.model.Servicio

data class CitasUiState(
    val citas: List<Cita> = emptyList(),
    val servicios: List<Servicio> = emptyList(),
    val empleados: List<Empleado> = emptyList(),
    val productos: List<Producto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val showAtenderDialog: Boolean = false,
    val selectedCita: Cita? = null,
    // Insumos pre-cargados de la "receta" del servicio; el usuario puede editarlos en el diálogo
    val insumosParaAtender: List<Insumo> = emptyList()
)
