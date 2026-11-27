package com.casajojo.app.ui.navigation

//-- Definición segura de las rutas de la aplicación mediante una sealed class

sealed class Ruta(val camino: String){
    object Login : Ruta("login")
    object HomeCliente : Ruta("home_cliente/{nombre}/{telefono}") {
//-- Función auxiliar para construir la ruta con parámetros reales
        fun crearRuta(nombre: String, telefono: String): String = "home_cliente/$nombre/$telefono" }

    object Carrito : Ruta("carrito")
    object Caja : Ruta("caja")
    object Admin : Ruta("admin")
}