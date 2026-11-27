package com.casajojo.app.model

import android.provider.ContactsContract

data class Pedido(
    val id: Int=0,
    val nombreCliente: String,
    val telefonoCliente: String,
    val platosSeleccionados: List<Plato>,
    val observaciones: String="",
    val horaRetiro: String,
    val estado: EstadoPedido= EstadoPedido.RECIBIDO
)
