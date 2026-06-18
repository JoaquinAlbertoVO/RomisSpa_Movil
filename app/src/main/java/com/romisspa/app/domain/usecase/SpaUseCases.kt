package com.romisspa.app.domain.usecase

data class SpaUseCases(
    val getServicios: GetServiciosUseCase,
    val addServicio: AddServicioUseCase,
    val updateServicio: UpdateServicioUseCase,
    val deleteServicio: DeleteServicioUseCase,
    val getCitas: GetCitasUseCase,
    val addCita: AddCitaUseCase,
    val deleteCita: DeleteCitaUseCase,
    val getClientes: GetClientesUseCase,
    val addOrUpdateCliente: AddOrUpdateClienteUseCase
)
