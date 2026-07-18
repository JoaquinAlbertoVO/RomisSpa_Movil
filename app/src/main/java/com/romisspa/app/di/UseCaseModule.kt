package com.romisspa.app.di

import com.romisspa.app.domain.usecase.*

class UseCaseModule(repositoryModule: RepositoryModule) {
    val spaUseCases by lazy {
        SpaUseCases(
            getServicios = GetServiciosUseCase(repositoryModule.spaRepository),
            addServicio = AddServicioUseCase(repositoryModule.spaRepository),
            updateServicio = UpdateServicioUseCase(repositoryModule.spaRepository),
            deleteServicio = DeleteServicioUseCase(repositoryModule.spaRepository),
            getCitas = GetCitasUseCase(repositoryModule.spaRepository),
            addCita = AddCitaUseCase(repositoryModule.spaRepository),
            deleteCita = DeleteCitaUseCase(repositoryModule.spaRepository),
            updateCitaStatus = UpdateCitaStatusUseCase(repositoryModule.spaRepository),
            getClientes = GetClientesUseCase(repositoryModule.spaRepository),
            addOrUpdateCliente = AddOrUpdateClienteUseCase(repositoryModule.spaRepository),
            getVentas = GetVentasUseCase(repositoryModule.spaRepository),
            addVenta = AddVentaUseCase(repositoryModule.spaRepository),
            getProductos = GetProductosUseCase(repositoryModule.spaRepository),
            addProducto = AddProductoUseCase(repositoryModule.spaRepository),
            updateProducto = UpdateProductoUseCase(repositoryModule.spaRepository),
            deleteProducto = DeleteProductoUseCase(repositoryModule.spaRepository),
            getEmpleados = GetEmpleadosUseCase(repositoryModule.spaRepository),
            addEmpleado = AddEmpleadoUseCase(repositoryModule.spaRepository),
            updateEmpleado = UpdateEmpleadoUseCase(repositoryModule.spaRepository),
            deleteEmpleado = DeleteEmpleadoUseCase(repositoryModule.spaRepository),
            descontarInsumos = DescontarInsumosUseCase(repositoryModule.spaRepository),
            getRutinasDiarias = GetRutinasDiariasUseCase(repositoryModule.spaRepository),
            addOrUpdateRutinaDiaria = AddOrUpdateRutinaDiariaUseCase(repositoryModule.spaRepository),
            deleteRutinaDiaria = DeleteRutinaDiariaUseCase(repositoryModule.spaRepository),
            getTareasEspeciales = GetTareasEspecialesUseCase(repositoryModule.spaRepository),
            addOrUpdateTareaEspecial = AddOrUpdateTareaEspecialUseCase(repositoryModule.spaRepository),
            deleteTareaEspecial = DeleteTareaEspecialUseCase(repositoryModule.spaRepository)
        )
    }
}
