package com.example.ejemploguias.navigation

/**
 * Rutas globales del catálogo de ejemplos (estilo Guía 10: sealed class tipo-segura).
 *
 * Cada demo tiene dos rutas: la demo en vivo y su explicación "¿Cómo se hizo?".
 * Las rutas internas de la demo de Navegación (Guía 10) viven en su propio NavHost
 * dentro de `ejemplos/navegacion/`.
 */
sealed class Screen(val route: String) {
    data object Menu : Screen(route = "menu")

    data object DemoEstructura : Screen(route = "demo_estructura")
    data object ExplicacionEstructura : Screen(route = "explicacion_estructura")

    data object DemoPantallaBase : Screen(route = "demo_pantalla_base")
    data object ExplicacionPantallaBase : Screen(route = "explicacion_pantalla_base")

    data object DemoAdaptabilidad : Screen(route = "demo_adaptabilidad")
    data object ExplicacionAdaptabilidad : Screen(route = "explicacion_adaptabilidad")

    data object DemoNavegacion : Screen(route = "demo_navegacion")
    data object ExplicacionNavegacion : Screen(route = "explicacion_navegacion")

    data object DemoFormularios : Screen(route = "demo_formularios")
    data object ExplicacionFormularios : Screen(route = "explicacion_formularios")

    data object DemoCamara : Screen(route = "demo_camara")
    data object ExplicacionCamara : Screen(route = "explicacion_camara")

    data object DemoPapers : Screen(route = "demo_papers")
    data object ExplicacionPapers : Screen(route = "explicacion_papers")
}
