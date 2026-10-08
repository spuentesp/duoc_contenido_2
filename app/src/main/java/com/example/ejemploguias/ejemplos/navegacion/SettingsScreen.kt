package com.example.ejemploguias.ejemplos.navegacion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel

/** Configuración: botones que navegan vía viewModel.navigateTo (Guía 10, pág. 9). */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pantalla Settings") },
                navigationIcon = {
                    Icon(Icons.Default.Settings, contentDescription = null)
                }
            )
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
                text = "Configuración",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "Desde aquí puedes volver a Home o a Perfil. " +
                    "La ruta de ejemplo con argumento de la guía sería " +
                    "\"${Screen.Detail(itemId = "42").buildRoute()}\".",
                style = MaterialTheme.typography.bodyMedium
            )
            Button(
                onClick = { viewModel.navigateTo(Screen.Home) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ir a Home")
            }
            Button(
                onClick = { viewModel.navigateTo(Screen.Profile) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ir a Perfil")
            }
            Button(
                onClick = { viewModel.navigateBack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver atrás")
            }
        }
    }
}
