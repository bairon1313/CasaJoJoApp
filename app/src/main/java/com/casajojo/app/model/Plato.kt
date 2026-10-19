package com.casajojo.app.model

data class Plato(
    val id: Int = 0,                   // Ajuste 1: Valor por defecto 0
    val nombre: String,
    val descripcion: String = "",       // Ajuste 2: Valor por defecto texto vacío
    val precio: Int,
    val categoria: String,             // FONDO, ACOMPAÑAMIENTO, ENSALADA, POSTRE, BEBIDA
    val disponible: Boolean = true
)