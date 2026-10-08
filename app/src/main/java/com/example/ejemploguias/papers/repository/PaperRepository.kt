package com.example.ejemploguias.papers.repository

import com.example.ejemploguias.papers.data.OpenApi
import com.example.ejemploguias.papers.data.PaperDao
import com.example.ejemploguias.papers.data.PaperEntity
import com.example.ejemploguias.papers.data.aEntidad
import com.example.ejemploguias.papers.data.aPaper
import com.example.ejemploguias.papers.model.Paper
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow

/**
 * Resultado envuelto para la UI: nunca lanzamos excepciones hacia arriba.
 * Cargando / Exito / Error son los 3 estados que una pantalla puede pintar.
 */
sealed class ResultadoApi<out T> {
    data object Cargando : ResultadoApi<Nothing>()
    data class Exito<out T>(val datos: T) : ResultadoApi<T>()
    data class Error(val mensaje: String) : ResultadoApi<Nothing>()
}

/**
 * Única puerta entre la UI y los datos. La Guía 7 deja `repository/` como
 * placeholder "para simular origen de datos"; aquí es real y decide POR CADA
 * método si toca la red (Retrofit/OpenAlex) o la base local (Room/SQLite).
 * La UI solo conoce esta clase: nunca importa Retrofit ni Room.
 */
class PaperRepository(
    private val api: OpenApi,
    private val dao: PaperDao
) {

    /** Siempre contra la API pública. Convierte cualquier fallo en ResultadoApi.Error. */
    suspend fun buscarPapers(query: String): ResultadoApi<List<Paper>> {
        return try {
            val respuesta = api.buscar(query, 25)
            val papers = respuesta.resultados.orEmpty().mapNotNull { it.aPaper() }
            ResultadoApi.Exito(papers)
        } catch (cancelacion: CancellationException) {
            throw cancelacion
        } catch (error: Exception) {
            ResultadoApi.Error(error.message ?: "No se pudo consultar OpenAlex")
        }
    }

    /**
     * Siempre contra SQLite. REPLACE es idempotente por id, pero borra la fila
     * anterior entera: por eso recuperamos la nota previa para no perderla.
     */
    suspend fun guardar(paper: Paper) {
        val existente = dao.obtenerPorId(paper.id)
        dao.guardar(
            paper.aEntidad(
                nota = existente?.nota ?: "",
                fechaGuardado = existente?.fechaGuardado ?: System.currentTimeMillis()
            )
        )
    }

    /** Siempre desde Room: este Flow vive sin conexión. */
    fun favoritos(): Flow<List<PaperEntity>> = dao.favoritos()

    suspend fun actualizarNota(id: String, nota: String) {
        dao.actualizarNota(id, nota)
    }

    suspend fun eliminar(paper: PaperEntity) {
        dao.eliminar(paper)
    }
}
