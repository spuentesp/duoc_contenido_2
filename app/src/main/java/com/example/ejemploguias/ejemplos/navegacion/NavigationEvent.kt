package com.example.ejemploguias.ejemplos.navegacion

/**
 * Eventos de navegación emitidos por el ViewModel (Guía 10, pág. 4).
 * La UI no toca `navController` directamente: solo emite estos eventos.
 */
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
