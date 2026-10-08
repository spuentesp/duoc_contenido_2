package com.example.ejemploguias.papers

import com.example.ejemploguias.papers.data.OpenApi
import com.example.ejemploguias.papers.data.PaperDao
import com.example.ejemploguias.papers.data.PaperEntity
import com.example.ejemploguias.papers.data.dto.AuthorshipDto
import com.example.ejemploguias.papers.data.dto.AutorDto
import com.example.ejemploguias.papers.data.dto.MetaDto
import com.example.ejemploguias.papers.data.dto.RespuestaBusquedaDto
import com.example.ejemploguias.papers.data.dto.UbicacionDto
import com.example.ejemploguias.papers.data.dto.WorkDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Fakes escritos a mano (sin Mockito). Funcionan porque PaperRepository
 * depende de las INTERFACES OpenApi y PaperDao, nunca de implementaciones.
 */

/** Devuelve la respuesta configurada o lanza la excepción pedida. */
internal class FakeOpenApi(
    private val respuesta: RespuestaBusquedaDto? = null,
    private val fallo: Exception? = null
) : OpenApi {
    override suspend fun buscar(consulta: String, porPagina: Int): RespuestaBusquedaDto {
        fallo?.let { throw it }
        return requireNotNull(respuesta) { "FakeOpenApi sin respuesta configurada" }
    }
}

/** "Base de datos" en memoria: una lista que emite su nuevo valor al cambiar. */
internal class FakePaperDao : PaperDao {
    private val filas = MutableStateFlow<List<PaperEntity>>(emptyList())

    override fun favoritos(): Flow<List<PaperEntity>> = filas

    override suspend fun obtenerPorId(id: String): PaperEntity? =
        filas.value.firstOrNull { it.id == id }

    override suspend fun guardar(paper: PaperEntity) {
        filas.value = filas.value.filterNot { it.id == paper.id } + paper
    }

    override suspend fun actualizarNota(id: String, nota: String) {
        filas.value = filas.value.map { if (it.id == id) it.copy(nota = nota) else it }
    }

    override suspend fun eliminar(paper: PaperEntity) {
        filas.value = filas.value.filterNot { it.id == paper.id }
    }
}

/**
 * Respuesta con la forma real de OpenAlex (verificada con curl):
 * objeto con meta + results[], y cada work con id, doi, title, display_name,
 * publication_year, authorships[].author.display_name y primary_location.
 * El segundo work cubre los casos anulables (sin DOI, sin autoría, sin ubicación).
 */
internal fun respuestaDeEjemplo(): RespuestaBusquedaDto = RespuestaBusquedaDto(
    meta = MetaDto(total = 2),
    resultados = listOf(
        WorkDto(
            id = "https://openalex.org/W4312611271",
            doi = "https://doi.org/10.1145/3524613.3527811",
            titulo = "Evaluating swift-to-kotlin and kotlin-to-swift transpilers",
            nombreMostrado = "Evaluating swift-to-kotlin and kotlin-to-swift transpilers",
            anioPublicacion = 2022,
            autorias = listOf(AuthorshipDto(autor = AutorDto(nombre = "Larissa Schneider"))),
            ubicacionPrincipal = UbicacionDto(
                urlLanding = "https://doi.org/10.1145/3524613.3527811"
            )
        ),
        WorkDto(
            id = "https://openalex.org/W123456789",
            doi = null,
            titulo = null,
            nombreMostrado = "Paper sin DOI",
            anioPublicacion = 2020,
            autorias = null,
            ubicacionPrincipal = null
        )
    )
)
