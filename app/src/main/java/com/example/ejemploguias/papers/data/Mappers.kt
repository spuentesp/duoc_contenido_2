package com.example.ejemploguias.papers.data

import com.example.ejemploguias.papers.data.dto.WorkDto
import com.example.ejemploguias.papers.model.Paper

/**
 * Mappers: funciones puras que traducen entre capas.
 * DTO → dominio (salida de la red) y dominio → entidad (entrada a SQLite).
 * Si el JSON cambia, solo se toca este archivo.
 */

/** Devuelve null si el work no trae id: sin id no hay clave primaria. */
fun WorkDto.aPaper(): Paper? {
    val identificador = id ?: return null
    val doiLimpio = doi?.removePrefix("https://doi.org/")
    return Paper(
        id = identificador,
        titulo = titulo ?: nombreMostrado ?: "Sin título",
        autores = autorias
            ?.mapNotNull { it.autor?.nombre }
            ?.filter { it.isNotBlank() }
            ?.joinToString(", ")
            ?.ifBlank { null }
            ?: "Autoría no disponible",
        anio = anioPublicacion ?: 0,
        doi = doiLimpio,
        url = ubicacionPrincipal?.urlLanding ?: doi
    )
}

fun Paper.aEntidad(
    nota: String = "",
    fechaGuardado: Long = System.currentTimeMillis()
): PaperEntity = PaperEntity(
    id = id,
    titulo = titulo,
    autores = autores,
    anio = anio,
    doi = doi,
    url = url,
    nota = nota,
    fechaGuardado = fechaGuardado
)
