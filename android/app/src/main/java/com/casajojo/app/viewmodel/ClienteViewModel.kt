package com.casajojo.app.viewmodel

import androidx.lifecycle.ViewModel
import com.casajojo.app.model.ClienteErrores
import com.casajojo.app.model.ClienteUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ClienteViewModel : ViewModel() {

    //-- Estado interno mutable
    private val _estado = MutableStateFlow(ClienteUiState())

    //-- Estado público de solo lectura para la interfaz visual
    val estado: StateFlow<ClienteUiState> = _estado.asStateFlow()

    //-- Actualiza el nombre digitado y limpia su mensaje de error activo
    fun onNombreChange(valor: String) {
        _estado.update {
            it.copy(
                nombre = valor,
                errores = it.errores.copy(nombre = null)
            )
        }
    }

    //-- Actualiza el teléfono y limpia su error activo
    fun onTelefonoChange(valor: String) {
        _estado.update {
            it.copy(
                telefono = valor,
                errores = it.errores.copy(telefono = null)
            )
        }
    }

    //-- Alterna la sección de perfil Empresa
    fun onEsPerfilEmpresaChange(valor: Boolean) {
        _estado.update {
            it.copy(
                esPerfilEmpresa = valor,
                errores = if (!valor) it.errores.copy(nombreEmpresa = null) else it.errores
            )
        }
    }

    //-- Actualiza el texto de la razón social / nombre empresa
    fun onNombreEmpresaChange(valor: String) {
        _estado.update {
            it.copy(
                nombreEmpresa = valor,
                errores = it.errores.copy(nombreEmpresa = null)
            )
        }
    }

    //-- Validación de negocio RF19 --
    fun validarFormulario(): Boolean {
        val estadoActual = _estado.value
        val nombreLimpio = estadoActual.nombre.trim()

        //-- Validación de nombre: obligatorio y mínimo 3 caracteres
        val errorNombre = when {
            nombreLimpio.isBlank() -> "El nombre es obligatorio"
            nombreLimpio.length < 3 -> "El nombre debe tener al menos 3 caracteres"
            else -> null
        }

        //-- Validación de teléfono: obligatorio y exactamente los 8 dígitos requeridos por +569
        val errorTelefono = when {
            estadoActual.telefono.isBlank() -> "El teléfono es obligatorio"
            estadoActual.telefono.length < 8 -> "El mínimo de dígitos es 8 para el contacto"
            else -> null
        }

        //-- Validación empresa en blanco
        val errorEmpresa = if (estadoActual.esPerfilEmpresa && estadoActual.nombreEmpresa.isBlank()) {
            "Ingrese nombre de la empresa"
        } else null

        //-- Consolidación de errores
        val erroresCalculados = ClienteErrores(
            nombre = errorNombre,
            telefono = errorTelefono,
            nombreEmpresa = errorEmpresa
        )

        val hayErrores = listOfNotNull(
            erroresCalculados.nombre,
            erroresCalculados.telefono,
            erroresCalculados.nombreEmpresa
        ).isNotEmpty()

        //-- Actualización reactiva del estado de errores
        _estado.update { it.copy(errores = erroresCalculados) }

        return !hayErrores
    }
}