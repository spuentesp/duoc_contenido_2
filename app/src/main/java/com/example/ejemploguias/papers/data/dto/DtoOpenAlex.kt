package com.example.ejemploguias.papers.data.dto

import com.google.gson.annotations.SerializedName

/**
 * DTOs de la API pública OpenAlex (GET https://api.openalex.org/works).
 *
 * Son los ÚNICOS lugares donde conviven Kotlin y el vocabulario del JSON:
 * cada propiedad que no se llama igual que su clave lleva @SerializedName.
 * Gson ignora los campos extra de la respuesta (abstract_inverted_index, topics, ...).
 * Todos son anulables porque la API puede omitirlos (doi, primary_location, ...).
 */
data class RespuestaBusquedaDto(
    val meta: MetaDto?,
    @SerializedName("results") val resultados: List<WorkDto>?
)

data class MetaDto(
    @SerializedName("count") val total: Int?
)

data class WorkDto(
    val id: String?,
    val doi: String?,
    @SerializedName("title") val titulo: String?,
    @SerializedName("display_name") val nombreMostrado: String?,
    @SerializedName("publication_year") val anioPublicacion: Int?,
    @SerializedName("authorships") val autorias: List<AuthorshipDto>?,
    @SerializedName("primary_location") val ubicacionPrincipal: UbicacionDto?
)

data class AuthorshipDto(
    @SerializedName("author") val autor: AutorDto?
)

data class AutorDto(
    @SerializedName("display_name") val nombre: String?
)

data class UbicacionDto(
    @SerializedName("landing_page_url") val urlLanding: String?
)
