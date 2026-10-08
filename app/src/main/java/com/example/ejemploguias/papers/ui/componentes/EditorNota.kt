package com.example.ejemploguias.papers.ui.componentes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Editor de la nota local (se escribe en SQLite, no en la API).
 * El estado del texto vive en el que lo usa (FavoritosScreen);
 * aquí solo se pintan el campo y los botones.
 */
@Composable
fun EditorNota(
    nota: String,
    onCambio: (String) -> Unit,
    onGuardar: () -> Unit,
    onCancelar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = nota,
            onValueChange = onCambio,
            label = { Text("Nota personal") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = onGuardar) { Text("Guardar nota") }
            TextButton(onClick = onCancelar) { Text("Cancelar") }
        }
    }
}
