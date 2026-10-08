package com.example.ejemploguias.papers.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ejemploguias.papers.repository.ResultadoApi
import com.example.ejemploguias.papers.ui.componentes.TarjetaPaper
import com.example.ejemploguias.papers.viewmodel.BuscarPapersViewModel

/**
 * Pestaña Buscar: consulta SIEMPRE a la API (OpenAlex) vía el repository.
 * Pinta los 3 estados de ResultadoApi: Cargando, Error y Exito.
 */
@Composable
fun BuscarPapersScreen(
    viewModel: BuscarPapersViewModel,
    modifier: Modifier = Modifier
) {
    val consulta by viewModel.consulta.collectAsStateWithLifecycle()
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val idsGuardados by viewModel.idsGuardados.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = consulta,
            onValueChange = viewModel::cambiarConsulta,
            label = { Text("Tema a buscar (p. ej. kotlin)") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = viewModel::buscar,
            enabled = consulta.isNotBlank() && estado !is ResultadoApi.Cargando,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Buscar")
        }

        when (val actual = estado) {
            is ResultadoApi.Cargando -> Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            is ResultadoApi.Error -> Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = actual.mensaje,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
                Button(onClick = viewModel::buscar) { Text("Reintentar") }
            }

            is ResultadoApi.Exito -> when {
                actual.datos.isEmpty() && consulta.isBlank() -> Text(
                    text = "Escribe un tema y toca Buscar. Los resultados vienen de la API " +
                        "pública OpenAlex; lo que guardes queda en SQLite.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                actual.datos.isEmpty() -> Text(
                    text = "No encontramos papers para «$consulta».",
                    style = MaterialTheme.typography.bodyMedium
                )

                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(actual.datos, key = { it.id }) { paper ->
                        val yaGuardado = paper.id in idsGuardados
                        TarjetaPaper(
                            titulo = paper.titulo,
                            autores = paper.autores,
                            anio = paper.anio,
                            doi = paper.doi,
                            url = paper.url,
                            acciones = {
                                if (yaGuardado) {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = "Guardado",
                                        color = MaterialTheme.colorScheme.primary,
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                } else {
                                    Button(onClick = { viewModel.guardar(paper) }) {
                                        Text("Guardar")
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
