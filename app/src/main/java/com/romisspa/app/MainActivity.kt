package com.romisspa.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.rememberNavController
import com.romisspa.app.core.navigation.AppNavigation
import com.romisspa.app.presentation.components.AppScaffold
import com.romisspa.app.ui.theme.RomisSpaTheme

class MainActivity : ComponentActivity() {
    private val container by lazy {
        (application as RomisSpaApplication).container
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RomisSpaTheme {
                val navController = rememberNavController()
                
                AppScaffold(navController = navController) { padding ->
                    AppNavigation(
                        navController = navController,
                        container = container,
                        padding = padding
                    )
                }
            }
        }
    }
}
