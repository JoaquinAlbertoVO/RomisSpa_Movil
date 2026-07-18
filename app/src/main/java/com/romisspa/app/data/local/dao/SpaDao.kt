package com.romisspa.app.data.local.dao

import androidx.room.*
import com.romisspa.app.data.local.entities.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SpaDao {

    // SERVICIOS
    @Query("SELECT * FROM servicios")
    suspend fun getServicios(): List<ServicioEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServicio(servicio: ServicioEntity)

    @Delete
    suspend fun deleteServicio(servicio: ServicioEntity)

    // CITAS
    @Query("SELECT * FROM citas")
    suspend fun getCitas(): List<CitaEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCita(cita: CitaEntity)

    @Delete
    suspend fun deleteCita(cita: CitaEntity)

    @Query("UPDATE citas SET estado = :nuevoEstado WHERE id = :citaId")
    suspend fun updateCitaStatus(citaId: String, nuevoEstado: String)

    // CLIENTES
    @Query("SELECT * FROM clientes")
    suspend fun getClientes(): List<ClienteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCliente(cliente: ClienteEntity)

    // PRODUCTOS
    @Query("SELECT * FROM productos")
    suspend fun getProductos(): List<ProductoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducto(producto: ProductoEntity)

    @Query("DELETE FROM productos WHERE id = :id")
    suspend fun deleteProducto(id: String)

    // EMPLEADOS
    @Query("SELECT * FROM empleados")
    suspend fun getEmpleados(): List<EmpleadoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmpleado(empleado: EmpleadoEntity)

    @Query("DELETE FROM empleados WHERE id = :id")
    suspend fun deleteEmpleado(id: String)

    // VENTAS
    @Query("SELECT * FROM ventas")
    suspend fun getVentas(): List<VentaEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVenta(venta: VentaEntity)
}
