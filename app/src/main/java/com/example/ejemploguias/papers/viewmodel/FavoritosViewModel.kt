package com.example.ejemploguias.papers.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.ejemploguias.papers.data.PaperEntity
import com.example.ejemploguias.papers.data.PaperServiceLocator
import com.example.ejemploguias.papers.repository.PaperRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Estado de la pestaña Favoritos: todo vive en SQLite, por lo que la lista
 * funciona sin conexión y se redibuja sola cada vez que el Flow del DAO emite.
 */
class FavoritosViewModel(private val repositorio: PaperRepository) : ViewModel() {

    val favoritos: StateFlow<List<PaperEntity>> = repositorio.favoritos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _eventos = MutableSharedFlow<String>()
    val eventos: SharedFlow<String> = _eventos.asSharedFlow()

    fun actualizarNota(id: String, nota: String) {
        viewModelScope.launch {
            repositorio.actualizarNota(id, nota)
            _eventos.emit("Nota guardada")
        }
    }

    fun eliminar(paper: PaperEntity) {
        viewModelScope.launch {
            repositorio.eliminar(paper)
            _eventos.emit("Paper eliminado de favoritos")
        }
    }

    companion object {
        /** Sin Hilt: la fábrica pide el repository al ServiceLocator. */
        fun fabrica(contexto: Context) = viewModelFactory {
            initializer { FavoritosViewModel(PaperServiceLocator.repositorio(contexto)) }
        }
    }
}
