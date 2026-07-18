package com.romisspa.app.presentation.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.romisspa.app.core.navigation.BottomNavItem
import com.romisspa.app.core.navigation.NavRoutes
import com.romisspa.app.ui.theme.Cream
import com.romisspa.app.ui.theme.RoseGold
import com.romisspa.app.ui.theme.GreyWarm

@Composable
fun AppBottomBar(
    navController: NavHostController
) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    // No mostrar BottomBar en el Login
    if (currentRoute == NavRoutes.LOGIN) return

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Citas,
        BottomNavItem.Servicios,
        BottomNavItem.Clientes
    )

    NavigationBar(
        containerColor = androidx.compose.material3.MaterialTheme.colorScheme.background
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            if (item.route == NavRoutes.DASHBOARD) {
                                popUpTo(NavRoutes.DASHBOARD) { inclusive = true }
                            }
                        }
                    }
                },
                icon = { Icon(item.icon, null) },
                label = { Text(item.title) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                    selectedTextColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                    unselectedIconColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = androidx.compose.material3.MaterialTheme.colorScheme.background
                )
            )
        }
    }
}
