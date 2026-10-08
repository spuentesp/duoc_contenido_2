package com.example.ejemploguias.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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

/**
 * Bloque de la explicación "¿Cómo se hizo?" de una demo.
 * @param parrafos texto explicativo (pasos, referencias a la guía).
 * @param codigo snippet opcional mostrado en bloque monoespaciado.
 */
data class BloqueExplicacion(
    val titulo: String,
    val parrafos: List<String>,
    val codigo: String? = null
)

/**
 * Pantalla compartida de explicación paso a paso de una demo.
 * Cada isla de `ejemplos/` construye sus bloques citando su guía (y su mejora).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaExplicacion(
    titulo: String,
    bloques: List<BloqueExplicacion>,
    onVolver: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(titulo) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(bloques) { bloque ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(bloque.titulo, style = MaterialTheme.typography.titleLarge)
                        bloque.parrafos.forEach { parrafo ->
                            Text(parrafo, style = MaterialTheme.typography.bodyLarge)
                        }
                        bloque.codigo?.let { codigo ->
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Text(
                                    text = codigo.trimIndent(),
                                    modifier = Modifier.padding(12.dp),
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = MaterialTheme.typography.bodyMedium.fontSize
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
