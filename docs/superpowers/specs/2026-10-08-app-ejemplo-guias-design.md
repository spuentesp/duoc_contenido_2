# App Ejemplo Guías DSY1105 — Diseño

Fecha: 2026-10-08. Estado: aprobado en conversación (Secciones 1–3; el usuario autorizó acelerar la implementación sin esperar revisión formal).

## Objetivo

App Android funcional en Kotlin que sirva de **material de enseñanza** para DSY1105 (Duoc UC, EA2): cada elemento de las 6 guías PDF de `docs/` se muestra funcionando, **en espejo del código de la guía**, y donde hay una mejora práctica se **demuestra además** (mejor método funcionando, no solo anotado). Complementa con lo que las guías no cubren: SQLite (Room), consumo de API REST y un repository que conecta SQLite con la API — con papers desde OpenAlex.

## Decisiones tomadas

| Decisión | Elección |
|---|---|
| Forma | Catálogo didáctico (menú de demos) + capstone Papers |
| Guardar papers | Favoritos + notas (CRUD completo) |
| Persistencia | Room sobre SQLite |
| Enfoque de código | C: espejo de la guía como material principal + mejora demostrada con switch "Guía \| Mejorado" |
| API | OpenAlex (`api.openalex.org/works?search=…`), JSON, sin API key |
| Idioma | UI y código en español; nombres de clases idénticos a los de las guías |

## Arquitectura

Proyecto Android de un módulo (`app`), Kotlin + Jetpack Compose Material 3, MVVM, minSdk 33 / compileSdk 35 (pauta "API 33 o superior" de las guías).

```
com.example.ejemploguias/
├── MainActivity.kt          # NavHost + rutas (estilo Guía 10 mejorado: AppNavigation separada)
├── navigation/Screen.kt     # sealed class Screen (estilo Guía 10)
├── ui/theme/                # Material 3
├── ui/utils/WindowSizeUtils.kt        # Guía 9
├── ui/components/           # ExplicacionDemo.kt, SelectorVersion.kt (compartidos)
├── ui/screens/              # MenuScreen + pantalla documental Guía 7
├── ejemplos/                # ISLAS espejo: una por guía, con sus mismos nombres de clase
│   ├── pantallabase/    # G8
│   ├── adaptabilidad/   # G9
│   ├── navegacion/      # G10 (Screen-locale, NavigationEvent, MainViewModel, 3 pantallas)
│   ├── formularios/     # G11 (UsuarioUiState, UsuarioErrores, UsuarioViewModel, Registro/Resumen)
│   └── camara/          # G13 (PerfilScreen, ImagenInteligente, TakePicture/GetContent + FileProvider)
└── papers/                # CAPSTONE con estructura canónica de Guía 7
    ├── model/  data/ (Room + OpenApi)  repository/  viewmodel/  ui/
```

Contratos compartidos (los define el esqueleto, no se editan desde las islas):
- `PantallaExplicacion(titulo, bloques: List<BloqueExplicacion>, onVolver)` — paneles "¿Cómo se hizo?" con pasos referenciando la guía (pág. incluida) y snippets; la mejora va como bloque aparte con `codigo` comparado.
- `SelectorVersion(version: VersionDemo, onCambiar)` — switch GUIA/MEJORADA.

## Mapa de demos y mejoras demostradas

| Entrada | Espejo (guía) | Mejora demostrada en vivo |
|---|---|---|
| Guía 7 · Estructura | pantalla documental (carpetas MVVM, Git, Trello, dependencias citadas) | — |
| G8 · Pantalla base | `HomeScreen` Scaffold/TopAppBar/Text/Button/Image/Preview | state hoisting + ViewModel con estado en el botón |
| G9 · Adaptabilidad | `WindowSizeUtils` + Compacta/Mediana/Expandida | un solo composable adaptativo |
| G10 · Navegación | `Screen`, `NavigationEvent`, `MainViewModel` con `CoroutineScope(Dispatchers.Main)`, drawer + bottom bar + NavHost | `viewModelScope` + NavHost en `AppNavigation.kt` |
| G11 · Formularios | `UsuarioUiState`/`Errores`, validación al enviar, Registro/Resumen, VM compartido | validación en tiempo real por campo |
| G13 · Cámara | `PerfilScreen`, `ImagenInteligente`, `TakePicture`/`GetContent` + FileProvider, `Uri?` en StateFlow | foto persistida en almacenamiento interno (sobrevive rotación/reinicio) |
| Extensión · Papers | — | ya es la extensión: OpenAlex + Retrofit + Room + `PaperRepository` |

## Capstone Papers

```
BuscarPapersScreen ─→ PapersViewModel ─→ PaperRepository ─┬─→ OpenApi (Retrofit+Gson) · GET /works?search=
FavoritosScreen  ←── StateFlow<List<PaperEntity>> ←───────┴─→ PaperDao (Room) · insertar/leer/borrar/nota
```

- `PaperEntity`: id (id OpenAlex), titulo, autores, anio, doi, url, nota, fechaGuardado.
- `PaperRepository`: `buscarPapers(query)`, `guardar(paper)`, `favoritos(): Flow<List<PaperEntity>>`, `actualizarNota(id, nota)`, `eliminar(paper)`. Materializa el `repository/` placeholder de la Guía 7.
- Guardar idempotente por DOI/id (sin duplicados). Sin abstract (OpenAlex lo entrega como índice invertido: ruido pedagógico).
- Estado: `StateFlow` (Guía 11); eventos puntuales tipo snackbar: `SharedFlow` (Guía 10).
- Errores: `sealed class ResultadoApi` (Cargando / Exito / Error) con reintento en pantalla.

## Versiones y dependencias

Toolchain actual para compilar (AGP 8.7.x, Gradle 8.11.x, Kotlin 2.0.x, Compose BOM 2024.10.x, KSP para Room 2.6.x, Retrofit 2.11.x). Las versiones que citan las guías (`navigation-compose:2.7.7` etc.) se muestran en los paneles "Cómo se hizo" como material didáctico. `navigation-compose` se fija a 2.7.7 (idéntico a las guías y compatible).

## Manejo de errores

- Demos espejo: sin manejo extra (fidelidad; la Guía 13 exige que cancelar no crashee y se respeta).
- Formularios: errores por campo estilo Guía 11 (`isError`, `supportingText`).
- Papers: `ResultadoApi` + "Reintentar"; guardar/notificar con snackbar.

## Verificación

1. `./gradlew assembleDebug` compila (toolchain ya instalado en este entorno: JDK 21 + Android SDK 35).
2. `./gradlew test`: validación de `UsuarioViewModel` (espejo G11) y `PaperRepository` con fakes (DAO + API).
3. Checklist manual en Android Studio/emulador del instructor: cada demo abre, switch Guía/Mejorado alterna, "¿Cómo se hizo?" navega, búsqueda de papers requiere red, favoritos persisten tras reinicio, galería en emulador, cámara en dispositivo físico (pauta Guía 13).

## Fuera de alcance

Hilt/Koin, CameraX, RxJava, autenticación, abstracts de papers, multi-módulo, i18n, persistencia de fotos en galería pública.
