package com.casajojo.app.model

data class Plato (
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val categoria: String,    // FONDO, ACOMPAÑAMIENTO, ENSALADA, POSTRE, BEBIDA
    val disponible: Boolean=true
)