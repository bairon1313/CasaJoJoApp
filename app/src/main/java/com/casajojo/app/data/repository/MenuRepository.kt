package com.casajojo.app.data.repository

import com.casajojo.app.model.Plato
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object MenuRepository {

    // Platos de prueba por defecto cargados con sus respectivas categorías
    private val _platos = MutableStateFlow<List<Plato>>(
        listOf(
            Plato(id = 1, nombre = "Cazuela de Vacuno", descripcion = "Tradicional cazuela con verduras de estación", precio = 4500, categoria = "FONDO", disponible = true),
            Plato(id = 2, nombre = "Pastel de Choclo", descripcion = "Acompañado de ensalada a elección", precio = 5000, categoria = "FONDO", disponible = true),
            Plato(id = 3, nombre = "Papas Fritas Porción", descripcion = "Porción individual crujiente", precio = 2000, categoria = "ACOMPAÑAMIENTO", disponible = true),
            Plato(id = 4, nombre = "Ensalada Chilena", descripcion = "Tomate con cebolla amortiguada", precio = 1500, categoria = "ENSALADA", disponible = true),
            Plato(id = 5, nombre = "Mote con Huesillo", descripcion = "Vaso de 500cc bien helado", precio = 1800, categoria = "POSTRE", disponible = true),
            Plato(id = 6, nombre = "Bebida 350ml", descripcion = "Lata variedad a elección", precio = 1200, categoria = "BEBIDA", disponible = false)
        )
    )
    val platos: StateFlow<List<Plato>> = _platos.asStateFlow()

    fun agregarPlato(plato: Plato) {
        _platos.value = _platos.value + plato
    }

    fun editarPlato(platoEditado: Plato) {
        _platos.value = _platos.value.map { if (it.id == platoEditado.id) platoEditado else it }
    }

    fun eliminarPlato(idPlato: Int) {
        _platos.value = _platos.value.filterNot { it.id == idPlato }
    }

    fun toggleDisponibilidad(idPlato: Int) {
        _platos.value = _platos.value.map { plato ->
            if (plato.id == idPlato) plato.copy(disponible = !plato.disponible) else plato
        }
    }
}