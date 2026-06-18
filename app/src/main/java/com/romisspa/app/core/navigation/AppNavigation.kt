package com.romisspa.app.core.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.romisspa.app.di.AppContainer
import com.romisspa.app.presentation.screens.LoginScreen
import com.romisspa.app.presentation.screens.DashboardScreen
import com.romisspa.app.presentation.screens.reserva.ReservaScreen
import com.romisspa.app.presentation.screens.servicios.ServiciosScreen
import com.romisspa.app.presentation.screens.citas.CitasScreen
import com.romisspa.app.presentation.screens.clientes.ClientesScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    container: AppContainer,
    padding: PaddingValues
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
                onReservaClick = { navController.navigate(NavRoutes.RESERVA) },
                onCitasClick = { navController.navigate(NavRoutes.CITAS) },
                onServiciosClick = { navController.navigate(NavRoutes.SERVICIOS) },
                onClientesClick = { navController.navigate(NavRoutes.CLIENTES) },
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
    }
}
