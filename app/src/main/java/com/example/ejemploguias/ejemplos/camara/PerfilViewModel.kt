package com.example.ejemploguias.ejemplos.camara

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Espejo Guía 13, Parte 2: Uri? en MutableStateFlow,
 * con funciones separadas para galería y cámara.
 */
class PerfilViewModel : ViewModel() {
    private val _imagen = MutableStateFlow<Uri?>(null)
    val imagen: StateFlow<Uri?> = _imagen

    fun onImagenDesdeGaleria(uri: Uri?) {
        _imagen.value = uri
    }

    fun onImagenDesdeCamara(uri: Uri?) {
        _imagen.value = uri
    }
}
