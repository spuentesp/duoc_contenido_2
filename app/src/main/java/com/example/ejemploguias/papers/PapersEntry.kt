package com.example.ejemploguias.papers

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ejemploguias.papers.ui.BuscarPapersScreen
import com.example.ejemploguias.papers.ui.FavoritosScreen
import com.example.ejemploguias.papers.viewmodel.BuscarPapersViewModel
import com.example.ejemploguias.papers.viewmodel.FavoritosViewModel
import com.example.ejemploguias.ui.components.BloqueExplicacion
import com.example.ejemploguias.ui.components.PantallaExplicacion
import kotlinx.coroutines.launch

/** Secciones internas de la demo (estado local con rememberSaveable). */
enum class SeccionPapers { BUSCAR, FAVORITOS }

/**
 * Demo capstone: buscar papers en OpenAlex (API) y guardarlos con notas en
 * SQLite (Room), orquestado por PaperRepository. Dos pestañas en una sola
 * pantalla, con snackbar para eventos puntuales (patrón Guía 10).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PapersDemoScreen(onAbrirExplicacion: () -> Unit) {
    var seccion by rememberSaveable { mutableStateOf(SeccionPapers.BUSCAR) }
    val contexto = LocalContext.current
    val buscarViewModel: BuscarPapersViewModel =
        viewModel(factory = BuscarPapersViewModel.fabrica(contexto))
    val favoritosViewModel: FavoritosViewModel =
        viewModel(factory = FavoritosViewModel.fabrica(contexto))
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        launch {
            buscarViewModel.eventos.collect { snackbarHostState.showSnackbar(it) }
        }
        launch {
            favoritosViewModel.eventos.collect { snackbarHostState.showSnackbar(it) }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Papers · API + SQLite",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    TextButton(onClick = onAbrirExplicacion) {
                        Text("¿Cómo se hizo?")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = seccion == SeccionPapers.BUSCAR,
                    onClick = { seccion = SeccionPapers.BUSCAR },
                    icon = { Icon(Icons.Filled.Search, contentDescription = null) },
                    label = { Text("Buscar") }
                )
                NavigationBarItem(
                    selected = seccion == SeccionPapers.FAVORITOS,
                    onClick = { seccion = SeccionPapers.FAVORITOS },
                    icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
                    label = { Text("Favoritos") }
                )
            }
        }
    ) { innerPadding ->
        when (seccion) {
            SeccionPapers.BUSCAR -> BuscarPapersScreen(
                viewModel = buscarViewModel,
                modifier = Modifier.padding(innerPadding)
            )

            SeccionPapers.FAVORITOS -> FavoritosScreen(
                viewModel = favoritosViewModel,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

/** Panel "¿Cómo se hizo?": 7 bloques numerados + diagrama de flujo. */
@Composable
fun PapersExplicacionScreen(onVolver: () -> Unit) {
    PantallaExplicacion(
        titulo = "¿Cómo se hizo? · Papers",
        onVolver = onVolver,
        bloques = listOf(
            BloqueExplicacion(
                titulo = "1. ¿Por qué un repository?",
                parrafos = listOf(
                    "En la Guía 7 la carpeta repository/ queda como placeholder «para simular el origen de datos». " +
                        "Aquí esa carpeta es real: PaperRepository es la ÚNICA puerta que la UI usa para leer y escribir datos.",
                    "El repository decide método por método si toca la red (OpenAlex con Retrofit) o la base local " +
                        "(SQLite con Room). Así ninguna pantalla importa retrofit2.* ni androidx.room.*: solo conoce " +
                        "Paper, PaperEntity y ResultadoApi.",
                    "Esta es la estructura modelo a copiar en el proyecto semestral: modelo de dominio, capa de " +
                        "datos (API y/o Room), repository, ViewModel y UI."
                ),
                codigo = """
                    papers/
                    ├── model/       Paper (modelo de dominio)
                    ├── data/        DTOs + OpenApi (Retrofit) y PaperEntity/Dao/Database (Room)
                    ├── repository/  PaperRepository + ResultadoApi  ← única puerta de la UI
                    ├── viewmodel/   BuscarPapersViewModel, FavoritosViewModel
                    └── ui/          BuscarPapersScreen, FavoritosScreen, componentes/
                """
            ),
            BloqueExplicacion(
                titulo = "2. Flujo de datos completo",
                parrafos = listOf(
                    "Buscar: la pantalla manda la consulta al ViewModel, el ViewModel delega en el repository y " +
                        "el repository elige destino (API o Room) y devuelve un ResultadoApi.",
                    "Favoritos: el DAO expone un Flow<List<PaperEntity>>, el ViewModel lo convierte en StateFlow " +
                        "con stateIn y la pantalla lo recolecta con collectAsStateWithLifecycle. Cualquier INSERT, " +
                        "UPDATE o DELETE re-emite la lista y la UI se redibuja sola: así se ve el patrón reactivo."
                ),
                codigo = """
                    BuscarPapersScreen ──► BuscarPapersViewModel ──► PaperRepository ──┬──► OpenApi (Retrofit → api.openalex.org)
                                                                                      └──► PaperDao  (Room → SQLite local)

                    FavoritosScreen ◄── StateFlow ◄── Flow<List<PaperEntity>> ◄── PaperDao ◄── Room
                         (al guardar / editar nota / eliminar, Room re-emite y la lista cambia sola)
                """
            ),
            BloqueExplicacion(
                titulo = "3. Room en 4 pasos",
                parrafos = listOf(
                    "Paso 1 — Entity: una clase = una tabla. @PrimaryKey es la clave primaria: con ella el " +
                        "inserto es idempotente (REPLACE reemplaza la fila que tenga el mismo id).",
                    "Paso 2 — Dao: interface con las consultas. Los lectores devuelven Flow (reactivo) y los " +
                        "escritores son suspend (corutina, fuera del hilo principal).",
                    "Paso 3 — Database: abre (o crea) el archivo .db y entrega el Dao. Se usa un singleton " +
                        "estándar con @Volatile + synchronized para que toda la app comparta la misma base.",
                    "Paso 4 — KSP: la línea ksp(libs.androidx.room.compiler) en build.gradle.kts hace que el " +
                        "compilador genere la implementación real de Dao y Database en tiempo de compilación. " +
                        "Sin esa línea, nada de Room compila."
                ),
                codigo = """
                    @Entity(tableName = "papers")
                    data class PaperEntity(
                        @PrimaryKey val id: String,
                        val titulo: String,
                        val autores: String,
                        val anio: Int,
                        val doi: String?,
                        val url: String?,
                        val nota: String,
                        val fechaGuardado: Long
                    )

                    @Dao
                    interface PaperDao {
                        fun favoritos(): Flow<List<PaperEntity>>
                        @Query("SELECT * FROM papers WHERE id = :id")
                        suspend fun obtenerPorId(id: String): PaperEntity?
                        @Insert(onConflict = OnConflictStrategy.REPLACE)
                        suspend fun guardar(paper: PaperEntity)
                    }

                    @Database(entities = [PaperEntity::class], version = 1)
                    abstract class PaperDatabase : RoomDatabase() {
                        abstract fun paperDao(): PaperDao
                    }
                """
            ),
            BloqueExplicacion(
                titulo = "4. Retrofit en 3 pasos",
                parrafos = listOf(
                    "Paso 1 — Interfaz: cada método es un endpoint. @GET(\"works\") apunta a /works de la API y " +
                        "@Query agrega los parámetros de la URL (?search=...&per-page=25). El suspend hace la " +
                        "petición en segundo plano sin Thread ni runBlocking.",
                    "Paso 2 — Cliente: Retrofit.Builder con la base URL arma el cliente HTTP una sola vez.",
                    "Paso 3 — Converter-gson: GsonConverterFactory transforma el JSON en los DTOs declarados en " +
                        "data/dto (los nombres exactos del JSON se fijan con @SerializedName). Verificado con " +
                        "curl: la respuesta trae meta + results[], y cada work tiene id, doi, title, " +
                        "display_name, publication_year, authorships[].author.display_name y " +
                        "primary_location.landing_page_url."
                ),
                codigo = """
                    interface OpenApi {
                        @GET("works")
                        suspend fun buscar(
                            @Query("search") consulta: String,
                            @Query("per-page") porPagina: Int
                        ): RespuestaBusquedaDto
                    }

                    Retrofit.Builder()
                        .baseUrl("https://api.openalex.org/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(OpenApi::class.java)
                """
            ),
            BloqueExplicacion(
                titulo = "5. Patrón offline: API para buscar, SQLite para favoritos",
                parrafos = listOf(
                    "La regla es simple y se puede explicar en una frase: buscar va SIEMPRE a la API (los " +
                        "resultados deben estar frescos) y favoritos van SIEMPRE a SQLite (deben existir aunque " +
                        "el teléfono esté sin datos).",
                    "No hay caché oculta ni doble origen: cada método del repository nombra su fuente. Por eso " +
                        "la pestaña Favoritos funciona en modo avión, y por eso una nota escrita sobre un paper " +
                        "sobrevive a cerrar y abrir la app (Room persiste en papers.db)."
                ),
                codigo = """
                    suspend fun buscarPapers(query: String) = api.buscar(query, 25)   // SIEMPRE la API
                    fun favoritos(): Flow<List<PaperEntity>> = dao.favoritos()        // SIEMPRE Room
                    suspend fun guardar(paper: Paper) = dao.guardar(...)              // SIEMPRE Room
                """
            ),
            BloqueExplicacion(
                titulo = "6. Manejo de errores con ResultadoApi",
                parrafos = listOf(
                    "El repository jamás lanza excepciones hacia la UI: envuelve todo en un sealed class con " +
                        "tres estados pintables: Cargando (spinner), Exito (lista) y Error (mensaje + botón " +
                        "Reintentar). Si la red falla, catch convierte la excepción en Error(mensaje).",
                    "La pantalla hace un when exhaustivo sobre ResultadoApi: el compilador obliga a cubrir los " +
                        "tres casos, así un estado olvidado es un error de compilación y no una pantalla en blanco. " +
                        "La única excepción que se re-lanza es CancellationException, para no romper la cancelación " +
                        "de corutinas de Kotlin (buena práctica que conviene enseñar desde ya)."
                ),
                codigo = """
                    sealed class ResultadoApi<out T> {
                        data object Cargando : ResultadoApi<Nothing>()
                        data class Exito<out T>(val datos: T) : ResultadoApi<T>()
                        data class Error(val mensaje: String) : ResultadoApi<Nothing>()
                    }
                """
            ),
            BloqueExplicacion(
                titulo = "7. Mejora recomendada para tu proyecto",
                parrafos = listOf(
                    "Separa los DTOs del modelo de dominio: Retrofit serializa DTOs (data/dto con @SerializedName) " +
                        "y la UI usa Paper/PaperEntity. El modelo de dominio no conoce Gson ni las claves del " +
                        "JSON, así que si OpenAlex cambia su respuesta solo se tocan los DTOs y el mapper.",
                    "El repository es lo que impide que la UI conozca Room y Retrofit: por eso las pantallas se " +
                        "pueden reescribir sin tocar la capa de datos, y por eso el repository se pudo probar en " +
                        "JVM puro con un OpenApi falso y un PaperDao falso escritos a mano (sin emulador y sin " +
                        "Mockito): PaperRepositoryTest. Para tu semestre, esa combinación — DTOs separados + " +
                        "repository + tests con fakes — es la mejora que se espera sobre el placeholder de la Guía 7."
                ),
                codigo = """
                    // data/dto/DtoOpenAlex.kt — solo lo entiende Gson
                    data class WorkDto(
                        @SerializedName("title") val titulo: String?,
                        @SerializedName("publication_year") val anioPublicacion: Int?
                    )

                    // model/Paper.kt — solo lo entiende la UI
                    data class Paper(val id: String, val titulo: String, val anio: Int, ...)

                    // data/Mappers.kt — el puente entre mundos
                    fun WorkDto.aPaper(): Paper? = ...
                """
            )
        )
    )
}
