package com.example.ejemploguias.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** Versión del código que una demo está ejecutando. */
enum class VersionDemo { GUIA, MEJORADA }

/**
 * Switch "Guía | Mejorado" que cada demo usa para intercambiar en vivo
 * la implementación espejo de la guía y la mejora recomendada.
 */
@Composable
fun SelectorVersion(
    version: VersionDemo,
    onCambiar: (VersionDemo) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = version == VersionDemo.GUIA,
            onClick = { onCambiar(VersionDemo.GUIA) },
            label = { Text("Versión de la guía") }
        )
        FilterChip(
            selected = version == VersionDemo.MEJORADA,
            onClick = { onCambiar(VersionDemo.MEJORADA) },
            label = { Text("Versión mejorada") }
        )
    }
}
