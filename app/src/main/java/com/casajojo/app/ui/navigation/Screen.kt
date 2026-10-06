package com.casajojo.app.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home_screen")
    object Carrito : Screen("carrito_screen")
    object Caja : Screen("caja_screen")
}