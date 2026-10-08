package com.example.ejemploguias.papers

import com.example.ejemploguias.papers.repository.PaperRepository
import com.example.ejemploguias.papers.repository.ResultadoApi
import com.example.ejemploguias.papers.viewmodel.BuscarPapersViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Prueba opcional del ViewModel con un repository real armado sobre fakes.
 * El único truco: Dispatchers.setMain reemplaza el hilo principal de Android
 * para que viewModelScope funcione en JVM.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class BuscarPapersViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun configurar() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun limpiar() {
        Dispatchers.resetMain()
    }

    private fun viewModelCon(api: FakeOpenApi): BuscarPapersViewModel =
        BuscarPapersViewModel(PaperRepository(api, FakePaperDao()))

    @Test
    fun `buscar exitoso deja Exito con la lista de papers`() = runTest(dispatcher) {
        val viewModel = viewModelCon(FakeOpenApi(respuestaDeEjemplo()))

        viewModel.cambiarConsulta("kotlin")
        viewModel.buscar()
        advanceUntilIdle()

        val estado = viewModel.estado.value
        assertTrue(estado is ResultadoApi.Exito)
        assertEquals(2, (estado as ResultadoApi.Exito).datos.size)
    }

    @Test
    fun `buscar fallido deja Error con el mensaje sin lanzar excepcion`() = runTest(dispatcher) {
        val viewModel = viewModelCon(FakeOpenApi(fallo = RuntimeException("sin conexión")))

        viewModel.cambiarConsulta("kotlin")
        viewModel.buscar()
        advanceUntilIdle()

        val estado = viewModel.estado.value
        assertTrue(estado is ResultadoApi.Error)
        assertEquals("sin conexión", (estado as ResultadoApi.Error).mensaje)
    }

    @Test
    fun `guardar marca el id como guardado y emite el evento de snackbar`() =
        runTest(dispatcher) {
            val viewModel = viewModelCon(FakeOpenApi(respuestaDeEjemplo()))
            val eventos = mutableListOf<String>()

            // backgroundScope se cancela solo al terminar el test (colectores infinitos).
            backgroundScope.launch { viewModel.idsGuardados.collect { } }
            backgroundScope.launch { viewModel.eventos.collect { eventos.add(it) } }
            advanceUntilIdle()

            viewModel.cambiarConsulta("kotlin")
            viewModel.buscar()
            advanceUntilIdle()
            val papers = (viewModel.estado.value as ResultadoApi.Exito).datos

            viewModel.guardar(papers.first())
            advanceUntilIdle()

            assertTrue(viewModel.idsGuardados.value.contains("https://openalex.org/W4312611271"))
            assertTrue(eventos.any { it.startsWith("Guardado en favoritos") })
        }
}
