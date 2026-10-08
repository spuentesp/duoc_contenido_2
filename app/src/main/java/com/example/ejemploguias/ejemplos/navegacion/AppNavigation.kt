package com.example.ejemploguias.ejemplos.navegacion

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigate
import androidx.navigation.popUpTo
import kotlinx.coroutines.flow.collectLatest

/**
 * Contenedor "MainActivity" de la guía (Guía 10, pág. 10-11):
 * recolecta los eventos del ViewModel y monta el NavHost con las 3 rutas.
 */
@Composable
fun AppNavigation(
    viewModel: MainViewModel = viewModel()
) {
    val navController: NavHostController = rememberNavController()

    LaunchedEffect(key1 = Unit) {
        viewModel.navigationEvents.collectLatest { event ->
            when (event) {
                is NavigationEvent.NavigateTo -> {
                    navController.navigate(event.route.route) {
                        event.popUpToRoute?.let {
                            popUpTo(it.route, event.inclusive)
                        }
                        launchSingleTop = event.singleTop
                        restoreState = true
                    }
                }
                is NavigationEvent.PopBackStack -> {
                    navController.popBackStack()
                }
                is NavigationEvent.NavigateUp -> {
                    navController.navigateUp()
                }
            }
        }
    }

    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(navController = navController, viewModel = viewModel)
            }
            composable(Screen.Profile.route) {
                ProfileScreen(navController = navController, viewModel = viewModel)
            }
            composable(Screen.Settings.route) {
                SettingsScreen(navController = navController, viewModel = viewModel)
            }
        }
    }
}
