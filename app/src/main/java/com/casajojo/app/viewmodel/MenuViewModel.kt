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
                platoFondo2 = if (nuevaOpcion == OpcionMenu.DOBLE_PROTEINA) actual.platoFondo2 else null,
                acompanamiento2 = if (nuevaOpcion == OpcionMenu.DOBLE_ACOMPANAMIENTO) actual.acompanamiento2 else null
            )
            limpio.copy(precioTotal = calcularTotal(limpio))
        }
    }


    //--Seleccionar fondo 1
    fun onPlatoFondo1Select(plato: Plato) {
        _estado.update {
            it.copy(platoFondo1 = plato)
                .let { nuevo -> nuevo.copy(precioTotal = calcularTotal(nuevo)) }
        }
    }

    //--Seleccionar fondo 2
    fun onPlatoFondo2Select(plato: Plato) {
        _estado.update {
            it.copy(platoFondo2 = plato)
                .let { nuevo -> nuevo.copy(precioTotal = calcularTotal(nuevo)) }
        }
    }

    //--Seleccionar acompanamiento 1
    fun onAcompanamiento1Select(plato: Plato) {
        _estado.update {
            it.copy(acompanamiento1 = plato)
                .let { nuevo -> nuevo.copy(precioTotal = calcularTotal(nuevo)) }
        }
    }

    //--Seleccionar acompanamiento 2
    fun onAcompanamiento2Select(plato: Plato) {
        _estado.update {
            it.copy(acompanamiento2 = plato)
                .let { nuevo -> nuevo.copy(precioTotal = calcularTotal(nuevo)) }
        }
    }

    //--Seleccionar ensalada
    fun onEnsaladaSelect(plato: Plato) {
        _estado.update {
            it.copy(ensalada = plato)
                .let { nuevo -> nuevo.copy(precioTotal = calcularTotal(nuevo)) }
        }
    }


    //--Activar o desactivar doble porcion de ensalda
    fun onPorcionDobleEnsaladaChange(doble: Boolean) {
        _estado.update {
            it.copy(porcionDobleEnsalada = doble)
                .let { nuevo -> nuevo.copy(precioTotal = calcularTotal(nuevo)) }
        }
    }

        //--Registrar especificaciones o restricciones alimentarias (RF03)
        fun onObservacionesChange(texto: String) {
            _estado.update { actual -> actual.copy(observacionesAlimentarias = texto)
            }
        }

        //-- Logica para calcular el costo total acumulado
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
            return total
        }
    }
