package com.example.ejemploguias.ui.utils

import android.app.Activity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * Guía 9 · Adaptabilidad del diseño.
 *
 * Devuelve el WindowSizeClass actual para decidir entre layout Compact/Medium/Expanded.
 *
 * Nota (versión de la guía): la Guía 9 escribe
 * `calculateWindowSizeClass(LocalActivity.current as Activity)` usando `LocalActivity`.
 * Aquí usamos `LocalContext.current as Activity`, que es equivalente y compatible con
 * más versiones de androidx.activity.
 */
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun obtenerWindowSizeClass(): WindowSizeClass {
    return calculateWindowSizeClass(LocalContext.current as Activity)
}
