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
import kotlinx.coroutines.flow.collectLatest

/**
 * Versión mejorada del contenedor (estilo Guía 11: NavHost separado en su propio archivo).
 * Mismo grafo y mismo comportamiento visible que [AppNavigation], pero con
 * `MainViewModelMejorado`, cuyo scope se cancela solo con el ciclo de vida.
 */
@Composable
fun AppNavigationMejorada(
    viewModel: MainViewModelMejorado = viewModel()
) {
    val navController: NavHostController = rememberNavController()

    LaunchedEffect(key1 = Unit) {
        viewModel.navigationEvents.collectLatest { event ->
            when (event) {
                is NavigationEvent.NavigateTo -> {
                    navController.navigate(event.route.route) {
                        event.popUpToRoute?.let {
                            popUpTo(it.route) { inclusive = event.inclusive }
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
                HomeScreenMejorado(navController = navController, viewModel = viewModel)
            }
            composable(Screen.Profile.route) {
                ProfileScreenMejorado(navController = navController, viewModel = viewModel)
            }
            composable(Screen.Settings.route) {
                SettingsScreenMejorado(navController = navController, viewModel = viewModel)
            }
        }
    }
}
