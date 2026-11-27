package com.casajojo.app.data.repository

import com.casajojo.app.model.Plato
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object MenuRepository {

    private val _platos = MutableStateFlow(
        listOf(
            // --- PROTEÍNAS (Carnes / Acompañables) ---
            Plato(
                id = 1,
                nombre = "Carne Mechada",
                descripcion = "Vacuno suave cocinado a fuego lento",
                precio = 4000,
                categoria = "PROTEINA",
                disponible = true
            ),
            Plato(
                id = 2,
                nombre = "Pechuga a la Plancha",
                descripcion = "Filete de pollo sazonado a las finas hierbas",
                precio = 3500,
                categoria = "PROTEINA",
                disponible = true
            ),
            Plato(
                id = 3,
                nombre = "Chuleta de Cerdo",
                descripcion = "Chuleta dorada a la plancha",
                precio = 3800,
                categoria = "PROTEINA",
                disponible = true
            ),

            // --- PLATOS DE FONDO / GUISOS PREPARADOS ---
            Plato(
                id = 4,
                nombre = "Pastel de Choclo",
                descripcion = "Tradicional con pino de carne, pollo, huevo y aceituna",
                precio = 4500,
                categoria = "FONDO",
                disponible = true
            ),
            Plato(
                id = 5,
                nombre = "Lasaña Bolognesa",
                descripcion = "Capas de pasta casera con pino y salsa bechamel",
                precio = 4200,
                categoria = "FONDO",
                disponible = true
            ),

            // --- ACOMPAÑAMIENTOS ---
            Plato(
                id = 6,
                nombre = "Arroz Primavera",
                descripcion = "Arroz graneado con pimentón y arvejas",
                precio = 1500,
                categoria = "ACOMPAÑAMIENTO",
                disponible = true
            ),
            Plato(
                id = 7,
                nombre = "Puré de Papas Rústico",
                descripcion = "Puré casero elaborado con mantequilla y leche",
                precio = 1500,
                categoria = "ACOMPAÑAMIENTO",
                disponible = true
            ),
            Plato(
                id = 8,
                nombre = "Papas Fritas",
                descripcion = "Porción de papas doradas y crujientes",
                precio = 1800,
                categoria = "ACOMPAÑAMIENTO",
                disponible = true
            ),

            // --- ENSALADAS ---
            Plato(
                id = 9,
                nombre = "Ensalada Chilena",
                descripcion = "Tomates frescos con cebolla amortiguada y cilantro",
                precio = 1200,
                categoria = "ENSALADA",
                disponible = true
            ),

            // --- POSTRES ---
            Plato(
                id = 10,
                nombre = "Leche Asada",
                descripcion = "Postre tradicional al horno con caramelo",
                precio = 1500,
                categoria = "POSTRE",
                disponible = true
            ),

            // --- BEBIDAS ---
            Plato(
                id = 11,
                nombre = "Jugo Natural",
                descripcion = "Jugo de fruta de la estación",
                precio = 1500,
                categoria = "BEBIDA",
                disponible = true
            )
        )
    )

    val platos: StateFlow<List<Plato>> = _platos.asStateFlow()

    fun agregarPlato(plato: Plato) {
        _platos.value = _platos.value + plato
    }

    fun editarPlato(plato: Plato) {
        _platos.value = _platos.value.map { if (it.id == plato.id) plato else it }
    }

    fun eliminarPlato(id: Int) {
        _platos.value = _platos.value.filter { it.id != id }
    }

    fun toggleDisponibilidad(id: Int) {
        _platos.value = _platos.value.map {
            if (it.id == id) it.copy(disponible = !it.disponible) else it
        }
    }
}