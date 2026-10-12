package com.casajojo.app.model

data class MenuUiState(
    val tipoCombinacion: OpcionMenu = OpcionMenu.ESTANDAR,
    val platoFondo1: Plato? = null,
    val platoFondo2: Plato? = null, //--Doble proteina
    val acompanamiento1: Plato? = null,
    val acompanamiento2: Plato? = null, //--Doble acompanamiento
    val ensalada: Plato? = null,
    val porcionDobleEnsalada: Boolean = false, //--Doble ensalada (RF02)
    val observacionesAlimentarias: String = "", //--Restricciones de cocina (RF03)
    val menuDelDia: List<Plato> = emptyList(), //--Minuta transmitida
    val precioTotal: Int = 0
){
    val fondos: List<Plato>
        get() = menuDelDia.filter { it.categoria.equals("FONDO", ignoreCase = true) }

    val acompanamientos: List<Plato>
        get() = menuDelDia.filter { it.categoria.equals("ACOMPAÑAMIENTO", ignoreCase = true) }

    val ensaladas: List<Plato>
        get() = menuDelDia.filter { it.categoria.equals("ENSALADA", ignoreCase = true) }
}