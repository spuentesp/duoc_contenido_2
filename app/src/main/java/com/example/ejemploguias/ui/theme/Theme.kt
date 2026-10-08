package com.example.ejemploguias.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val ColoresClaro = lightColorScheme(
    primary = AzulPrimario,
    onPrimary = AzulSobrePrimario,
    primaryContainer = AzulContenedor,
    onPrimaryContainer = AzulSobreContenedor,
    secondary = VerdeSecundario,
    onSecondary = VerdeSobreSecundario,
    tertiary = NaranjaTerciario,
    onTertiary = NaranjaSobreTerciario,
    error = RojoError
)

private val ColoresOscuro = darkColorScheme(
    primary = AzulContenedor,
    onPrimary = AzulSobreContenedor,
    secondary = VerdeSecundario,
    tertiary = NaranjaTerciario
)

/**
 * Tema Material 3 de la app de ejemplo.
 * Cualquier pantalla de las guías puede usar MaterialTheme.colorScheme / typography.
 */
@Composable
fun EjemploGuiasTheme(
    temaOscuro: Boolean = isSystemInDarkTheme(),
    contenido: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (temaOscuro) ColoresOscuro else ColoresClaro,
        typography = TipografiaApp,
        content = contenido
    )
}
