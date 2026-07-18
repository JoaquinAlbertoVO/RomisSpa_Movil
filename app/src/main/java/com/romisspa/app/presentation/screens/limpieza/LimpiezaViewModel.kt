package com.romisspa.app.presentation.screens.limpieza

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romisspa.app.domain.model.RutinaDiaria
import com.romisspa.app.domain.model.TareaEspecial
import com.romisspa.app.domain.usecase.SpaUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class LimpiezaViewModel(
    private val useCases: SpaUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(LimpiezaUiState())
    val uiState: StateFlow<LimpiezaUiState> = _uiState.asStateFlow()

    private val ADMIN_PIN = "0626"

    init {
        loadData()
    }

    private fun getTodayString(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val todayStr = getTodayString()
                val rawRutinas = useCases.getRutinasDiarias()
                
                // Si la rutina diaria tiene una fecha distinta a hoy, la reiniciamos a PENDIENTE
                val rutinasProcesadas = rawRutinas.map { rutina ->
                    if (rutina.fechaActualizacion != todayStr) {
                        // Guardamos el cambio en la base de datos para que persista
                        val resetRutina = rutina.copy(fechaActualizacion = todayStr, estadoActual = "PENDIENTE")
                        useCases.addOrUpdateRutinaDiaria(resetRutina)
                        resetRutina
                    } else {
                        rutina
                    }
                }
                
                val tareas = useCases.getTareasEspeciales()
                val empleados = useCases.getEmpleados()

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        rutinasDiarias = rutinasProcesadas.sortedBy { r -> r.estadoActual != "PENDIENTE" },
                        tareasEspeciales = tareas.sortedBy { t -> t.estado != "PENDIENTE" },
                        empleados = empleados
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    // ─── RUTINAS DIARIAS ──────────────────────────────────────────────

    fun addRutinaDiaria(tarea: String, empleadoId: String, empleadoNombre: String) {
        viewModelScope.launch {
            try {
                val nuevaRutina = RutinaDiaria(
                    id = UUID.randomUUID().toString(),
                    tarea = tarea,
                    empleadoId = empleadoId,
                    empleadoNombre = empleadoNombre,
                    fechaActualizacion = getTodayString(),
                    estadoActual = "PENDIENTE"
                )
                useCases.addOrUpdateRutinaDiaria(nuevaRutina)
                loadData()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message) }
            }
        }
    }

    fun deleteRutinaDiaria(id: String) {
        viewModelScope.launch {
            try {
                useCases.deleteRutinaDiaria(id)
                loadData()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message) }
            }
        }
    }

    // Flujo: PENDIENTE -> POR_SUPERVISAR
    fun solicitarSupervisionRutina(rutina: RutinaDiaria) {
        viewModelScope.launch {
            try {
                val updated = rutina.copy(estadoActual = "POR_SUPERVISAR", fechaActualizacion = getTodayString())
                useCases.addOrUpdateRutinaDiaria(updated)
                loadData()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message) }
            }
        }
    }

    // Flujo: POR_SUPERVISAR -> COMPLETADA (Requiere PIN)
    fun aprobarSupervisionRutina(rutina: RutinaDiaria, pinInput: String): Boolean {
        if (pinInput == ADMIN_PIN) {
            viewModelScope.launch {
                try {
                    val updated = rutina.copy(estadoActual = "COMPLETADA", fechaActualizacion = getTodayString())
                    useCases.addOrUpdateRutinaDiaria(updated)
                    loadData()
                } catch (e: Exception) {
                    _uiState.update { it.copy(errorMessage = e.message) }
                }
            }
            return true
        }
        return false
    }

    // ─── TAREAS ESPECIALES ────────────────────────────────────────────

    fun addTareaEspecial(tarea: String, empleadoId: String, empleadoNombre: String, fechaAsignada: String) {
        viewModelScope.launch {
            try {
                val nuevaTarea = TareaEspecial(
                    id = UUID.randomUUID().toString(),
                    tarea = tarea,
                    empleadoId = empleadoId,
                    empleadoNombre = empleadoNombre,
                    fechaAsignada = fechaAsignada,
                    estado = "PENDIENTE"
                )
                useCases.addOrUpdateTareaEspecial(nuevaTarea)
                loadData()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message) }
            }
        }
    }

    fun deleteTareaEspecial(id: String) {
        viewModelScope.launch {
            try {
                useCases.deleteTareaEspecial(id)
                loadData()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message) }
            }
        }
    }

    // Flujo: PENDIENTE -> POR_SUPERVISAR
    fun solicitarSupervisionTareaEspecial(tarea: TareaEspecial) {
        viewModelScope.launch {
            try {
                val updated = tarea.copy(estado = "POR_SUPERVISAR")
                useCases.addOrUpdateTareaEspecial(updated)
                loadData()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message) }
            }
        }
    }

    // Flujo: POR_SUPERVISAR -> COMPLETADA (Requiere PIN)
    fun aprobarSupervisionTareaEspecial(tarea: TareaEspecial, pinInput: String): Boolean {
        if (pinInput == ADMIN_PIN) {
            viewModelScope.launch {
                try {
                    val updated = tarea.copy(estado = "COMPLETADA")
                    useCases.addOrUpdateTareaEspecial(updated)
                    loadData()
                } catch (e: Exception) {
                    _uiState.update { it.copy(errorMessage = e.message) }
                }
            }
            return true
        }
        return false
    }
}
