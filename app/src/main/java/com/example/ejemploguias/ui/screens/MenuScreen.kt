package com.example.ejemploguias.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ejemploguias.navigation.Screen

/** Entrada del menú principal del catálogo. */
data class EntradaMenu(
    val titulo: String,
    val guia: String,
    val descripcion: String,
    val destino: Screen
)

val entradasMenu = listOf(
    EntradaMenu(
        titulo = "Estructura del proyecto",
        guia = "Guía 7",
        descripcion = "Carpetas MVVM, Git colaborativo y Trello",
        destino = Screen.DemoEstructura
    ),
    EntradaMenu(
        titulo = "Pantalla base",
        guia = "Guía 8",
        descripcion = "Scaffold, TopAppBar, Text, Button, Image y Preview",
        destino = Screen.DemoPantallaBase
    ),
    EntradaMenu(
        titulo = "Adaptabilidad del diseño",
        guia = "Guía 9",
        descripcion = "Window Size Classes: compacta, mediana y expandida",
        destino = Screen.DemoAdaptabilidad
    ),
    EntradaMenu(
        titulo = "Navegación y menús",
        guia = "Guía 10",
        descripcion = "Rutas tipo-seguras, drawer, bottom bar y NavHost",
        destino = Screen.DemoNavegacion
    ),
    EntradaMenu(
        titulo = "Formularios y validación",
        guia = "Guía 11",
        descripcion = "UiState con StateFlow, errores por campo y ViewModel compartido",
        destino = Screen.DemoFormularios
    ),
    EntradaMenu(
        titulo = "Cámara y galería",
        guia = "Guía 13",
        descripcion = "TakePicture / GetContent con FileProvider",
        destino = Screen.DemoCamara
    ),
    EntradaMenu(
        titulo = "Papers: API + SQLite + Repository",
        guia = "Extensión",
        descripcion = "OpenAlex, Retrofit, Room y el repository que une ambos mundos",
        destino = Screen.DemoPapers
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(navController: NavController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Ejemplos Guías · Desarrollo Móvil") }) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(entradasMenu) { entrada ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate(entrada.destino.route) }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(entrada.guia, style = MaterialTheme.typography.labelLarge)
                        Text(entrada.titulo, style = MaterialTheme.typography.titleLarge)
                        Text(entrada.descripcion, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}
