package com.example.ejemploguias.papers.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ejemploguias.papers.ui.componentes.EditorNota
import com.example.ejemploguias.papers.ui.componentes.TarjetaPaper
import com.example.ejemploguias.papers.viewmodel.FavoritosViewModel

/**
 * Pestaña Favoritos: todo se lee de Room (SQLite), por lo que la lista funciona
 * sin conexión y se redibuja sola: el Flow del DAO notifica cada cambio.
 */
@Composable
fun FavoritosScreen(
    viewModel: FavoritosViewModel,
    modifier: Modifier = Modifier
) {
    val favoritos by viewModel.favoritos.collectAsStateWithLifecycle()
    val uriHandler = LocalUriHandler.current

    if (favoritos.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Aún no guardas papers",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Pasa a la pestaña Buscar y toca «Guardar». " +
                        "Esta lista se redibuja sola gracias al Flow de Room.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(favoritos, key = { it.id }) { favorito ->
                var editandoNota by rememberSaveable(favorito.id) { mutableStateOf(false) }
                var notaLocal by remember(favorito.nota) { mutableStateOf(favorito.nota) }
                val enlace = favorito.url ?: favorito.doi?.let { "https://doi.org/$it" }

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    TarjetaPaper(
                        titulo = favorito.titulo,
                        autores = favorito.autores,
                        anio = favorito.anio,
                        doi = favorito.doi,
                        url = favorito.url,
                        nota = favorito.nota,
                        acciones = {
                            TextButton(onClick = {
                                notaLocal = favorito.nota
                                editandoNota = !editandoNota
                            }) {
                                Text(if (editandoNota) "Cerrar" else "Editar nota")
                            }
                            TextButton(
                                onClick = { enlace?.let { uriHandler.openUri(it) } },
                                enabled = enlace != null
                            ) {
                                Text("Abrir DOI")
                            }
                            TextButton(onClick = { viewModel.eliminar(favorito) }) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = null,
                                    modifier = Modifier.padding(end = 4.dp)
                                )
                                Text("Eliminar")
                            }
                        }
                    )

                    if (editandoNota) {
                        EditorNota(
                            nota = notaLocal,
                            onCambio = { notaLocal = it },
                            onGuardar = {
                                viewModel.actualizarNota(favorito.id, notaLocal)
                                editandoNota = false
                            },
                            onCancelar = { editandoNota = false },
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}
