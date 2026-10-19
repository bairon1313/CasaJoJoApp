package com.casajojo.app.data.repository

import com.casajojo.app.model.EstadoPedido
import com.casajojo.app.model.Pedido
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object PedidoRepository {

    private var incrementalId = 1
    private val _pedidos = MutableStateFlow<List<Pedido>>(emptyList())
    val pedidos: StateFlow<List<Pedido>> = _pedidos.asStateFlow()

    fun agregarPedido(pedido: Pedido) {
        val nuevo = pedido.copy(id = incrementalId++)
        _pedidos.value = _pedidos.value + nuevo
    }

    fun actualizarEstado(idPedido: Int, nuevoEstado: EstadoPedido) {
        _pedidos.value = _pedidos.value.map {
            if (it.id == idPedido) it.copy(estado = nuevoEstado) else it
        }
    }
}