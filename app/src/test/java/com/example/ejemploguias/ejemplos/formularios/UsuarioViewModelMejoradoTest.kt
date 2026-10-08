package com.example.ejemploguias.ejemplos.formularios

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Prueba de la validación en tiempo real de la versión mejorada:
 * el error aparece al tocar el campo y se limpia al corregirlo,
 * sin esperar al botón de enviar.
 */
class UsuarioViewModelMejoradoTest {

    @Test
    fun `campos sin tocar - no muestran errores`() = runTest {
        val viewModel = UsuarioViewModelMejorado()

        val errores = viewModel.estado.value.errores
        assertNull(errores.nombre)
        assertNull(errores.correo)
        assertNull(errores.clave)
        assertNull(errores.direccion)
        assertFalse(viewModel.estado.value.tocado.correo)
    }

    @Test
    fun `correo invalido escrito - el error aparece en vivo`() = runTest {
        val viewModel = UsuarioViewModelMejorado()

        viewModel.onCorreoChange("ana.perez.duoc.cl")

        assertEquals("Correo inválido", viewModel.estado.value.errores.correo)
        assertTrue(viewModel.estado.value.tocado.correo)
        assertNull(viewModel.estado.value.errores.nombre)
    }

    @Test
    fun `corregir el correo - el error desaparece sin volver a enviar`() = runTest {
        val viewModel = UsuarioViewModelMejorado()

        viewModel.onCorreoChange("ana.perez.duoc.cl")
        viewModel.onCorreoChange("ana@duoc.cl")

        assertNull(viewModel.estado.value.errores.correo)
    }

    @Test
    fun `clave corta escrita - muestra el error de longitud en vivo`() = runTest {
        val viewModel = UsuarioViewModelMejorado()

        viewModel.onClaveChange("1234")

        assertEquals("Debe tener al menos 6 caracteres", viewModel.estado.value.errores.clave)
    }

    @Test
    fun `enviar incompleto - marca todos los campos y devuelve false`() = runTest {
        val viewModel = UsuarioViewModelMejorado()
        viewModel.onNombreChange("Ana Pérez")

        val esValido = viewModel.validarFormulario()

        assertFalse(esValido)
        val estado = viewModel.estado.value
        assertEquals("Campo obligatorio", estado.errores.correo)
        assertEquals("Campo obligatorio", estado.errores.clave)
        assertEquals("Campo obligatorio", estado.errores.direccion)
        assertNull(estado.errores.nombre)
        assertTrue(estado.tocado.nombre)
        assertTrue(estado.tocado.correo)
        assertTrue(estado.tocado.clave)
        assertTrue(estado.tocado.direccion)
    }

    @Test
    fun `formulario valido - devuelve true sin errores`() = runTest {
        val viewModel = UsuarioViewModelMejorado()
        viewModel.onNombreChange("Ana Pérez")
        viewModel.onCorreoChange("ana@duoc.cl")
        viewModel.onClaveChange("secreta123")
        viewModel.onDireccionChange("Av. Siempreviva 742")

        val esValido = viewModel.validarFormulario()

        assertTrue(esValido)
        val errores = viewModel.estado.value.errores
        assertNull(errores.nombre)
        assertNull(errores.correo)
        assertNull(errores.clave)
        assertNull(errores.direccion)
    }

    @Test
    fun `alternar mostrar clave - actualiza el estado`() = runTest {
        val viewModel = UsuarioViewModelMejorado()

        viewModel.onMostrarClaveChange(true)

        assertTrue(viewModel.estado.value.mostrarClave)
    }
}
