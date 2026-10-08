package com.example.ejemploguias.ejemplos.navegacion

import com.example.ejemploguias.ui.components.BloqueExplicacion

/**
 * Bloques "¿Cómo se hizo?" de la demo de Navegación,
 * siguiendo los pasos de la Guía 10 (págs. 3-11).
 */
val bloquesExplicacionNavegacion: List<BloqueExplicacion> = listOf(
    BloqueExplicacion(
        titulo = "Paso 1 · Rutas tipo-seguras (Guía 10, pág. 3)",
        parrafos = listOf(
            "La guía define las rutas de la app con una sealed class Screen: cada pantalla es un miembro que centraliza su string de route, en lugar de repartir strings sueltos por el código.",
            "Los objetos usan data object porque no tienen estado de instancia: `data object` es un singleton seguro de tipos (Kotlin 1.9+).",
            "También se ve el caso de ruta con argumento: Detail recibe un itemId y buildRoute() arma el string final reemplazando {itemId}."
        ),
        codigo = """
            sealed class Screen(val route: String) {
                // `data object` es un singleton seguro de tipos (Kotlin 1.9+)
                data object Home : Screen(route = "home_page")
                data object Profile : Screen(route = "profile_page")
                data object Settings : Screen(route = "settings_page")

                // Ruta con argumento: "detail_page/{itemId}"
                data class Detail(val itemId: String) : Screen(route = "detail_page/{itemId}") {
                    fun buildRoute() = route.replace(oldValue = "{itemId}", newValue = itemId)
                }
            }
        """
    ),
    BloqueExplicacion(
        titulo = "Paso 2 · Eventos de navegación (Guía 10, pág. 4)",
        parrafos = listOf(
            "Las pantallas no llaman a navController directamente: emiten NavigationEvent. El sealed class agrupa las tres acciones del patrón (ir a ruta, volver del stack, navigateUp) más las opciones popUpTo/inclusive/singleTop para controlar el stack."
        ),
        codigo = """
            sealed class NavigationEvent {
                data class NavigateTo(
                    val route: Screen,
                    val popUpToRoute: Screen? = null,
                    val inclusive: Boolean = false,
                    val singleTop: Boolean = false
                ) : NavigationEvent()

                object PopBackStack : NavigationEvent()
                object NavigateUp : NavigationEvent()
            }
        """
    ),
    BloqueExplicacion(
        titulo = "Paso 3 · ViewModel con SharedFlow (Guía 10, pág. 5)",
        parrafos = listOf(
            "MainViewModel expone navigationEvents como SharedFlow (sin replay): la UI pide navegación con navigateTo/navigateBack/navigateUp y el ViewModel emite el evento correspondiente.",
            "Ojo: la guía emite con CoroutineScope(context = Dispatchers.Main).launch tal cual se muestra aquí. Es el patrón literal de la guía; la versión mejorada de esta demo lo reemplaza por viewModelScope (ver bloque final)."
        ),
        codigo = """
            class MainViewModel : ViewModel() {
                private val _navigationEvents = MutableSharedFlow<NavigationEvent>()
                val navigationEvents: SharedFlow<NavigationEvent> = _navigationEvents.asSharedFlow()

                fun navigateTo(screen: Screen) {
                    CoroutineScope(context = Dispatchers.Main).launch {
                        _navigationEvents.emit(NavigationEvent.NavigateTo(route = screen))
                    }
                }

                fun navigateBack() {
                    CoroutineScope(context = Dispatchers.Main).launch {
                        _navigationEvents.emit(NavigationEvent.PopBackStack)
                    }
                }
            }
        """
    ),
    BloqueExplicacion(
        titulo = "Paso 4 · HomeScreen con ModalNavigationDrawer (Guía 10, pág. 6-7)",
        parrafos = listOf(
            "HomeScreen envuelve su Scaffold en un ModalNavigationDrawer con un ModalDrawerSheet. El ícono de menú (Icons.Default.Menu) del TopAppBar abre el drawerState; cada NavigationDrawerItem cierra el drawer y emite la navegación hacia la pantalla elegida."
        ),
        codigo = """
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
                    }
                }
            ) {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Pantalla Home") },
                            navigationIcon = {
                                IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                    Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                                }
                            }
                        )
                    }
                ) { /* Text("¡Bienvenido a la Página de Inicio (MVVM)!") + botón a Settings */ }
            }
        """
    ),
    BloqueExplicacion(
        titulo = "Paso 5 · Bottom bar con NavigationBar (Guía 10, pág. 8)",
        parrafos = listOf(
            "ProfileScreen agrega un bottomBar con NavigationBar y un NavigationBarItem por cada opción de la lista listOf(Screen.Home, Screen.Profile).",
            "selectedItem (mutableStateOf(1), arranca en Perfil) recuerda cuál está activo; al tocar un ítem se actualiza la selección y se emite el evento hacia esa ruta."
        ),
        codigo = """
            val items = listOf(Screen.Home, Screen.Profile)
            var selectedItem by remember { mutableStateOf(1) }

            Scaffold(
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
            ) { /* contenido de Profile */ }
        """
    ),
    BloqueExplicacion(
        titulo = "Paso 6 · SettingsScreen (Guía 10, pág. 9)",
        parrafos = listOf(
            "SettingsScreen es la pantalla más simple: una Column con botones que navegan con viewModel.navigateTo(Screen.Home) y viewModel.navigateTo(Screen.Profile), más un botón de volver atrás con navigateBack()."
        ),
        codigo = """
            Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
                Button(onClick = { viewModel.navigateTo(Screen.Home) }) { Text("Ir a Home") }
                Button(onClick = { viewModel.navigateTo(Screen.Profile) }) { Text("Ir a Perfil") }
                Button(onClick = { viewModel.navigateBack() }) { Text("Volver atrás") }
            }
        """
    ),
    BloqueExplicacion(
        titulo = "Paso 7 · LaunchedEffect + NavHost (Guía 10, pág. 10-11)",
        parrafos = listOf(
            "El 'MainActivity' de la guía (aquí AppNavigation, el contenedor del NavHost anidado) recolecta navigationEvents con collectLatest dentro de un LaunchedEffect y despacha cada NavigationEvent con un when: NavigateTo navega aplicando popUpTo/launchSingleTop/restoreState, PopBackStack hace popBackStack() y NavigateUp hace navigateUp().",
            "Debajo monta el NavHost(startDestination = Screen.Home.route) con las tres rutas y le pasa el mismo ViewModel a cada pantalla. En esta demo ese NavHost es anidado: vive dentro de la isla y no toca la navegación global del catálogo."
        ),
        codigo = """
            LaunchedEffect(key1 = Unit) {
                viewModel.navigationEvents.collectLatest { event ->
                    when (event) {
                        is NavigationEvent.NavigateTo -> {
                            navController.navigate(event.route.route) {
                                event.popUpToRoute?.let { popUpTo(it.route, event.inclusive) }
                                launchSingleTop = event.singleTop
                                restoreState = true
                            }
                        }
                        is NavigationEvent.PopBackStack -> navController.popBackStack()
                        is NavigationEvent.NavigateUp -> navController.navigateUp()
                    }
                }
            }

            NavHost(navController = navController, startDestination = Screen.Home.route) {
                composable(Screen.Home.route) { HomeScreen(navController, viewModel) }
                composable(Screen.Profile.route) { ProfileScreen(navController, viewModel) }
                composable(Screen.Settings.route) { SettingsScreen(navController, viewModel) }
            }
        """
    ),
    BloqueExplicacion(
        titulo = "Paso 8 · Dependencias y trabajo en equipo (Guía 10)",
        parrafos = listOf(
            "La guía cita estas dependencias en build.gradle.kts para que Navigation Compose, el ViewModel de Compose y las coroutines funcionen juntos.",
            "El avance del capítulo se hizo en la rama feature/home-screen con el commit 'Navegación y menú generado', siguiendo el flujo colaborativo de Git de la Guía 7: cada pantalla (Home, Profile, Settings) fue sumando su ruta al NavHost."
        ),
        codigo = """
            // build.gradle.kts (módulo app)
            implementation("androidx.navigation:navigation-compose:2.7.7")
            implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

            // Flujo de la guía:
            // rama feature/home-screen → commit "Navegación y menú generado"
        """
    ),
    BloqueExplicacion(
        titulo = "Mejora recomendada (switch «Versión mejorada»)",
        parrafos = listOf(
            "La guía emite cada evento con CoroutineScope(context = Dispatchers.Main).launch: crea un scope nuevo y 'suelto' que el ViewModel nunca cancela. Funciona, pero si el ViewModel se destruye a mitad de emisión, esa coroutine queda huérfana (riesgo de fuga).",
            "viewModelScope se cancela automáticamente al limpiar el ViewModel: cuando la pantalla muere no quedan coroutines vivas ni emisiones fantasma. Es el fix correcto y el patrón recomendado por Google.",
            "Además, la versión mejorada separa el NavHost en AppNavigationMejorada.kt (mismo estilo que la Guía 11 con su AppNavigation.kt): el contenedor solo decide rutas y las pantallas solo componen UI. El comportamiento visible es idéntico; el switch de arriba recrea el grafo interno para que lo compruebes en vivo."
        ),
        codigo = """
            // Versión guía: scope suelto, ajeno al ciclo de vida del ViewModel
            fun navigateTo(screen: Screen) {
                CoroutineScope(context = Dispatchers.Main).launch {
                    _navigationEvents.emit(NavigationEvent.NavigateTo(route = screen))
                }
            }

            // Versión mejorada: scope ligado al ViewModel (se cancela con él, sin fugas)
            fun navigateTo(screen: Screen) {
                viewModelScope.launch {
                    _navigationEvents.emit(NavigationEvent.NavigateTo(route = screen))
                }
            }
        """
    )
)
