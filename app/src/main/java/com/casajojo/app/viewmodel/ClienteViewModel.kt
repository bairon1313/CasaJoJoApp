package com.casajojo.app.viewmodel

import androidx.lifecycle.ViewModel
import com.casajojo.app.model.ClienteErrores
import com.casajojo.app.model.ClienteUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class ClienteViewModel : ViewModel() {
//-- Estado interno mutable (mutable por el tipo de objeto dentro de la variable inmutable)
    private  val _estado = MutableStateFlow(ClienteUiState())

//-- Estado público de solo lectura para la interfaz visual (Al ser de tipo StateFlow, la pantalla solo puede observar y leer)
    val estado: StateFlow<ClienteUiState> = _estado.asStateFlow()

//-- Actualiza el nombre ya digitado y limpia su mensaje de error activo
    fun onNombreChange(valor : String){
        _estado.update {it.copy(nombre = valor, errores = it.errores.copy(nombre = null))} }

//-- Actualiza el telefono y limpia su error activo
    fun onTelefonoChange(valor: String){
        _estado.update {it.copy(telefono = valor, errores = it.errores.copy(telefono = null))} }

//-- Actualiza el texto de la razon social (para el Checkbox)
    fun onEsPerfilEmpresaChange(valor: Boolean) {
        _estado.update { it.copy(
            esPerfilEmpresa = valor,
            errores = if (!valor) it.errores.copy(nombreEmpresa = null) else it.errores
            )
        }
    }

//-- Alterna la seccion de perfil Empresa
    fun onNombreEmpresaChange(valor: String) {
        _estado.update {
            it.copy(
                nombreEmpresa = valor,
                errores =  it.errores.copy(nombreEmpresa = null)
            )
        }
    }

//-- Validacion de negocio RF19 --
    fun validarFormulario(): Boolean{

        val estadoActual = _estado.value
    //-- Validacion nombre en blanco
        val errorNombre =
            if (estadoActual.nombre.isBlank())
                "El nombre es obligatorio"
                else null

    //-- Validacion telefono en blanco y minimo de digitos
        val errorTelefono =
            if (estadoActual.telefono.isBlank()){
                "El telefono es obligatorio"}
            else if (estadoActual.telefono.length < 8){
                "El minimo de digitos es 8 para el contacto"}
            else null

    //-- Validacion empresa en blanco
        val errorEmpresa =
            if (estadoActual.esPerfilEmpresa && estadoActual.nombreEmpresa.isBlank()){
                "Ingrese nombre de la empresa"}
            else null

    //--
    val erroresCalculados = ClienteErrores(
        nombre = errorNombre,
        telefono = errorTelefono,
        nombreEmpresa = errorEmpresa)
    //--
    val hayErrores = listOfNotNull(
        erroresCalculados.nombre,
        erroresCalculados.telefono,
        erroresCalculados.nombreEmpresa).isNotEmpty()

    //--
    _estado.update {it.copy(errores = erroresCalculados)}

    return !hayErrores
    }
}