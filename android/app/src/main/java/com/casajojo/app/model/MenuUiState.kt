package com.casajojo.app.model

data class MenuUiState(
    val tipoCombinacion: OpcionMenu = OpcionMenu.ESTANDAR,
    val proteina: Plato? = null,            // <-- Ranura independiente para Proteínas
    val platoFondo1: Plato? = null,         // Ranura para Platos de Fondo / Guisos
    val platoFondo2: Plato? = null,
    val acompanamiento1: Plato? = null,
    val acompanamiento2: Plato? = null,
    val ensalada: Plato? = null,
    val porcionDobleEnsalada: Boolean = false,
    val postre: Plato? = null,
    val bebida: Plato? = null,
    val horaRetiro: String = "",
    val esConvenioEmpresa: Boolean = false,
    val observacionesAlimentarias: String = "",
    val menuDelDia: List<Plato> = emptyList(),
    val precioTotal: Int = 0
) {
    val proteinas: List<Plato>
        get() = menuDelDia.filter { it.categoria.equals("PROTEINA", ignoreCase = true) }

    val fondos: List<Plato>
        get() = menuDelDia.filter {
            it.categoria.equals("FONDO", ignoreCase = true) ||
                    it.categoria.equals("GUISO", ignoreCase = true)
        }

    val acompanamientos: List<Plato>
        get() = menuDelDia.filter {
            it.categoria.equals("ACOMPAÑAMIENTO", ignoreCase = true) ||
                    it.categoria.equals("ACOMPANAMIENTO", ignoreCase = true)
        }

    val ensaladas: List<Plato>
        get() = menuDelDia.filter { it.categoria.equals("ENSALADA", ignoreCase = true) }

    val postres: List<Plato>
        get() = menuDelDia.filter { it.categoria.equals("POSTRE", ignoreCase = true) }

    val bebidas: List<Plato>
        get() = menuDelDia.filter { it.categoria.equals("BEBIDA", ignoreCase = true) }
}