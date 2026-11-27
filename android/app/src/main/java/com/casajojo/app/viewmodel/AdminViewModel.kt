package com.casajojo.app.viewmodel

import androidx.lifecycle.ViewModel
import com.casajojo.app.model.Plato
import com.casajojo.app.data.repository.MenuRepository
import kotlinx.coroutines.flow.StateFlow

class AdminViewModel : ViewModel() {

    val listaPlatos: StateFlow<List<Plato>> = MenuRepository.platos

    fun agregarPlato(nombre: String, descripcion: String, precio: Int, categoria: String) {
        val nuevoId = (MenuRepository.platos.value.maxOfOrNull { it.id } ?: 0) + 1
        val nuevoPlato = Plato(
            id = nuevoId,
            nombre = nombre,
            descripcion = descripcion,
            precio = precio,
            categoria = categoria,
            disponible = true
        )
        MenuRepository.agregarPlato(nuevoPlato)
    }

    fun editarPlato(plato: Plato) {
        MenuRepository.editarPlato(plato)
    }

    fun eliminarPlato(idPlato: Int) {
        MenuRepository.eliminarPlato(idPlato)
    }

    fun toggleDisponibilidad(idPlato: Int) {
        MenuRepository.toggleDisponibilidad(idPlato)
    }
}