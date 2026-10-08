package com.example.ejemploguias.ejemplos.navegacion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel

/** Perfil con bottom bar (espejo de la Guía 10, pág. 8). */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
) {
    val items = listOf(Screen.Home, Screen.Profile)
    var selectedItem by remember { mutableStateOf(1) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Pantalla Profile") })
        },
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, screen ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = if (index == 0) Icons.Default.Home else Icons.Default.Person,
                                contentDescription = null
                            )
                        },
                        label = { Text(if (index == 0) "Home" else "Perfil") },
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            viewModel.navigateTo(screen)
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "¡Estás en tu Perfil!",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "La NavigationBar inferior muestra en qué pantalla estás. " +
                    "Al tocar un ítem se emite un NavigationEvent hacia esa ruta.",
                style = MaterialTheme.typography.bodyMedium
            )
            Button(onClick = { viewModel.navigateTo(Screen.Home) }) {
                Text("Ir a Home")
            }
            Button(onClick = { viewModel.navigateTo(Screen.Settings) }) {
                Text("Ir a Configuración")
            }
            Button(onClick = { viewModel.navigateBack() }) {
                Text("Volver atrás")
            }
        }
    }
}
