package com.romisspa.app.data.repository

import com.romisspa.app.core.network.SpaApiService
import com.romisspa.app.data.remote.dto.toDomain
import com.romisspa.app.data.remote.dto.toDto
import com.romisspa.app.domain.model.Cita
import com.romisspa.app.domain.model.Cliente
import com.romisspa.app.domain.model.Servicio
import com.romisspa.app.domain.repository.SpaRepository

class RetrofitSpaRepository(
    private val apiService: SpaApiService
) : SpaRepository {

    override suspend fun getServicios(): List<Servicio> {
        return apiService.getServicios().map { it.toDomain() }
    }

    override suspend fun addServicio(servicio: Servicio) {
        apiService.addServicio(servicio.toDto())
    }

    override suspend fun updateServicio(servicio: Servicio) {
        apiService.updateServicio(servicio.toDto())
    }

    override suspend fun deleteServicio(servicio: Servicio) {
        apiService.deleteServicio(servicio.nombre)
    }

    override suspend fun getCitas(): List<Cita> {
        return apiService.getCitas().map { it.toDomain() }
    }

    override suspend fun addCita(cita: Cita) {
        apiService.addCita(cita.toDto())
    }

    override suspend fun deleteCita(cita: Cita) {
        // En DynamoDB usualmente se usa una Partition Key (ej: cliente o id)
        apiService.deleteCita(cita.cliente)
    }

    override suspend fun getClientes(): List<Cliente> {
        return apiService.getClientes().map { it.toDomain() }
    }

    override suspend fun addOrUpdateCliente(cliente: Cliente) {
        apiService.addOrUpdateCliente(cliente.toDto())
    }
}
