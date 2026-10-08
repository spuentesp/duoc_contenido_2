package com.example.ejemploguias

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ejemploguias.ejemplos.adaptabilidad.AdaptabilidadDemoScreen
import com.example.ejemploguias.ejemplos.adaptabilidad.AdaptabilidadExplicacionScreen
import com.example.ejemploguias.ejemplos.camara.CamaraDemoScreen
import com.example.ejemploguias.ejemplos.camara.CamaraExplicacionScreen
import com.example.ejemploguias.ejemplos.estructura.EstructuraDemoScreen
import com.example.ejemploguias.ejemplos.estructura.EstructuraExplicacionScreen
import com.example.ejemploguias.ejemplos.formularios.FormulariosDemoScreen
import com.example.ejemploguias.ejemplos.formularios.FormulariosExplicacionScreen
import com.example.ejemploguias.ejemplos.navegacion.NavegacionDemoScreen
import com.example.ejemploguias.ejemplos.navegacion.NavegacionExplicacionScreen
import com.example.ejemploguias.ejemplos.pantallabase.PantallaBaseDemoScreen
import com.example.ejemploguias.ejemplos.pantallabase.PantallaBaseExplicacionScreen
import com.example.ejemploguias.navigation.Screen
import com.example.ejemploguias.papers.PapersDemoScreen
import com.example.ejemploguias.papers.PapersExplicacionScreen
import com.example.ejemploguias.ui.screens.MenuScreen
import com.example.ejemploguias.ui.theme.EjemploGuiasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EjemploGuiasTheme {
                AppCatalogo()
            }
        }
    }
}

/**
 * NavHost global del catálogo (patrón de la Guía 10: rutas tipo-seguras con sealed class).
 * Cada isla de `ejemplos/` y `papers/` exporta su par Demo/Explicación.
 */
@Composable
fun AppCatalogo(navController: NavHostController = rememberNavController()) {
    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Menu.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Menu.route) {
                MenuScreen(navController)
            }

            composable(Screen.DemoEstructura.route) {
                EstructuraDemoScreen(onAbrirExplicacion = {
                    navController.navigate(Screen.ExplicacionEstructura.route)
                })
            }
            composable(Screen.ExplicacionEstructura.route) {
                EstructuraExplicacionScreen(onVolver = { navController.popBackStack() })
            }

            composable(Screen.DemoPantallaBase.route) {
                PantallaBaseDemoScreen(onAbrirExplicacion = {
                    navController.navigate(Screen.ExplicacionPantallaBase.route)
                })
            }
            composable(Screen.ExplicacionPantallaBase.route) {
                PantallaBaseExplicacionScreen(onVolver = { navController.popBackStack() })
            }

            composable(Screen.DemoAdaptabilidad.route) {
                AdaptabilidadDemoScreen(onAbrirExplicacion = {
                    navController.navigate(Screen.ExplicacionAdaptabilidad.route)
                })
            }
            composable(Screen.ExplicacionAdaptabilidad.route) {
                AdaptabilidadExplicacionScreen(onVolver = { navController.popBackStack() })
            }

            composable(Screen.DemoNavegacion.route) {
                NavegacionDemoScreen(onAbrirExplicacion = {
                    navController.navigate(Screen.ExplicacionNavegacion.route)
                })
            }
            composable(Screen.ExplicacionNavegacion.route) {
                NavegacionExplicacionScreen(onVolver = { navController.popBackStack() })
            }

            composable(Screen.DemoFormularios.route) {
                FormulariosDemoScreen(onAbrirExplicacion = {
                    navController.navigate(Screen.ExplicacionFormularios.route)
                })
            }
            composable(Screen.ExplicacionFormularios.route) {
                FormulariosExplicacionScreen(onVolver = { navController.popBackStack() })
            }

            composable(Screen.DemoCamara.route) {
                CamaraDemoScreen(onAbrirExplicacion = {
                    navController.navigate(Screen.ExplicacionCamara.route)
                })
            }
            composable(Screen.ExplicacionCamara.route) {
                CamaraExplicacionScreen(onVolver = { navController.popBackStack() })
            }

            composable(Screen.DemoPapers.route) {
                PapersDemoScreen(onAbrirExplicacion = {
                    navController.navigate(Screen.ExplicacionPapers.route)
                })
            }
            composable(Screen.ExplicacionPapers.route) {
                PapersExplicacionScreen(onVolver = { navController.popBackStack() })
            }
        }
    }
}
