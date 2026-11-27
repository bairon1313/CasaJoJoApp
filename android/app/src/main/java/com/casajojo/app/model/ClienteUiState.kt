package com.casajojo.app.model

//--- Almacena los mensajes de error
data class ClienteErrores(
    val nombre: String? = null,
    val telefono: String? = null,
    val nombreEmpresa: String? = null)

//--- Modelo principal que representa el estado completo de la interfaz de usuario
data class ClienteUiState(
    val nombre: String = "",
    val telefono: String = "",
    val esPerfilEmpresa: Boolean = false,
    val nombreEmpresa: String = "",
    val errores: ClienteErrores = ClienteErrores()
)