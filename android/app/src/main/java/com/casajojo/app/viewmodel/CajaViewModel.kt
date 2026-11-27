package com.casajojo.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.casajojo.app.data.repository.PedidoRepository
import com.casajojo.app.model.EstadoPedido
import com.casajojo.app.model.Pedido
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class CajaViewModel : ViewModel() {

    // Lista de pedidos reordenada automáticamente por hora de retiro en tiempo real
    val pedidos: StateFlow<List<Pedido>> = PedidoRepository.pedidos
        .map { lista ->
            lista.sortedBy { it.horaRetiro }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun marcarComoListo(idPedido: Int) {
        PedidoRepository.actualizarEstado(idPedido, EstadoPedido.LISTO)
    }

    fun cambiarEstado(idPedido: Int, nuevoEstado: EstadoPedido) {
        PedidoRepository.actualizarEstado(idPedido, nuevoEstado)
    }
}