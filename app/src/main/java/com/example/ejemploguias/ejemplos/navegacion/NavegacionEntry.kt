package com.example.ejemploguias.ejemplos.navegacion

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ejemploguias.ui.components.PantallaExplicacion
import com.example.ejemploguias.ui.components.SelectorVersion
import com.example.ejemploguias.ui.components.VersionDemo

/**
 * Isla didáctica de Navegación (Guía 10).
 * El demo vive en un NavHost anidado propio (`AppNavigation` / `AppNavigationMejorada`);
 * alternar el selector recrea ese grafo interno completo con `key(version)`.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavegacionDemoScreen(onAbrirExplicacion: () -> Unit) {
    var version by rememberSaveable { mutableStateOf(VersionDemo.GUIA) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Demo: Navegación · Guía 10") },
                actions = {
                    TextButton(onClick = onAbrirExplicacion) {
                        Text("¿Cómo se hizo?")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            SelectorVersion(
                version = version,
                onCambiar = { version = it },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            // key(version) reinicia el NavHost anidado y su estado al cambiar de versión
            key(version) {
                when (version) {
                    VersionDemo.GUIA -> AppNavigation()
                    VersionDemo.MEJORADA -> AppNavigationMejorada()
                }
            }
        }
    }
}

@Composable
fun NavegacionExplicacionScreen(onVolver: () -> Unit) {
    PantallaExplicacion(
        titulo = "Navegación · Guía 10",
        bloques = bloquesExplicacionNavegacion,
        onVolver = onVolver
    )
}
