package com.example.ejemploguias.papers.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.ejemploguias.papers.data.PaperServiceLocator
import com.example.ejemploguias.papers.model.Paper
import com.example.ejemploguias.papers.repository.PaperRepository
import com.example.ejemploguias.papers.repository.ResultadoApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Estado de la pestaña Buscar (patrón Guía 11: StateFlow para el estado que
 * la pantalla pinta, SharedFlow para eventos de una sola vez tipo snackbar).
 */
class BuscarPapersViewModel(private val repositorio: PaperRepository) : ViewModel() {

    private val _consulta = MutableStateFlow("")
    val consulta: StateFlow<String> = _consulta.asStateFlow()

    private val _estado =
        MutableStateFlow<ResultadoApi<List<Paper>>>(ResultadoApi.Exito(emptyList()))
    val estado: StateFlow<ResultadoApi<List<Paper>>> = _estado.asStateFlow()

    /** ids ya presentes en Room: la tarjeta pinta "Guardado" sin tocar la API. */
    val idsGuardados: StateFlow<Set<String>> = repositorio.favoritos()
        .map { lista -> lista.map { it.id }.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptySet())

    /** Eventos de una sola vez (snackbar). Con buffer: un emisor nunca pierde
     *  el evento aunque el colector de la UI se incorpore una fracción después. */
    private val _eventos = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val eventos: SharedFlow<String> = _eventos.asSharedFlow()

    fun cambiarConsulta(valor: String) {
        _consulta.value = valor
    }

    fun buscar() {
        val texto = _consulta.value.trim()
        if (texto.isBlank()) return
        viewModelScope.launch {
            _estado.value = ResultadoApi.Cargando
            _estado.value = repositorio.buscarPapers(texto)
        }
    }

    fun guardar(paper: Paper) {
        viewModelScope.launch {
            repositorio.guardar(paper)
            _eventos.emit("Guardado en favoritos: ${paper.titulo}")
        }
    }

    companion object {
        /** Sin Hilt: la fábrica pide el repository al ServiceLocator. */
        fun fabrica(contexto: Context) = viewModelFactory {
            initializer { BuscarPapersViewModel(PaperServiceLocator.repositorio(contexto)) }
        }
    }
}
