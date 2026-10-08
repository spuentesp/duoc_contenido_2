package com.example.ejemploguias.ejemplos.pantallabase

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
fun PantallaBaseDemoScreen(onAbrirExplicacion: () -> Unit) {
    var version by rememberSaveable { mutableStateOf(VersionDemo.GUIA) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Guía 8 · Pantalla base") },
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                when (version) {
                    VersionDemo.GUIA -> HomeScreen()
                    VersionDemo.MEJORADA -> HomeScreenMejorada()
                }
            }
        }
    }
}

private val BloquesPantallaBase = listOf(
    BloqueExplicacion(
        titulo = "1. Crear ui/HomeScreen.kt (Guía 8, pág. 2)",
        parrafos = listOf(
            "Paso 1: dentro del paquete ui/ crea un archivo Kotlin llamado HomeScreen.kt (Kotlin File).",
            "Paso 2: implementa el código de la guía con comentarios explicativos; la pantalla base usa Scaffold, TopAppBar, Column, Text, Button e Image.",
            "Paso 3: investiga y ajusta el proyecto para que el MainActivity muestre estos elementos como si fuesen propios."
        )
    ),
    BloqueExplicacion(
        titulo = "2. Estructura de la pantalla (Guía 8, pág. 2)",
        parrafos = listOf(
            "Scaffold aporta la estructura: el topBar dibuja la TopAppBar y el contenido recibe innerPadding para no quedar debajo de la barra.",
            "Column organiza los elementos en vertical con Arrangement.spacedBy(20.dp) para lograr el espaciado uniforme que pide la guía.",
            "Image carga el logo desde /res/drawable con painterResource y ContentScale.Fit para que la imagen no se deforme.",
            "Nota: la guía usa R.drawable.logo; en esta app el recurso se llama logo_ejemplo (mismo papel, nombre propio del proyecto)."
        ),
        codigo = """
            @OptIn(ExperimentalMaterial3Api::class)
            @Composable
            fun HomeScreen() {
                Scaffold(
                    topBar = { TopAppBar(title = { Text("Mi App Kotlin") }) }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier.padding(innerPadding).fillMaxSize().padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Text(text = "¡Bienvenido!")
                        Button(onClick = { /* acción futura */ }) { Text("Presióname") }
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
        titulo = "3. Personalización y buenas prácticas (Guía 8, pág. 3)",
        parrafos = listOf(
            "Pasos 4-6: agrega espaciado uniforme con verticalArrangement, usa colores desde MaterialTheme cuando sea posible y verifica que los elementos queden correctamente alineados.",
            "Pasos 7-9: comenta en el código qué hace cada sección, agrega nuevos elementos visuales guiándote de la documentación oficial y ejecuta la app en el dispositivo."
        )
    ),
    BloqueExplicacion(
        titulo = "4. Evidencia de trabajo colaborativo (Guía 8, pág. 3)",
        parrafos = listOf(
            "Realiza un commit con el mensaje «Pantalla HomeScreen con estructura Scaffold».",
            "Crea una rama por feature si trabajas de esa forma: feature/home-screen.",
            "En Trello: actualiza la tarjeta de la tarea y adjunta la captura del resultado visual al tablero."
        )
    ),
    BloqueExplicacion(
        titulo = "5. Mejora recomendada: state hoisting",
        parrafos = listOf(
            "La guía deja el onClick vacío (/* acción futura */): el botón no tiene estado que mostrar. En la versión mejorada ese estado «se sube» a un ViewModel (state hoisting) y se expone como StateFlow.",
            "El composable solo observa el flujo con collectAsState: la UI (HomeScreenMejorada) queda sin lógica y la lógica (PantallaBaseViewModel) queda sin UI. Cambia el switch a «Versión mejorada» y pulsa el botón para ver el contador."
        ),
        codigo = """
            // Versión de la guía: el onClick no hace nada
            Button(onClick = { /* acción futura */ }) { Text("Presióname") }

            // Versión mejorada: el estado vive en el ViewModel (state hoisting)
            class PantallaBaseViewModel : ViewModel() {
                private val _contador = MutableStateFlow(0)
                val contador: StateFlow<Int> = _contador.asStateFlow()
                fun incrementar() { _contador.value += 1 }
            }

            // En HomeScreenMejorada
            val contador by viewModel.contador.collectAsState()
            Text(text = "Clics: ${'$'}contador")
            Button(onClick = { viewModel.incrementar() }) { Text("Presióname") }
        """
    )
)

@Composable
fun PantallaBaseExplicacionScreen(onVolver: () -> Unit) {
    PantallaExplicacion(
        titulo = "Guía 8 · ¿Cómo se hizo?",
        bloques = BloquesPantallaBase,
        onVolver = onVolver
    )
}
