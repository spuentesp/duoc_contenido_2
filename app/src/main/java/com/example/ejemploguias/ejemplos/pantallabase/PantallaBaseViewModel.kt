package com.example.ejemploguias.ejemplos.pantallabase

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** Lógica de presentación de la pantalla base: contador de clics (state hoisting). */
class PantallaBaseViewModel : ViewModel() {

    private val _contador = MutableStateFlow(0)
    val contador: StateFlow<Int> = _contador.asStateFlow()

    fun incrementar() {
        _contador.value += 1
    }
}
