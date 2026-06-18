package com.romisspa.app.domain.repository

import com.romisspa.app.domain.model.Cita
import com.romisspa.app.domain.model.Cliente
import com.romisspa.app.domain.model.Servicio

interface SpaRepository {
    suspend fun getServicios(): List<Servicio>
    suspend fun addServicio(servicio: Servicio)
    suspend fun updateServicio(servicio: Servicio)
    suspend fun deleteServicio(servicio: Servicio)
    
    suspend fun getCitas(): List<Cita>
    suspend fun addCita(cita: Cita)
    suspend fun deleteCita(cita: Cita)

    suspend fun getClientes(): List<Cliente>
    suspend fun addOrUpdateCliente(cliente: Cliente)
}
