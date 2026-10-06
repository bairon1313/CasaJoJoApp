package com.casajojo.app.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            Text(text = "Pantalla Home Cliente")
        }
        composable(Screen.Carrito.route) {
            Text(text = "Pantalla Carrito")
        }
        composable(Screen.Caja.route) {
            Text(text = "Pantalla Caja / Administración")
        }
    }
}