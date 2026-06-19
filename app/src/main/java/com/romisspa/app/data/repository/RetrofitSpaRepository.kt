package com.romisspa.app.data.repository

import com.romisspa.app.data.remote.datasource.RemoteDataSource
import com.romisspa.app.data.remote.dto.CreateCitaRequest
import com.romisspa.app.data.remote.dto.CreateServicioRequest
import com.romisspa.app.data.remote.dto.UpdateServicioRequest
import com.romisspa.app.data.mapper.toDomain
import com.romisspa.app.data.mapper.toDto
import com.romisspa.app.domain.model.Cita
import com.romisspa.app.domain.model.Cliente
import com.romisspa.app.domain.model.Servicio
import com.romisspa.app.domain.repository.SpaRepository

class RetrofitSpaRepository(
    private val remoteDataSource: RemoteDataSource
) : SpaRepository {

    override suspend fun getServicios(): List<Servicio> {
        return remoteDataSource.getServicios()?.data?.map { it.toDomain() } ?: emptyList()
    }

    override suspend fun addServicio(servicio: Servicio) {
        val request = CreateServicioRequest(
            nombre = servicio.nombre,
            descripcion = servicio.descripcion,
            // Convertimos "S/ 45.00" a número 45.00
            precio = servicio.precio.replace("S/ ", "").toDoubleOrNull() ?: 0.0,
            imagenRes = null // El modelo Servicio no tiene imagen
        )
        remoteDataSource.addServicio(request)
    }

    override suspend fun updateServicio(servicio: Servicio) {
        val request = UpdateServicioRequest(
            nombre = servicio.nombre,
            descripcion = servicio.descripcion,
            // Convertimos "S/ 45.00" a número 45.00
            precio = servicio.precio.replace("S/ ", "").toDoubleOrNull() ?: 0.0,
            imagenRes = null // El modelo Servicio no tiene imagen
        )
        remoteDataSource.updateServicio(request)
    }

    override suspend fun deleteServicio(servicio: Servicio) {
        remoteDataSource.deleteServicio(servicio.nombre)
    }

    override suspend fun getCitas(): List<Cita> {
        return remoteDataSource.getCitas()?.data?.map { it.toDomain() } ?: emptyList()
    }

    override suspend fun addCita(cita: Cita) {
        val request = CreateCitaRequest(
            cliente = cita.cliente,
            servicio = cita.servicio,
            fecha = cita.fecha,
            hora = cita.hora
        )
        remoteDataSource.addCita(request)
    }

    override suspend fun deleteCita(cita: Cita) {
        // Usamos el ID único para borrar la cita correctamente
        remoteDataSource.deleteCita(cita.id)
    }

    override suspend fun getClientes(): List<Cliente> {
        return remoteDataSource.getClientes()?.data?.map { it.toDomain() } ?: emptyList()
    }

    override suspend fun addOrUpdateCliente(cliente: Cliente) {
        remoteDataSource.addOrUpdateCliente(cliente.toDto())
    }
}
