package com.romisspa.app.domain.repository

import com.romisspa.app.domain.model.Cita
import com.romisspa.app.domain.model.Cliente
import com.romisspa.app.domain.model.Empleado
import com.romisspa.app.domain.model.Insumo
import com.romisspa.app.domain.model.Producto
import com.romisspa.app.domain.model.Servicio
import com.romisspa.app.domain.model.Venta
import com.romisspa.app.domain.model.RutinaDiaria
import com.romisspa.app.domain.model.TareaEspecial

interface SpaRepository {
    suspend fun getServicios(): List<Servicio>
    suspend fun addServicio(servicio: Servicio)
    suspend fun updateServicio(servicio: Servicio)
    suspend fun deleteServicio(servicio: Servicio)
    
    suspend fun getCitas(): List<Cita>
    suspend fun addCita(cita: Cita)
    suspend fun deleteCita(cita: Cita)
    suspend fun updateCitaStatus(citaId: String, nuevoEstado: String)

    suspend fun getClientes(): List<Cliente>
    suspend fun addOrUpdateCliente(cliente: Cliente)

    suspend fun getVentas(): List<Venta>
    suspend fun addVenta(venta: Venta)

    suspend fun getProductos(): List<Producto>
    suspend fun addProducto(producto: Producto)
    suspend fun updateProducto(producto: Producto)
    suspend fun deleteProducto(productoId: String)

    suspend fun getEmpleados(): List<Empleado>
    suspend fun addEmpleado(empleado: Empleado)
    suspend fun updateEmpleado(empleado: Empleado)
    suspend fun deleteEmpleado(empleadoId: String)

    suspend fun descontarInsumos(insumosUsados: List<Insumo>)

    // Limpieza
    suspend fun getRutinasDiarias(): List<RutinaDiaria>
    suspend fun addOrUpdateRutinaDiaria(rutina: RutinaDiaria)
    suspend fun deleteRutinaDiaria(rutinaId: String)
    
    suspend fun getTareasEspeciales(): List<TareaEspecial>
    suspend fun addOrUpdateTareaEspecial(tarea: TareaEspecial)
    suspend fun deleteTareaEspecial(tareaId: String)
}
