package com.romisspa.app.data.repository

import com.romisspa.app.data.remote.datasource.RemoteDataSource
import com.romisspa.app.data.remote.dto.*
import com.romisspa.app.data.mapper.toDomain
import com.romisspa.app.data.mapper.toDto
import com.romisspa.app.domain.model.*
import com.romisspa.app.domain.repository.SpaRepository

class RetrofitSpaRepository(
    private val remoteDataSource: RemoteDataSource
) : SpaRepository {

    override suspend fun getServicios(): List<Servicio> {
        val response = remoteDataSource.getServicios()
        return response?.data?.map { it.toDomain() } ?: emptyList()
    }

    override suspend fun addServicio(servicio: Servicio) {
        val request = CreateServicioRequest(
            nombre = servicio.nombre,
            descripcion = servicio.descripcion,
            precio = servicio.precio.replace("S/ ", "").replace(",", ".").toDoubleOrNull() ?: 0.0,
            imagenRes = null
        )
        remoteDataSource.addServicio(request)
    }

    override suspend fun updateServicio(servicio: Servicio) {
        val request = UpdateServicioRequest(
            nombre = servicio.nombre,
            descripcion = servicio.descripcion,
            precio = servicio.precio.replace("S/ ", "").replace(",", ".").toDoubleOrNull() ?: 0.0,
            imagenRes = null
        )
        remoteDataSource.updateServicio(request)
    }

    override suspend fun deleteServicio(servicio: Servicio) {
        remoteDataSource.deleteServicio(servicio.nombre)
    }

    override suspend fun getCitas(): List<Cita> {
        val response = remoteDataSource.getCitas()
        return response?.data?.map { it.toDomain() } ?: emptyList()
    }

    override suspend fun addCita(cita: Cita) {
        val request = CreateCitaRequest(
            cliente = cita.cliente,
            telefono = cita.telefono,
            servicio = cita.servicio,
            fecha = cita.fecha,
            hora = cita.hora
        )
        remoteDataSource.addCita(request)
    }

    override suspend fun deleteCita(cita: Cita) {
        remoteDataSource.deleteCita(cita.id)
    }

    override suspend fun updateCitaStatus(citaId: String, nuevoEstado: String) {
        remoteDataSource.updateCitaStatus(citaId, UpdateCitaStatusRequest(nuevoEstado))
    }

    override suspend fun getClientes(): List<Cliente> {
        val response = remoteDataSource.getClientes()
        return response?.data?.map { it.toDomain() } ?: emptyList()
    }

    override suspend fun addOrUpdateCliente(cliente: Cliente) {
        remoteDataSource.addOrUpdateCliente(cliente.toDto())
    }

    override suspend fun getVentas(): List<Venta> {
        val response = remoteDataSource.getVentas()
        return response?.data?.map { it.toDomain() } ?: emptyList()
    }

    override suspend fun addVenta(venta: Venta) {
        val request = CreateVentaRequest(
            cliente = venta.cliente,
            servicio = venta.servicio,
            monto = venta.monto,
            empleadoId = venta.empleadoId,
            metodoPago = venta.metodoPago
        )
        remoteDataSource.addVenta(request)
    }

    override suspend fun getProductos(): List<Producto> {
        val response = remoteDataSource.getProductos()
        return response?.data?.map { it.toDomain() } ?: emptyList()
    }

    override suspend fun addProducto(producto: Producto) {
        val request = CreateProductoRequest(
            nombre = producto.nombre,
            descripcion = producto.descripcion,
            precio = producto.precio,
            stock = producto.stock,
            categoria = producto.categoria
        )
        remoteDataSource.addProducto(request)
    }

    override suspend fun updateProducto(producto: Producto) {
        val request = UpdateProductoRequest(
            id = producto.id,
            nombre = producto.nombre,
            descripcion = producto.descripcion,
            precio = producto.precio,
            stock = producto.stock,
            categoria = producto.categoria
        )
        remoteDataSource.updateProducto(request)
    }

    override suspend fun deleteProducto(productoId: String) {
        remoteDataSource.deleteProducto(productoId)
    }

    override suspend fun getEmpleados(): List<Empleado> {
        val response = remoteDataSource.getEmpleados()
        return response?.data?.map { it.toDomain() } ?: emptyList()
    }

    override suspend fun addEmpleado(empleado: Empleado) {
        val request = CreateEmpleadoRequest(
            nombre = empleado.nombre,
            especialidad = empleado.especialidad,
            telefono = empleado.telefono,
            comision = empleado.comision
        )
        remoteDataSource.addEmpleado(request)
    }

    override suspend fun updateEmpleado(empleado: Empleado) {
        remoteDataSource.updateEmpleado(empleado.toDto())
    }

    override suspend fun deleteEmpleado(empleadoId: String) {
        remoteDataSource.deleteEmpleado(empleadoId)
    }

    override suspend fun descontarInsumos(insumosUsados: List<com.romisspa.app.domain.model.Insumo>) {
        // Not implemented for Retrofit — Firebase handles this directly
    }

    override suspend fun getRutinasDiarias(): List<RutinaDiaria> = emptyList()
    override suspend fun addOrUpdateRutinaDiaria(rutina: RutinaDiaria) {}
    override suspend fun deleteRutinaDiaria(rutinaId: String) {}

    override suspend fun getTareasEspeciales(): List<TareaEspecial> = emptyList()
    override suspend fun addOrUpdateTareaEspecial(tarea: TareaEspecial) {}
    override suspend fun deleteTareaEspecial(tareaId: String) {}
}
