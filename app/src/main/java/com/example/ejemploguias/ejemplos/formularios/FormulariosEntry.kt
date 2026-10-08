package com.example.ejemploguias.ejemplos.formularios

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ejemploguias.ui.components.BloqueExplicacion
import com.example.ejemploguias.ui.components.PantallaExplicacion
import com.example.ejemploguias.ui.components.SelectorVersion
import com.example.ejemploguias.ui.components.VersionDemo

private const val RUTA_REGISTRO = "registro"
private const val RUTA_RESUMEN = "resumen"

/** Demo en vivo de la Guía 11 con el switch "Versión de la guía | Versión mejorada". */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormulariosDemoScreen(onAbrirExplicacion: () -> Unit) {
    var version by rememberSaveable { mutableStateOf(VersionDemo.GUIA) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Guía 11 · Formularios") },
                actions = {
                    IconButton(onClick = onAbrirExplicacion) {
                        Icon(Icons.Default.Info, contentDescription = "¿Cómo se hizo?")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            SelectorVersion(
                version = version,
                onCambiar = { version = it },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            HorizontalDivider()
            AppNavigation(
                version = version,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/**
 * NavHost anidado propio de la demo (Guía 11, Parte 5, pág. 9-10):
 * rutas "registro" y "resumen" compartiendo un único UsuarioViewModel,
 * creado una sola vez fuera del NavHost.
 */
@Composable
private fun AppNavigation(
    version: VersionDemo,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val usuarioViewModel: UsuarioViewModel = viewModel()
    val usuarioViewModelMejorado: UsuarioViewModelMejorado = viewModel()

    NavHost(
        navController = navController,
        startDestination = RUTA_REGISTRO,
        modifier = modifier
    ) {
        composable(RUTA_REGISTRO) {
            when (version) {
                VersionDemo.GUIA -> RegistroScreen(
                    viewModel = usuarioViewModel,
                    onRegistroExitoso = { navController.navigate(RUTA_RESUMEN) }
                )
                VersionDemo.MEJORADA -> RegistroScreenMejorado(
                    viewModel = usuarioViewModelMejorado,
                    onRegistroExitoso = { navController.navigate(RUTA_RESUMEN) }
                )
            }
        }
        composable(RUTA_RESUMEN) {
            when (version) {
                VersionDemo.GUIA -> ResumenScreen(
                    viewModel = usuarioViewModel,
                    onVolver = { navController.popBackStack() }
                )
                VersionDemo.MEJORADA -> ResumenScreenMejorado(
                    viewModel = usuarioViewModelMejorado,
                    onVolver = { navController.popBackStack() }
                )
            }
        }
    }
}

@Composable
fun FormulariosExplicacionScreen(onVolver: () -> Unit) {
    PantallaExplicacion(
        titulo = "Guía 11 · ¿Cómo se hizo?",
        bloques = bloquesFormularios,
        onVolver = onVolver
    )
}

private val bloquesFormularios = listOf(
    BloqueExplicacion(
        titulo = "Parte 1 · Modelado del estado (Guía 11, pág. 2-3)",
        parrafos = listOf(
            "La guía parte con data class puras: UsuarioUiState concentra los datos del formulario (nombre, correo, clave, dirección, aceptaTerminos) y UsuarioErrores guarda el mensaje de cada campo; null significa \"sin error\".",
            "No hay nada de Android en el modelo, por eso se puede probar con JUnit en la JVM sin emulador."
        ),
        codigo = """
            data class UsuarioUiState(
                val nombre: String = "",
                val correo: String = "",
                val clave: String = "",
                val direccion: String = "",
                val aceptaTerminos: Boolean = false,
                val errores: UsuarioErrores = UsuarioErrores()
            )

            data class UsuarioErrores(
                val nombre: String? = null,
                val correo: String? = null,
                val clave: String? = null,
                val direccion: String? = null
            )
        """
    ),
    BloqueExplicacion(
        titulo = "Parte 2 · Lógica del ViewModel (Guía 11, pág. 3-4)",
        parrafos = listOf(
            "UsuarioViewModel guarda el estado en MutableStateFlow y lo expone como StateFlow. Cada onXxxChange copia el estado con copy() y limpia el error de ese campo mientras el usuario escribe.",
            "validarFormulario() evalúa los cuatro campos, guarda los errores en el estado y devuelve true/false para decidir si se navega a resumen.",
            "Se prueba en app/src/test/.../UsuarioViewModelTest.kt con JUnit4 y runTest: sin Mockito y sin emulador, solo se instancia el ViewModel y se lee estado.value."
        ),
        codigo = """
            fun validarFormulario(): Boolean {
                val estadoActual = _estado.value
                val errores = UsuarioErrores(
                    nombre = if (estadoActual.nombre.isBlank()) "Campo obligatorio" else null,
                    correo = if (!estadoActual.correo.contains("@")) "Correo inválido" else null,
                    clave = if (estadoActual.clave.length < 6) "Debe tener al menos 6 caracteres" else null,
                    direccion = if (estadoActual.direccion.isBlank()) "Campo obligatorio" else null
                )
                val hayErrores = listOfNotNull(
                    errores.nombre, errores.correo, errores.clave, errores.direccion
                ).isNotEmpty()
                _estado.update { it.copy(errores = errores) }
                return !hayErrores
            }
        """
    ),
    BloqueExplicacion(
        titulo = "Parte 3 · RegistroScreen (Guía 11, pág. 5-7)",
        parrafos = listOf(
            "OutlinedTextField con isError y supportingText muestra el error justo bajo cada campo (pág. 6); la contraseña usa PasswordVisualTransformation y un Checkbox maneja aceptaTerminos (pág. 5).",
            "El botón solo navega si validarFormulario() devuelve true: navController.navigate(\"resumen\") (pág. 7)."
        ),
        codigo = """
            OutlinedTextField(
                value = estado.nombre,
                onValueChange = viewModel::onNombreChange,
                label = { Text("Nombre") },
                isError = estado.errores.nombre != null,
                supportingText = {
                    estado.errores.nombre?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (viewModel.validarFormulario()) {
                        navController.navigate("resumen")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Registrarse") }
        """
    ),
    BloqueExplicacion(
        titulo = "Parte 4 · ResumenScreen (Guía 11, pág. 8)",
        parrafos = listOf(
            "ResumenScreen observa el MISMO UsuarioViewModel con collectAsState(): los datos llegan reactivos, sin argumentos de ruta y sin ViewModel nuevo.",
            "La contraseña se enmascara con \"*\".repeat(n = estado.clave.length) para no mostrarla en claro en la pantalla de resumen."
        ),
        codigo = """
            val estado by viewModel.estado.collectAsState()

            Text("Nombre: ${'$'}{estado.nombre}",
                style = MaterialTheme.typography.headlineMedium)
            Text("Contraseña: ${'$'}{"*".repeat(n = estado.clave.length)}",
                style = MaterialTheme.typography.headlineMedium)
            Text("Términos: ${'$'}{if (estado.aceptaTerminos) "aceptados" else "no aceptados"}",
                style = MaterialTheme.typography.headlineMedium)
        """
    ),
    BloqueExplicacion(
        titulo = "Parte 5 · Navegación con ViewModel compartido (Guía 11, pág. 9-10)",
        parrafos = listOf(
            "AppNavigation.kt local con NavHost y dos rutas: \"registro\" y \"resumen\" (pág. 9); NavController con navigate() y popBackStack() (pág. 10).",
            "El UsuarioViewModel se crea UNA sola vez, fuera del NavHost, por eso ambas rutas comparten el mismo estado. Este NavHost vive dentro de la demo; el NavHost global del catálogo no se toca."
        ),
        codigo = """
            @Composable
            fun AppNavigation() {
                val navController = rememberNavController()
                val usuarioViewModel: UsuarioViewModel = viewModel() // una sola vez

                NavHost(navController, startDestination = "registro") {
                    composable("registro") {
                        RegistroScreen(usuarioViewModel) {
                            navController.navigate("resumen")
                        }
                    }
                    composable("resumen") {
                        ResumenScreen(usuarioViewModel) {
                            navController.popBackStack()
                        }
                    }
                }
            }
        """
    ),
    BloqueExplicacion(
        titulo = "Parte 6 · Evidencia de trabajo colaborativo (Guía 11, pág. 11)",
        parrafos = listOf(
            "La guía cierra con una tarjeta de Trello, un commit con el mensaje \"Formulario y resumen funcional\" en la rama feature/registro-formulario, y una captura de ResumenScreen con los datos ingresados.",
            "En esta demo, el botón \"Volver\" de ResumenScreen usa popBackStack() para regresar a registro con el formulario intacto."
        )
    ),
    BloqueExplicacion(
        titulo = "Mejora demostrada: validación en tiempo real",
        parrafos = listOf(
            "La guía solo valida al enviar; UsuarioViewModelMejorado valida cada campo dentro de su propio onXxxChange usando flags \"tocado\": el error aparece recién cuando el usuario escribe y desaparece en cuanto lo corrige. Al enviar se marcan todos los campos.",
            "Además la contraseña tiene un botón \"Mostrar\"/\"Ocultar\" (TextButton): el icono Visibility no existe en material-icons-core, así que se usa texto. Cambia el switch arriba para ver ambas versiones en vivo."
        ),
        codigo = """
            fun onCorreoChange(valor: String) {
                _estado.update { s ->
                    s.copy(
                        correo = valor,
                        tocado = s.tocado.copy(correo = true),
                        errores = s.errores.copy(correo = validarCorreo(valor))
                    )
                }
            }
        """
    )
)
