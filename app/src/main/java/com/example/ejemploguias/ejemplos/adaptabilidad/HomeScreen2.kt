package com.example.ejemploguias.ejemplos.adaptabilidad

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import com.example.ejemploguias.ui.utils.obtenerWindowSizeClass

/** Archivo principal de la Guía 9: identifica qué pantalla mostrar según el ancho. */
@Composable
fun HomeScreen2() {
    val windowSizeClass = obtenerWindowSizeClass()
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> HomeScreenCompacta()
        WindowWidthSizeClass.Medium -> HomeScreenMediana()
        WindowWidthSizeClass.Expanded -> HomeScreenExpandida()
    }
}
