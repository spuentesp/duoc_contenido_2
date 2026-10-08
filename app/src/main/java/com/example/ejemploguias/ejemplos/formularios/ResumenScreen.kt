package com.example.ejemploguias.ejemplos.formularios

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Espejo Guía 11, Parte 4 (pág. 8): observa el MISMO UsuarioViewModel
 * con collectAsState(); no se pasan argumentos por ruta.
 */
@Composable
fun ResumenScreen(
    viewModel: UsuarioViewModel,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.estado.collectAsState()
    ContenidoResumen(
        nombre = estado.nombre,
        correo = estado.correo,
        clave = estado.clave,
        direccion = estado.direccion,
        aceptaTerminos = estado.aceptaTerminos,
        onVolver = onVolver,
        modifier = modifier
    )
}

/** Resumen de la versión mejorada: mismo contenido, otro ViewModel. */
@Composable
fun ResumenScreenMejorado(
    viewModel: UsuarioViewModelMejorado,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.estado.collectAsState()
    ContenidoResumen(
        nombre = estado.nombre,
        correo = estado.correo,
        clave = estado.clave,
        direccion = estado.direccion,
        aceptaTerminos = estado.aceptaTerminos,
        onVolver = onVolver,
        modifier = modifier
    )
}

@Composable
private fun ContenidoResumen(
    nombre: String,
    correo: String,
    clave: String,
    direccion: String,
    aceptaTerminos: Boolean,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Nombre: $nombre",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Correo: $correo",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Contraseña: ${"*".repeat(n = clave.length)}",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Dirección: $direccion",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Términos: ${if (aceptaTerminos) "aceptados" else "no aceptados"}",
            style = MaterialTheme.typography.headlineMedium
        )
        Button(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver")
        }
    }
}
