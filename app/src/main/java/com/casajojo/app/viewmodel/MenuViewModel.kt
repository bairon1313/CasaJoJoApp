package com.casajojo.app.viewmodel

import androidx.lifecycle.ViewModel
import com.casajojo.app.model.MenuUiState
import com.casajojo.app.model.OpcionMenu
import com.casajojo.app.model.Plato
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MenuViewModel : ViewModel() {

    //-- Estado interno mutable (mutable por el tipo de objeto dentro de la variable inmutable)
    private val _estado = MutableStateFlow(MenuUiState())

    //-- Estado público de solo lectura para la interfaz visual (Al ser de tipo StateFlow, la pantalla solo puede observar y leer)
    val estado: StateFlow<MenuUiState> = _estado.asStateFlow()

    init {
        cargarMenuDelDia()
    }

    // Minuta simulada (aun no hay base de datos)
    private fun cargarMenuDelDia() {
        val listaSimulada = listOf(
            Plato(
                id = 1,
                nombre = "Pollo al Horno",
                descripcion = "Con especias",
                precio = 4500,
                categoria = "FONDO"
            ),
            Plato(
                id = 2,
                nombre = "Carne Mechada",
                descripcion = "Carne mechada",
                precio = 5000,
                categoria = "FONDO"
            ),
            Plato(
                id = 3,
                nombre = "Arroz",
                descripcion = "Arroz blanco",
                precio = 1000,
                categoria = "ACOMPAÑAMIENTO"
            ),
            Plato(
                id = 4,
                nombre = "Puré de Papas",
                descripcion = "Puré casero",
                precio = 1200,
                categoria = "ACOMPAÑAMIENTO"
            ),
            Plato(
                id = 5,
                nombre = "Ensalada Chilena",
                descripcion = "Tomate y cebolla",
                precio = 1500,
                categoria = "ENSALADA"
            )
        )
        _estado.update { actual ->
            val nuevoEstado = actual.copy(menuDelDia = listaSimulada)
            nuevoEstado.copy(precioTotal = calcularTotal(nuevoEstado))
        }
    }

    //-- Cambiar modalidad y limpiar selecciones no aplicables para evitar costos ocultos
    fun onTipoCombinacionChange(nuevaOpcion: OpcionMenu) {
        _estado.update { actual ->
            val limpio = actual.copy(
                tipoCombinacion = nuevaOpcion,
                platoFondo1 = if (nuevaOpcion == OpcionMenu.SOLO_ENSALADA || nuevaOpcion == OpcionMenu.DOBLE_ACOMPANAMIENTO) null else actual.platoFondo1,
                platoFondo2 = if (nuevaOpcion == OpcionMenu.DOBLE_PROTEINA) actual.platoFondo2 else null,
                acompanamiento1 = if (nuevaOpcion == OpcionMenu.SOLO_ENSALADA || nuevaOpcion == OpcionMenu.DOBLE_PROTEINA) null else actual.acompanamiento1,
                acompanamiento2 = if (nuevaOpcion == OpcionMenu.DOBLE_ACOMPANAMIENTO) actual.acompanamiento2 else null
            )
            limpio.copy(precioTotal = calcularTotal(limpio))
        }
    }


    //--Seleccionar fondo 1
    fun onPlatoFondo1Select(plato: Plato) {
        _estado.update {
            val nuevoPlato= if(it.platoFondo1?.id==plato.id)null else plato
            it.copy(platoFondo1 = nuevoPlato).let { n -> n.copy(precioTotal = calcularTotal(n)) }
        }
    }

    fun onPlatoFondo2Select(plato: Plato) {
        _estado.update {
            val nuevoPlato = if (it.platoFondo2?.id == plato.id) null else plato
            it.copy(platoFondo2 = nuevoPlato).let { n -> n.copy(precioTotal = calcularTotal(n)) }
        }
    }

    fun onAcompanamiento1Select(plato: Plato) {
        _estado.update {
            val nuevoPlato = if (it.acompanamiento1?.id == plato.id) null else plato
            it.copy(acompanamiento1 = nuevoPlato).let { n -> n.copy(precioTotal = calcularTotal(n)) }
        }
    }

    fun onAcompanamiento2Select(plato: Plato) {
        _estado.update {
            val nuevoPlato = if (it.acompanamiento2?.id == plato.id) null else plato
            it.copy(acompanamiento2 = nuevoPlato).let { n -> n.copy(precioTotal = calcularTotal(n)) }
        }
    }

    fun onEnsaladaSelect(plato: Plato) {
        _estado.update {
            val nuevoPlato = if (it.ensalada?.id == plato.id) null else plato
            it.copy(ensalada = nuevoPlato).let { n -> n.copy(precioTotal = calcularTotal(n)) }
        }
    }

    //-- Selección de Postres y Bebidas (RF01)
    fun onPostreSelect(plato: Plato) {
        _estado.update {
            val nuevoPostre = if (it.postre?.id == plato.id) null else plato
            it.copy(postre = nuevoPostre).let { n -> n.copy(precioTotal = calcularTotal(n)) }
        }
    }

    fun onBebidaSelect(plato: Plato) {
        _estado.update {
            val nuevaBebida = if (it.bebida?.id == plato.id) null else plato
            it.copy(bebida = nuevaBebida).let { n -> n.copy(precioTotal = calcularTotal(n)) }
        }
    }

    //-- Activar o desactivar doble porción de ensalada (RF02)
    fun onPorcionDobleEnsaladaChange(doble: Boolean) {
        _estado.update {
            it.copy(porcionDobleEnsalada = doble).let { n -> n.copy(precioTotal = calcularTotal(n)) }
        }
    }

    //-- Registrar especificaciones alimentarias (RF03)
    fun onObservacionesChange(texto: String) {
        _estado.update { actual -> actual.copy(observacionesAlimentarias = texto) }
    }

    //-- Programar hora de retiro (RF04)
    fun onHoraRetiroChange(hora: String) {
        _estado.update { actual -> actual.copy(horaRetiro = hora) }
    }

    //-- Modalidad Convenio Empresa (RF26)
    fun onConvenioEmpresaChange(activo: Boolean) {
        _estado.update { actual -> actual.copy(esConvenioEmpresa = activo) }
    }


        //-- Cálculo del costo total acumulado
        private fun calcularTotal(state: MenuUiState): Int {
            var total = 0

            when (state.tipoCombinacion) {
                OpcionMenu.ESTANDAR -> {
                    total += state.platoFondo1?.precio ?: 0
                    total += state.acompanamiento1?.precio ?: 0
                }

                OpcionMenu.DOBLE_PROTEINA -> {
                    total += state.platoFondo1?.precio ?: 0
                    total += state.platoFondo2?.precio ?: 0
                }

                OpcionMenu.DOBLE_ACOMPANAMIENTO -> {
                    total += state.acompanamiento1?.precio ?: 0
                    total += state.acompanamiento2?.precio ?: 0
                }

                OpcionMenu.SOLO_ENSALADA -> {}
            }
            //-- Sumar ensalada duplicando el precio si la opcion porcion doble esta activa
            state.ensalada?.let { ensalada ->
                total += if (state.porcionDobleEnsalada) ensalada.precio * 2
                else ensalada.precio
            }

            total+=state.postre?.precio ?:0
            total+=state.bebida?.precio ?:0

            return total
        }
    }
