package com.example.ejemploguias.ejemplos.navegacion

/**
 * Rutas internas del demo de Navegación (espejo de la Guía 10, pág. 3).
 * Vive en este paquete para no chocar con `navigation.Screen` del catálogo global.
 */
sealed class Screen(val route: String) {
    // `data object` es un singleton seguro de tipos (Kotlin 1.9+)
    data object Home : Screen(route = "home_page")
    data object Profile : Screen(route = "profile_page")
    data object Settings : Screen(route = "settings_page")

    // Ejemplo de ruta con argumento: "detail_page/{itemId}"
    data class Detail(val itemId: String) : Screen(route = "detail_page/{itemId}") {
        fun buildRoute() = route.replace(oldValue = "{itemId}", newValue = itemId)
    }
}
