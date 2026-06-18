package com.romisspa.app.domain.repository

import com.romisspa.app.domain.model.Cita
import com.romisspa.app.domain.model.Cliente
import com.romisspa.app.domain.model.Servicio

interface SpaRepository {
    fun getServicios(): List<Servicio>
    fun addServicio(servicio: Servicio)
    fun updateServicio(servicio: Servicio)
    fun deleteServicio(servicio: Servicio)
    
    fun getCitas(): List<Cita>
    fun addCita(cita: Cita)
    fun deleteCita(cita: Cita)

    fun getClientes(): List<Cliente>
    fun addOrUpdateCliente(cliente: Cliente)
}
