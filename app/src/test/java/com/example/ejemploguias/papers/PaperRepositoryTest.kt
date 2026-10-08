package com.example.ejemploguias.papers

import com.example.ejemploguias.papers.data.aPaper
import com.example.ejemploguias.papers.repository.PaperRepository
import com.example.ejemploguias.papers.repository.ResultadoApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Pruebas del corazón del capstone: PaperRepository con un OpenApi falso y
 * un PaperDao falso escritos a mano (ver Fakes.kt). Corren en JVM puro,
 * sin emulador, sin red y sin Mockito.
 */
class PaperRepositoryTest {

    private val dao = FakePaperDao()
    private val repositorio = PaperRepository(FakeOpenApi(respuestaDeEjemplo()), dao)

    @Test
    fun `buscarPapers con la API OK devuelve Exito con los papers mapeados`() = runTest {
        val resultado = repositorio.buscarPapers("kotlin")

        assertTrue(resultado is ResultadoApi.Exito)
        val papers = (resultado as ResultadoApi.Exito).datos
        assertEquals(2, papers.size)

        val primero = papers[0]
        assertEquals("https://openalex.org/W4312611271", primero.id)
        assertEquals("Evaluating swift-to-kotlin and kotlin-to-swift transpilers", primero.titulo)
        assertEquals("Larissa Schneider", primero.autores)
        assertEquals(2022, primero.anio)
        assertEquals("10.1145/3524613.3527811", primero.doi)
        assertEquals("https://doi.org/10.1145/3524613.3527811", primero.url)

        val segundo = papers[1]
        assertEquals("Paper sin DOI", segundo.titulo)
        assertEquals("Autoría no disponible", segundo.autores)
        assertNull(segundo.doi)
        assertNull(segundo.url)
    }

    @Test
    fun `buscarPapers cuando la API falla devuelve Error con el mensaje`() = runTest {
        val conFallo = PaperRepository(
            FakeOpenApi(fallo = RuntimeException("sin conexión")),
            FakePaperDao()
        )

        val resultado = conFallo.buscarPapers("kotlin")

        assertTrue(resultado is ResultadoApi.Error)
        assertEquals("sin conexión", (resultado as ResultadoApi.Error).mensaje)
    }

    @Test
    fun `guardar inserta el paper en el dao`() = runTest {
        val paper = respuestaDeEjemplo().resultados!![0].aPaper()!!

        repositorio.guardar(paper)

        val guardado = requireNotNull(dao.obtenerPorId(paper.id))
        assertEquals(paper.titulo, guardado.titulo)
        assertEquals(paper.anio, guardado.anio)
        assertEquals("", guardado.nota)
    }

    @Test
    fun `guardar dos veces conserva la nota existente`() = runTest {
        val paper = respuestaDeEjemplo().resultados!![0].aPaper()!!

        repositorio.guardar(paper)
        repositorio.actualizarNota(paper.id, "leer el artículo completo")
        repositorio.guardar(paper)

        assertEquals("leer el artículo completo", dao.obtenerPorId(paper.id)?.nota)
    }

    @Test
    fun `favoritos refleja lo que hay en el dao`() = runTest {
        assertTrue(repositorio.favoritos().first().isEmpty())

        val paper = respuestaDeEjemplo().resultados!![0].aPaper()!!
        repositorio.guardar(paper)

        val lista = repositorio.favoritos().first()
        assertEquals(1, lista.size)
        assertEquals(paper.id, lista[0].id)
    }

    @Test
    fun `actualizarNota cambia la nota del paper guardado`() = runTest {
        val paper = respuestaDeEjemplo().resultados!![1].aPaper()!!
        repositorio.guardar(paper)

        repositorio.actualizarNota(paper.id, "comparar con la guía 11")

        assertEquals("comparar con la guía 11", dao.obtenerPorId(paper.id)?.nota)
    }

    @Test
    fun `eliminar quita el paper de favoritos`() = runTest {
        val paper = respuestaDeEjemplo().resultados!![0].aPaper()!!
        repositorio.guardar(paper)
        val entidad = dao.obtenerPorId(paper.id)!!

        repositorio.eliminar(entidad)

        assertNull(dao.obtenerPorId(paper.id))
        assertTrue(repositorio.favoritos().first().isEmpty())
    }
}
