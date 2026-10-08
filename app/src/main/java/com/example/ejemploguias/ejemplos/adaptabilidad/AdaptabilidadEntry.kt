package com.example.ejemploguias.ejemplos.adaptabilidad

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ejemploguias.ui.components.BloqueExplicacion
import com.example.ejemploguias.ui.components.PantallaExplicacion
import com.example.ejemploguias.ui.components.SelectorVersion
import com.example.ejemploguias.ui.components.VersionDemo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdaptabilidadDemoScreen(onAbrirExplicacion: () -> Unit) {
    var version by rememberSaveable { mutableStateOf(VersionDemo.GUIA) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Guía 9 · Adaptabilidad") },
                actions = {
                    IconButton(onClick = onAbrirExplicacion) {
                        Icon(Icons.Default.Info, contentDescription = "¿Cómo se hizo?")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            SelectorVersion(
                version = version,
                onCambiar = { version = it },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            HorizontalDivider()
            Text(
                text = "Gira el dispositivo o redimensiona la ventana para cambiar el tamaño (ancho: compacto / mediano / expandido).",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                when (version) {
                    VersionDemo.GUIA -> HomeScreen2()
                    VersionDemo.MEJORADA -> HomeScreenAdaptativa()
                }
            }
        }
    }
}

private val BloquesAdaptabilidad = listOf(
    BloqueExplicacion(
        titulo = "1. Preparar el proyecto (Guía 9, pág. 1-2)",
        parrafos = listOf(
            "Parte 1, pasos 1-3: con tu pareja decidan el nombre y la temática de la pantalla (simple, con al menos 1 imagen); descarga las imágenes a /res/drawable/ y trabaja con el repositorio de GitHub y el tablero de Trello.",
            "Parte 2, pasos 4-7: crea el proyecto con la plantilla «Empty Compose Activity», aplica la convención AppNombre_GrupoX, crea las carpetas MVVM y un dispositivo en el Device Manager."
        )
    ),
    BloqueExplicacion(
        titulo = "2. WindowSizeUtils.kt (Guía 9, pág. 2)",
        parrafos = listOf(
            "Paso 8: crea el archivo WindowSizeUtils.kt dentro de ui/utils/ con una función reutilizable que calcule el tamaño de pantalla para toda la app.",
            "La guía escribe calculateWindowSizeClass(LocalActivity.current as Activity); aquí se usa LocalContext.current as Activity, equivalente y compatible con más versiones de androidx.activity.",
            "En esta app ese archivo ya existe: ui/utils/WindowSizeUtils.kt."
        ),
        codigo = """
            @Composable
            fun obtenerWindowSizeClass(): WindowSizeClass {
                return calculateWindowSizeClass(LocalContext.current as Activity)
            }
        """
    ),
    BloqueExplicacion(
        titulo = "3. Un archivo por tamaño (Guía 9, pág. 2-3)",
        parrafos = listOf(
            "Parte 3, pasos 1-3: crea el paquete ui/screens/ y un archivo por cada tamaño: HomeScreenCompacta.kt, HomeScreenMediana.kt y HomeScreenExpandida.kt; implementa cada vista con un diseño diferente.",
            "Aquí: Compacta usa Column vertical (como el ejemplo de la guía), Mediana usa Row con imagen + texto y Expandida usa Row con 3 columnas y más espaciado.",
            "MaterialTheme.colorScheme y MaterialTheme.typography dan los colores y estilos sin valores mágicos."
        ),
        codigo = """
            @OptIn(ExperimentalMaterial3Api::class)
            @Composable
            fun HomeScreenCompacta() {
                Scaffold(
                    topBar = { TopAppBar(title = { Text(text = "Mi App Kotlin") }) }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier.padding(innerPadding).fillMaxSize().padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Text(
                            text = "¡Bienvenido!",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Button(onClick = { /* acción futura */ }) { Text(text = "Presióname") }
                        Image(
                            painter = painterResource(id = R.drawable.logo_ejemplo),
                            contentDescription = "Logo App",
                            modifier = Modifier.fillMaxWidth().height(150.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }
        """
    ),
    BloqueExplicacion(
        titulo = "4. El archivo principal / dispatcher (Guía 9, pág. 3)",
        parrafos = listOf(
            "Paso 4: crea el archivo principal que llama e identifica la pantalla a mostrar según el tamaño del dispositivo donde se ejecuta la app.",
            "La guía lo muestra como HomeScreen2: toma el WindowSizeClass con obtenerWindowSizeClass() y reparte con un when sobre widthSizeClass. El nombre HomeScreen2 es el de la propia guía."
        ),
        codigo = """
            @Composable
            fun HomeScreen2() {
                val windowSizeClass = obtenerWindowSizeClass()
                when (windowSizeClass.widthSizeClass) {
                    WindowWidthSizeClass.Compact -> HomeScreenCompacta()
                    WindowWidthSizeClass.Medium -> HomeScreenMediana()
                    WindowWidthSizeClass.Expanded -> HomeScreenExpandida()
                }
            }
        """
    ),
    BloqueExplicacion(
        titulo = "5. Previews por tamaño y emuladores (Guía 9, pág. 4-5)",
        parrafos = listOf(
            "Paso 5: agrega un @Preview en cada vista con el tamaño que le corresponde, para visualizar cómo quedaría cada una sin ejecutar la app.",
            "Paso 6: crea dispositivos de diferentes tamaños en el Device Manager y ejecuta la app en cada uno para ver las diferencias (o gira/redimensiona la ventana en esta demo)."
        ),
        codigo = """
            @Preview(name = "Compact", widthDp = 360, heightDp = 800)
            @Composable fun PreviewCompacta() { HomeScreenCompacta() }

            @Preview(name = "Medium", widthDp = 800, heightDp = 800)
            @Composable fun PreviewMediana() { HomeScreenMediana() }

            @Preview(name = "Expanded", widthDp = 1200, heightDp = 800)
            @Composable fun PreviewExpandida() { HomeScreenExpandida() }
        """
    ),
    BloqueExplicacion(
        titulo = "6. Mejora recomendada: 3 copias vs 1 adaptativo",
        parrafos = listOf(
            "La Guía 9 propone un archivo por tamaño: Scaffold, TopAppBar, Text, Button e Image se repiten en tres pantallas casi idénticas; solo cambia la disposición, y cualquier cambio de contenido hay que hacerlo tres veces.",
            "La versión mejorada concentra la decisión en un solo composable (HomeScreenAdaptativa) que reutiliza piezas pequeñas —BloqueBienvenida, LogoApp y PanelExtra— y las combina según el ancho: mismo resultado visual, pero con un solo punto de mantenimiento. No siempre necesitas 3 copias; adapta composiciones pequeñas."
        ),
        codigo = """
            // Versión de la guía: 3 pantallas casi idénticas + dispatcher
            WindowWidthSizeClass.Compact -> HomeScreenCompacta()
            WindowWidthSizeClass.Medium -> HomeScreenMediana()
            WindowWidthSizeClass.Expanded -> HomeScreenExpandida()

            // Versión mejorada: 1 composable adaptativo que reutiliza piezas
            @Composable
            fun HomeScreenAdaptativa() {
                val ancho = obtenerWindowSizeClass().widthSizeClass
                when (ancho) {
                    WindowWidthSizeClass.Compact -> Column(... BloqueBienvenida() + LogoApp())
                    WindowWidthSizeClass.Medium -> Row(... LogoApp() + BloqueBienvenida())
                    else -> Row(... BloqueBienvenida() + LogoApp() + PanelExtra())
                }
            }
        """
    )
)

@Composable
fun AdaptabilidadExplicacionScreen(onVolver: () -> Unit) {
    PantallaExplicacion(
        titulo = "Guía 9 · ¿Cómo se hizo?",
        bloques = BloquesAdaptabilidad,
        onVolver = onVolver
    )
}
