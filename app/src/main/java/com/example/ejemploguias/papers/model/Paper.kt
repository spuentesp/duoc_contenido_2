package com.example.ejemploguias.papers.model

/**
 * Modelo de dominio de la app: lo que ve la UI y guarda el repository.
 * No conoce Gson (DTOs) ni Room (entidades): es un plano intermedio.
 *
 * @param doi identificador DOI sin prefijo (p. ej. "10.1145/3524613.3527811").
 * @param url enlace para abrir en el navegador (landing page o DOI).
 */
data class Paper(
    val id: String,
    val titulo: String,
    val autores: String,
    val anio: Int,
    val doi: String?,
    val url: String?
)
