package com.example.ejemploguias.ejemplos.estructura

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ejemploguias.ui.components.BloqueExplicacion
import com.example.ejemploguias.ui.components.PantallaExplicacion

private val ArbolProyecto = """
    AppNombre_GrupoX/
    ├── app/
    │   └── src/main/
    │       ├── java/com/example/appnombre/
    │       │   ├── ui/               Composables de pantallas
    │       │   │   ├── HomeScreen.kt
    │       │   │   └── components/
    │       │   ├── viewmodel/        ViewModels de la lógica de presentación
    │       │   │   └── HomeViewModel.kt
    │       │   ├── model/            Clases de datos y DTOs
    │       │   │   └── Tarea.kt
    │       │   ├── repository/       (opcional) simular origen de datos
    │       │   │   └── TareaRepository.kt
    │       │   └── navigation/       Rutas
    │       │       └── AppNavHost.kt
    │       ├── res/
    │       └── AndroidManifest.xml
    ├── build.gradle.kts
    ├── settings.gradle.kts
    └── gradle/
""".trimIndent()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstructuraDemoScreen(onAbrirExplicacion: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Guía 7 · Estructura del proyecto") },
                actions = {
                    IconButton(onClick = onAbrirExplicacion) {
                        Icon(Icons.Default.Info, contentDescription = "¿Cómo se hizo?")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Réplica de lo que pide la Guía 7: carpetas del patrón MVVM, " +
                    "nombre por convención y entorno colaborativo con GitHub y Trello.",
                style = MaterialTheme.typography.bodyMedium
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Nombre del proyecto (convención)",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "AppNombre_GrupoX",
                        fontFamily = FontFamily.Monospace,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Reemplaza AppNombre por el nombre de tu app y GrupoX por tu grupo (p. ej. AppTareas_Grupo3).",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Estructura MVVM canónica",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text(
                            text = ArbolProyecto,
                            modifier = Modifier.padding(12.dp),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Git · GitHub (repositorio privado)",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "• Crea un repositorio privado con el mismo nombre del proyecto.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "• Agrega a tu pareja como colaborador/a con permisos de escritura y a tu docente.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "• Primer commit: «Inicio de proyecto + estructura base MVVM».",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "• Trabaja en ramas feature/* (p. ej. feature/home-screen) e integra a main.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Trello (planificación en pareja)",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "• Tablero con al menos estas listas: «Por hacer», «En curso» y «Finalizado».",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "• Asigna las tareas reales de la semana y adjúntalas al repositorio (capturas o enlaces en la tarjeta).",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

private val BloquesEstructura = listOf(
    BloqueExplicacion(
        titulo = "1. Crear el proyecto (Guía 7, pág. 1-2)",
        parrafos = listOf(
            "Pasos 1-2 (Parte 1): con tu docente define el equipo de Proyecto Semestral y el tema de la app (nombre, alcance, requerimientos, reglas de negocio y contexto); entrégalo en un informe simple.",
            "Paso 4: crea un proyecto nuevo en Android Studio con la plantilla «Empty Compose Activity».",
            "Paso 5: aplica la convención de nombre AppNombre_GrupoX."
        )
    ),
    BloqueExplicacion(
        titulo = "2. Carpetas del patrón MVVM (Guía 7, pág. 2)",
        parrafos = listOf(
            "Paso 6: dentro del módulo main crea ui/ (composables de pantallas), viewmodel/ (ViewModels de la lógica de presentación), model/ (clases de datos y DTOs) y repository/ (opcional, «para simular origen de datos»).",
            "Esta app agrega además navigation/ para las rutas, que verás a detalle en la Guía 10.",
            "El árbol monoespaciado de la pantalla anterior es la referencia a replicar en tu proyecto."
        ),
        codigo = """
            ui/             Composables de pantallas
            viewmodel/      ViewModels de la lógica de presentación
            model/          Clases de datos y DTOs
            repository/     (opcional) para simular origen de datos
            navigation/     Rutas
        """
    ),
    BloqueExplicacion(
        titulo = "3. Device Manager y primera corrida (Guía 7, pág. 2)",
        parrafos = listOf(
            "Paso 7: crea un dispositivo virtual con el Device Manager para poder ejecutar la app.",
            "Paso 8: ejecuta el proyecto y verifica que la app aparezca en el dispositivo.",
            "Referencias oficiales citadas por la guía: developer.android.com/studio/projects/create-project y developer.android.com/jetpack/compose/setup."
        )
    ),
    BloqueExplicacion(
        titulo = "4. Colaboración con GitHub (Guía 7, pág. 2)",
        parrafos = listOf(
            "Paso 1: crea un repositorio privado en GitHub con el mismo nombre del proyecto.",
            "Paso 2: agrega a tu pareja como colaborador/a con permisos de escritura y a tu docente.",
            "Paso 3: realiza el primer commit con el mensaje «Inicio de proyecto + estructura base MVVM».",
            "Paso 5: asigna las tareas reales de la semana y adjúntalas al repositorio; conviene trabajar en ramas feature/* (p. ej. feature/home-screen)."
        )
    ),
    BloqueExplicacion(
        titulo = "5. Tablero Trello (Guía 7, pág. 2)",
        parrafos = listOf(
            "Paso 4: crea un tablero en Trello con al menos las listas «Por hacer», «En curso» y «Finalizado».",
            "Paso 5: reparte las tareas de la semana entre esas listas y vincula cada tarjeta con el repositorio (capturas o enlaces)."
        )
    ),
    BloqueExplicacion(
        titulo = "6. Dependencias que citan las guías",
        parrafos = listOf(
            "La guía agrega estas dependencias en el build.gradle.kts del módulo app:",
            "Esta app usa esas mismas librerías con versiones actualizadas (lifecycle 2.8.6 y kotlinx-coroutines 1.8.1), excepto navigation-compose, que se mantiene en 2.7.7, idéntico a la guía."
        ),
        codigo = """
            implementation("androidx.navigation:navigation-compose:2.7.7")
            implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0-rc02")
            implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
        """
    )
)

@Composable
fun EstructuraExplicacionScreen(onVolver: () -> Unit) {
    PantallaExplicacion(
        titulo = "Guía 7 · ¿Cómo se hizo?",
        bloques = BloquesEstructura,
        onVolver = onVolver
    )
}
