package com.romisspa.app.core.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.romisspa.app.di.AppContainer
import com.romisspa.app.presentation.screens.login.LoginScreen
import com.romisspa.app.presentation.screens.dashboard.DashboardScreen
import com.romisspa.app.presentation.screens.reserva.ReservaScreen
import com.romisspa.app.presentation.screens.servicios.ServiciosScreen
import com.romisspa.app.presentation.screens.citas.CitasScreen
import com.romisspa.app.presentation.screens.clientes.ClientesScreen
import com.romisspa.app.presentation.screens.ventas.VentasScreen
import com.romisspa.app.presentation.screens.inventario.InventarioScreen
import com.romisspa.app.presentation.screens.empleados.EmpleadosScreen
import com.romisspa.app.presentation.screens.limpieza.LimpiezaScreen
import com.romisspa.app.presentation.screens.reportes.ReportesScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    container: AppContainer,
    padding: PaddingValues,
    isDarkTheme: Boolean = false,
    onThemeToggle: (Boolean) -> Unit = {}
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.LOGIN,
        modifier = Modifier.padding(padding)
    ) {
        composable(NavRoutes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(NavRoutes.DASHBOARD) {
                        popUpTo(NavRoutes.LOGIN) { inclusive = true }
                    }
                }
            )
        }
        composable(NavRoutes.DASHBOARD) {
            DashboardScreen(
                viewModel = container.dashboardViewModel,
                onReservaClick = { navController.navigate(NavRoutes.RESERVA) },
                onCitasClick = { navController.navigate(NavRoutes.CITAS) },
                onServiciosClick = { navController.navigate(NavRoutes.SERVICIOS) },
                onClientesClick = { navController.navigate(NavRoutes.CLIENTES) },
                onVentasClick = { navController.navigate(NavRoutes.VENTAS) },
                onInventarioClick = { navController.navigate(NavRoutes.INVENTARIO) },
                onEmpleadosClick = { navController.navigate(NavRoutes.EMPLEADOS) },
                onLimpiezaClick = { navController.navigate(NavRoutes.LIMPIEZA) },
                onReportesClick = { navController.navigate(NavRoutes.REPORTES) },
                onCajaClick = { navController.navigate(NavRoutes.CAJA) },
                isDarkTheme = isDarkTheme,
                onThemeToggle = onThemeToggle,
                onLogout = {
                    navController.navigate(NavRoutes.LOGIN) {
                        popUpTo(NavRoutes.DASHBOARD) { inclusive = true }
                    }
                }
            )
        }
        composable(NavRoutes.RESERVA) {
            ReservaScreen(
                viewModel = container.reservaViewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable(NavRoutes.SERVICIOS) {
            ServiciosScreen(
                viewModel = container.serviciosViewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable(NavRoutes.CLIENTES) {
            ClientesScreen(
                viewModel = container.clientesViewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable(NavRoutes.CITAS) {
            CitasScreen(
                viewModel = container.citasViewModel,
                onAddCita = { navController.navigate(NavRoutes.RESERVA) },
                onBack = { navController.popBackStack() }
            )
        }
        composable(NavRoutes.VENTAS) {
            VentasScreen(
                viewModel = container.ventasViewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable(NavRoutes.INVENTARIO) {
            InventarioScreen(
                viewModel = container.inventarioViewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable(NavRoutes.EMPLEADOS) {
            EmpleadosScreen(
                viewModel = container.empleadosViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(NavRoutes.LIMPIEZA) {
            LimpiezaScreen(
                viewModel = container.limpiezaViewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable(NavRoutes.REPORTES) {
            ReportesScreen(
                viewModel = container.reportesViewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable(NavRoutes.CAJA) {
            com.romisspa.app.presentation.screens.caja.CajaScreen(
                viewModel = container.cajaViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
