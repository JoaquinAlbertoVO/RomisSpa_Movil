package com.romisspa.app.data.repository

import com.romisspa.app.data.local.dao.SpaDao
import com.romisspa.app.data.local.entities.*
import com.romisspa.app.domain.model.*
import com.romisspa.app.domain.repository.SpaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class RoomSpaRepository(private val spaDao: SpaDao) : SpaRepository {

    override suspend fun getServicios(): List<Servicio> = withContext(Dispatchers.IO) {
        spaDao.getServicios().map { it.toDomain() }
    }

    override suspend fun addServicio(servicio: Servicio) = withContext(Dispatchers.IO) {
        spaDao.insertServicio(servicio.toEntity())
        Unit
    }

    override suspend fun updateServicio(servicio: Servicio) = withContext(Dispatchers.IO) {
        spaDao.insertServicio(servicio.toEntity())
        Unit
    }

    override suspend fun deleteServicio(servicio: Servicio) = withContext(Dispatchers.IO) {
        spaDao.deleteServicio(servicio.toEntity())
        Unit
    }

    override suspend fun getCitas(): List<Cita> = withContext(Dispatchers.IO) {
        spaDao.getCitas().map { it.toDomain() }
    }

    override suspend fun addCita(cita: Cita) = withContext(Dispatchers.IO) {
        val citaConId = if (cita.id.isEmpty()) cita.copy(id = UUID.randomUUID().toString()) else cita
        spaDao.insertCita(citaConId.toEntity())
        Unit
    }

    override suspend fun deleteCita(cita: Cita) = withContext(Dispatchers.IO) {
        spaDao.deleteCita(cita.toEntity())
        Unit
    }

    override suspend fun updateCitaStatus(citaId: String, nuevoEstado: String) = withContext(Dispatchers.IO) {
        spaDao.updateCitaStatus(citaId, nuevoEstado)
        Unit
    }

    override suspend fun getClientes(): List<Cliente> = withContext(Dispatchers.IO) {
        spaDao.getClientes().map { it.toDomain() }
    }

    override suspend fun addOrUpdateCliente(cliente: Cliente) = withContext(Dispatchers.IO) {
        spaDao.insertCliente(cliente.toEntity())
        Unit
    }

    override suspend fun getVentas(): List<Venta> = withContext(Dispatchers.IO) {
        spaDao.getVentas().map { it.toDomain() }
    }

    override suspend fun addVenta(venta: Venta) = withContext(Dispatchers.IO) {
        val ventaConId = if (venta.id.isEmpty()) venta.copy(id = UUID.randomUUID().toString()) else venta
        spaDao.insertVenta(ventaConId.toEntity())
        Unit
    }

    override suspend fun getProductos(): List<Producto> = withContext(Dispatchers.IO) {
        spaDao.getProductos().map { it.toDomain() }
    }

    override suspend fun addProducto(producto: Producto) = withContext(Dispatchers.IO) {
        val productoConId = if (producto.id.isEmpty()) producto.copy(id = UUID.randomUUID().toString()) else producto
        spaDao.insertProducto(productoConId.toEntity())
        Unit
    }

    override suspend fun updateProducto(producto: Producto) = withContext(Dispatchers.IO) {
        spaDao.insertProducto(producto.toEntity())
        Unit
    }

    override suspend fun deleteProducto(productoId: String) = withContext(Dispatchers.IO) {
        spaDao.deleteProducto(productoId)
        Unit
    }

    override suspend fun getEmpleados(): List<Empleado> = withContext(Dispatchers.IO) {
        spaDao.getEmpleados().map { it.toDomain() }
    }

    override suspend fun addEmpleado(empleado: Empleado) = withContext(Dispatchers.IO) {
        val empleadoConId = if (empleado.id.isEmpty()) empleado.copy(id = UUID.randomUUID().toString()) else empleado
        spaDao.insertEmpleado(empleadoConId.toEntity())
        Unit
    }

    override suspend fun updateEmpleado(empleado: Empleado) = withContext(Dispatchers.IO) {
        spaDao.insertEmpleado(empleado.toEntity())
        Unit
    }

    override suspend fun deleteEmpleado(empleadoId: String) = withContext(Dispatchers.IO) {
        spaDao.deleteEmpleado(empleadoId)
        Unit
    }

    override suspend fun descontarInsumos(insumosUsados: List<Insumo>) = withContext(Dispatchers.IO) {
        // Implementar lógica de Room si se desea seguir usando local
    }

    override suspend fun getRutinasDiarias(): List<RutinaDiaria> = emptyList()
    override suspend fun addOrUpdateRutinaDiaria(rutina: RutinaDiaria) {}
    override suspend fun deleteRutinaDiaria(rutinaId: String) {}

    override suspend fun getTareasEspeciales(): List<TareaEspecial> = emptyList()
    override suspend fun addOrUpdateTareaEspecial(tarea: TareaEspecial) {}
    override suspend fun deleteTareaEspecial(tareaId: String) {}
}
