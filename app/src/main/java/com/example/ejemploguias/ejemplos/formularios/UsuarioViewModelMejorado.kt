package com.example.ejemploguias.ejemplos.formularios

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/** Flags "tocado": el error de un campo aparece recién después de que el usuario escribe algo. */
data class UsuarioTocado(
    val nombre: Boolean = false,
    val correo: Boolean = false,
    val clave: Boolean = false,
    val direccion: Boolean = false
)

/** Misma información que UsuarioUiState, más los flags de la validación en tiempo real. */
data class UsuarioUiStateMejorado(
    val nombre: String = "",
    val correo: String = "",
    val clave: String = "",
    val direccion: String = "",
    val aceptaTerminos: Boolean = false,
    val errores: UsuarioErrores = UsuarioErrores(),
    val tocado: UsuarioTocado = UsuarioTocado(),
    val mostrarClave: Boolean = false
)

/**
 * Mejora de la Guía 11: valida cada campo dentro de su propio onXxxChange
 * en lugar de esperar al botón, usando flags `tocado` para no mostrar
 * errores en campos con los que el usuario aún no interactuó.
 */
class UsuarioViewModelMejorado : ViewModel() {
    private val _estado = MutableStateFlow(UsuarioUiStateMejorado())
    val estado: StateFlow<UsuarioUiStateMejorado> = _estado

    fun onNombreChange(valor: String) {
        _estado.update {
            it.copy(
                nombre = valor,
                tocado = it.tocado.copy(nombre = true),
                errores = it.errores.copy(nombre = validarRequerido(valor))
            )
        }
    }

    fun onCorreoChange(valor: String) {
        _estado.update {
            it.copy(
                correo = valor,
                tocado = it.tocado.copy(correo = true),
                errores = it.errores.copy(correo = validarCorreo(valor))
            )
        }
    }

    fun onClaveChange(valor: String) {
        _estado.update {
            it.copy(
                clave = valor,
                tocado = it.tocado.copy(clave = true),
                errores = it.errores.copy(clave = validarClave(valor))
            )
        }
    }

    fun onDireccionChange(valor: String) {
        _estado.update {
            it.copy(
                direccion = valor,
                tocado = it.tocado.copy(direccion = true),
                errores = it.errores.copy(direccion = validarRequerido(valor))
            )
        }
    }

    fun onAceptaTerminosChange(valor: Boolean) {
        _estado.update { it.copy(aceptaTerminos = valor) }
    }

    fun onMostrarClaveChange(mostrar: Boolean) {
        _estado.update { it.copy(mostrarClave = mostrar) }
    }

    fun validarFormulario(): Boolean {
        val estadoActual = _estado.value
        val errores = UsuarioErrores(
            nombre = validarRequerido(estadoActual.nombre),
            correo = validarCorreo(estadoActual.correo),
            clave = validarClave(estadoActual.clave),
            direccion = validarRequerido(estadoActual.direccion)
        )
        val hayErrores = listOfNotNull(errores.nombre, errores.correo, errores.clave, errores.direccion).isNotEmpty()
        _estado.update {
            it.copy(
                errores = errores,
                tocado = UsuarioTocado(nombre = true, correo = true, clave = true, direccion = true)
            )
        }
        return !hayErrores
    }

    private fun validarRequerido(valor: String): String? =
        if (valor.isBlank()) "Campo obligatorio" else null

    private fun validarCorreo(valor: String): String? = when {
        valor.isBlank() -> "Campo obligatorio"
        !valor.contains("@") -> "Correo inválido"
        else -> null
    }

    private fun validarClave(valor: String): String? = when {
        valor.isBlank() -> "Campo obligatorio"
        valor.length < 6 -> "Debe tener al menos 6 caracteres"
        else -> null
    }
}
