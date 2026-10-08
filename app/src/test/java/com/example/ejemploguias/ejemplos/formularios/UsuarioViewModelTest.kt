package com.example.ejemploguias.ejemplos.formularios

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Prueba de la validación del UsuarioViewModel (espejo Guía 11, Parte 2).
 * Sin Mockito ni emulador: se instancia el ViewModel directamente
 * y se lee estado.value.
 */
class UsuarioViewModelTest {

    @Test
    fun `campos vacios - la validacion falla y los errores se poblan`() = runTest {
        val viewModel = UsuarioViewModel()

        val esValido = viewModel.validarFormulario()

        assertFalse(esValido)
        val errores = viewModel.estado.value.errores
        assertEquals("Campo obligatorio", errores.nombre)
        assertEquals("Correo inválido", errores.correo)
        assertEquals("Debe tener al menos 6 caracteres", errores.clave)
        assertEquals("Campo obligatorio", errores.direccion)
    }

    @Test
    fun `correo sin arroba - marca el correo y deja pasar los demas campos`() = runTest {
        val viewModel = UsuarioViewModel()
        viewModel.onNombreChange("Ana Pérez")
        viewModel.onCorreoChange("ana.perez.duoc.cl")
        viewModel.onClaveChange("secreta123")
        viewModel.onDireccionChange("Av. Siempreviva 742")

        val esValido = viewModel.validarFormulario()

        assertFalse(esValido)
        val errores = viewModel.estado.value.errores
        assertEquals("Correo inválido", errores.correo)
        assertNull(errores.nombre)
        assertNull(errores.clave)
        assertNull(errores.direccion)
    }

    @Test
    fun `clave menor a 6 caracteres - marca solo la clave`() = runTest {
        val viewModel = UsuarioViewModel()
        viewModel.onNombreChange("Ana Pérez")
        viewModel.onCorreoChange("ana@duoc.cl")
        viewModel.onClaveChange("12345")
        viewModel.onDireccionChange("Av. Siempreviva 742")

        val esValido = viewModel.validarFormulario()

        assertFalse(esValido)
        val errores = viewModel.estado.value.errores
        assertEquals("Debe tener al menos 6 caracteres", errores.clave)
        assertNull(errores.nombre)
        assertNull(errores.correo)
        assertNull(errores.direccion)
    }

    @Test
    fun `formulario valido - devuelve true y no deja errores`() = runTest {
        val viewModel = UsuarioViewModel()
        viewModel.onNombreChange("Ana Pérez")
        viewModel.onCorreoChange("ana@duoc.cl")
        viewModel.onClaveChange("secreta123")
        viewModel.onDireccionChange("Av. Siempreviva 742")
        viewModel.onAceptaTerminosChange(true)

        val esValido = viewModel.validarFormulario()

        assertTrue(esValido)
        val errores = viewModel.estado.value.errores
        assertNull(errores.nombre)
        assertNull(errores.correo)
        assertNull(errores.clave)
        assertNull(errores.direccion)
        assertTrue(viewModel.estado.value.aceptaTerminos)
    }

    @Test
    fun `escribir en un campo limpia su error anterior`() = runTest {
        val viewModel = UsuarioViewModel()
        viewModel.validarFormulario()
        viewModel.onNombreChange("Ana Pérez")

        assertNull(viewModel.estado.value.errores.nombre)
        assertEquals("Correo inválido", viewModel.estado.value.errores.correo)
    }
}
