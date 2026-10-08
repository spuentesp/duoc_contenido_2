package com.example.ejemploguias.ejemplos.navegacion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch

/**
 * Variantes "mejora en vivo" de las tres pantallas: mismo comportamiento visible
 * que la versión guía, pero inyectando `MainViewModelMejorado` (viewModelScope).
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenMejorado(
    navController: NavController,
    viewModel: MainViewModelMejorado = viewModel()
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                NavigationDrawerItem(
                    label = { Text("Ir a Perfil") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        viewModel.navigateTo(Screen.Profile)
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Ir a Configuración") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        viewModel.navigateTo(Screen.Settings)
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Pantalla Home") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                        }
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
                    text = "¡Bienvenido a la Página de Inicio (MVVM)!",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "Versión mejorada: misma UI, pero los eventos salen de viewModelScope. " +
                        "Abre el menú con el ícono ☰ o navega a Configuración.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Button(onClick = { viewModel.navigateTo(Screen.Settings) }) {
                    Text("Ir a Configuración")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenMejorado(
    navController: NavController,
    viewModel: MainViewModelMejorado = viewModel()
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
                text = "Versión mejorada: la NavigationBar inferior emite eventos desde viewModelScope.",
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenMejorado(
    navController: NavController,
    viewModel: MainViewModelMejorado = viewModel()
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
                text = "Versión mejorada: mismo flujo de botones, ViewModel con viewModelScope.",
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
