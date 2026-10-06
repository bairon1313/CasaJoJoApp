package com.casajojo.app.model

object PlatoMock {
    val listaPlatos = listOf(
        Plato(
            id = 1,
            nombre = "Empanada de Pino",
            descripcion = "Tradicional empanada chilena al horno con carne picada, cebolla, huevo y aceituna.",
            precio = 2500,
            categoria = "FONDO",
            disponible = true
        ),
        Plato(
            id = 2,
            nombre = "Pastel de Choclo",
            descripcion = "Pastel artesanal con pino de res, pollo, huevo duro y pino dulce de maíz.",
            precio = 6500,
            categoria = "FONDO",
            disponible = true
        ),
        Plato(
            id = 3,
            nombre = "Cazuela de Ave",
            descripcion = "Sopa casera con presa de pollo, papa, zapallo, choclo y verduritas.",
            precio = 5500,
            categoria = "FONDO",
            disponible = true
        ),
        Plato(
            id = 4,
            nombre = "Mote con Huesillo",
            descripcion = "Bebida refrescante chilena con mote de trigo hervido y huesillos deshidratados.",
            precio = 2000,
            categoria = "POSTRE",
            disponible = true
        )
    )
}