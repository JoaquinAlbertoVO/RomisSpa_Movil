package com.romisspa.app.data.remote.datasource

import com.romisspa.app.core.network.SpaApiService
import com.romisspa.app.data.remote.dto.*

class RemoteDataSource(private val api: SpaApiService) {

    suspend fun getServicios(): ApiResponse<List<ServicioDto>>? {
        return api.getServicios().body()
    }

    suspend fun addServicio(request: CreateServicioRequest) {
        api.addServicio(request)
    }

    suspend fun updateServicio(request: UpdateServicioRequest) {
        api.updateServicio(request)
    }

    suspend fun deleteServicio(nombre: String) {
        api.deleteServicio(nombre)
    }

    suspend fun getCitas(): ApiResponse<List<CitaDto>>? {
        return api.getCitas().body()
    }

    suspend fun addCita(request: CreateCitaRequest) {
        api.addCita(request)
    }

    suspend fun deleteCita(id: String) {
        api.deleteCita(id)
    }

    suspend fun getClientes(): ApiResponse<List<ClienteDto>>? {
        return api.getClientes().body()
    }

    suspend fun addOrUpdateCliente(cliente: ClienteDto) {
        api.addOrUpdateCliente(cliente)
    }
}
