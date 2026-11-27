package com.casajojo.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.casajojo.app.data.repository.MenuRepository
import com.casajojo.app.model.MenuUiState
import com.casajojo.app.model.OpcionMenu
import com.casajojo.app.model.Plato
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ClienteMenuViewModel : ViewModel() {

    private val _estado = MutableStateFlow(MenuUiState())
    val estado: StateFlow<MenuUiState> = _estado.asStateFlow()

    val platosDisponibles: StateFlow<List<Plato>> = MenuRepository.platos
        .map { lista -> lista.filter { it.disponible } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MenuRepository.platos.value.filter { it.disponible }
        )

    init {
        viewModelScope.launch {
            MenuRepository.platos.collect { lista ->
                val disponibles = lista.filter { it.disponible }
                _estado.value = _estado.value.copy(
                    menuDelDia = disponibles
                )
                recalcularTotal()
            }
        }
    }

    fun onProteinaSelect(plato: Plato) {
        val actual = _estado.value.proteina
        _estado.value = _estado.value.copy(
            proteina = if (actual?.id == plato.id) null else plato
        )
        recalcularTotal()
    }

    fun removerProteina() {
        _estado.value = _estado.value.copy(proteina = null)
        recalcularTotal()
    }

    fun onTipoCombinacionChange(opcion: OpcionMenu) {
        _estado.value = _estado.value.copy(tipoCombinacion = opcion)
        recalcularTotal()
    }

    fun onPlatoFondo1Select(plato: Plato) {
        val actual = _estado.value.platoFondo1
        _estado.value = _estado.value.copy(platoFondo1 = if (actual?.id == plato.id) null else plato)
        recalcularTotal()
    }

    fun onPlatoFondo2Select(plato: Plato) {
        val actual = _estado.value.platoFondo2
        _estado.value = _estado.value.copy(platoFondo2 = if (actual?.id == plato.id) null else plato)
        recalcularTotal()
    }

    fun onAcompanamiento1Select(plato: Plato) {
        val actual = _estado.value.acompanamiento1
        _estado.value = _estado.value.copy(acompanamiento1 = if (actual?.id == plato.id) null else plato)
        recalcularTotal()
    }

    fun onAcompanamiento2Select(plato: Plato) {
        val actual = _estado.value.acompanamiento2
        _estado.value = _estado.value.copy(acompanamiento2 = if (actual?.id == plato.id) null else plato)
        recalcularTotal()
    }

    fun onEnsaladaSelect(plato: Plato) {
        val actual = _estado.value.ensalada
        _estado.value = _estado.value.copy(ensalada = if (actual?.id == plato.id) null else plato)
        recalcularTotal()
    }

    fun onPorcionDobleEnsaladaChange(esDoble: Boolean) {
        _estado.value = _estado.value.copy(porcionDobleEnsalada = esDoble)
        recalcularTotal()
    }

    fun onPostreSelect(plato: Plato) {
        val actual = _estado.value.postre
        _estado.value = _estado.value.copy(postre = if (actual?.id == plato.id) null else plato)
        recalcularTotal()
    }

    fun onBebidaSelect(plato: Plato) {
        val actual = _estado.value.bebida
        _estado.value = _estado.value.copy(bebida = if (actual?.id == plato.id) null else plato)
        recalcularTotal()
    }

    fun onObservacionesChange(texto: String) {
        _estado.value = _estado.value.copy(observacionesAlimentarias = texto)
    }

    // Funciones para eliminar ítems específicos desde el Carrito
    fun removerPlatoFondo1() {
        _estado.value = _estado.value.copy(platoFondo1 = null)
        recalcularTotal()
    }

    fun removerPlatoFondo2() {
        _estado.value = _estado.value.copy(platoFondo2 = null)
        recalcularTotal()
    }

    fun removerAcompanamiento1() {
        _estado.value = _estado.value.copy(acompanamiento1 = null)
        recalcularTotal()
    }

    fun removerAcompanamiento2() {
        _estado.value = _estado.value.copy(acompanamiento2 = null)
        recalcularTotal()
    }

    fun removerEnsalada() {
        _estado.value = _estado.value.copy(ensalada = null)
        recalcularTotal()
    }

    fun removerPostre() {
        _estado.value = _estado.value.copy(postre = null)
        recalcularTotal()
    }

    fun removerBebida() {
        _estado.value = _estado.value.copy(bebida = null)
        recalcularTotal()
    }

    fun vaciarCarrito() {
        _estado.value = _estado.value.copy(
            proteina = null,
            platoFondo1 = null,
            platoFondo2 = null,
            acompanamiento1 = null,
            acompanamiento2 = null,
            ensalada = null,
            porcionDobleEnsalada = false,
            postre = null,
            bebida = null,
            observacionesAlimentarias = "",
            precioTotal = 0
        )
    }

    private fun recalcularTotal() {
        val e = _estado.value
        var suma = 0

        // Suma proteína si está seleccionada
        e.proteina?.let { suma += it.precio }

        if (e.tipoCombinacion != OpcionMenu.SOLO_ENSALADA) {
            e.platoFondo1?.let { suma += it.precio }
            if (e.tipoCombinacion == OpcionMenu.DOBLE_PROTEINA) {
                e.platoFondo2?.let { suma += it.precio }
            }
        }

        if (e.tipoCombinacion != OpcionMenu.SOLO_ENSALADA && e.tipoCombinacion != OpcionMenu.DOBLE_PROTEINA) {
            e.acompanamiento1?.let { suma += it.precio }
            if (e.tipoCombinacion == OpcionMenu.DOBLE_ACOMPANAMIENTO) {
                e.acompanamiento2?.let { suma += it.precio }
            }
        }

        e.ensalada?.let { ens ->
            suma += if (e.porcionDobleEnsalada) ens.precio * 2 else ens.precio
        }

        e.postre?.let { suma += it.precio }
        e.bebida?.let { suma += it.precio }

        _estado.value = _estado.value.copy(precioTotal = suma)
    }
}