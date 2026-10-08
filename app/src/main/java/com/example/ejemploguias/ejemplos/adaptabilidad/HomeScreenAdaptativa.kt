package com.example.ejemploguias.ejemplos.adaptabilidad

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ejemploguias.R
import com.example.ejemploguias.ui.utils.obtenerWindowSizeClass

/**
 * Versión mejorada: un solo composable decide internamente cómo combinar
 * piezas pequeñas reutilizables según el ancho disponible.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenAdaptativa() {
    val ancho = obtenerWindowSizeClass().widthSizeClass
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Mi App Kotlin") })
        }
    ) { innerPadding ->
        when (ancho) {
            WindowWidthSizeClass.Compact -> Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                BloqueBienvenida()
                LogoApp()
            }
            WindowWidthSizeClass.Medium -> Row(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LogoApp(modifier = Modifier.weight(1f))
                BloqueBienvenida(
                    modifier = Modifier.weight(1f),
                    etiqueta = "Tamaño mediano · Row imagen + texto"
                )
            }
            else -> Row(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BloqueBienvenida(
                    modifier = Modifier.weight(1f),
                    etiqueta = "Tamaño expandido · Row con 3 columnas"
                )
                LogoApp(modifier = Modifier.weight(1f), alto = 180.dp)
                PanelExtra(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun BloqueBienvenida(modifier: Modifier = Modifier, etiqueta: String? = null) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(20.dp)) {
        etiqueta?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.labelLarge
            )
        }
        Text(
            text = "¡Bienvenido!",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge
        )
        Button(onClick = { /* acción futura */ }) {
            Text(text = "Presióname")
        }
    }
}

@Composable
private fun LogoApp(modifier: Modifier = Modifier, alto: Dp = 150.dp) {
    Image(
        painter = painterResource(id = R.drawable.logo_ejemplo),
        contentDescription = "Logo App",
        modifier = modifier
            .fillMaxWidth()
            .height(alto),
        contentScale = ContentScale.Fit
    )
}

@Composable
private fun PanelExtra(modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Panel extra",
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "El ancho expandido permite mostrar una tercera columna con información adicional sin saturar el contenido principal.",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
