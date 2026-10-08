package com.example.ejemploguias.papers.data

import com.example.ejemploguias.papers.data.dto.RespuestaBusquedaDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Contrato HTTP con OpenAlex (la Guía 7 no cubre redes: aquí Retrofit
 * reemplaza al "origen de datos simulado" del repository placeholder).
 *
 * Retrofit implementa esta interfaz en tiempo de ejecución y devuelve
 * el JSON ya convertido a [RespuestaBusquedaDto] por converter-gson.
 */
interface OpenApi {

    /** GET /works?search={consulta}&per-page={porPagina} */
    @GET("works")
    suspend fun buscar(
        @Query("search") consulta: String,
        @Query("per-page") porPagina: Int
    ): RespuestaBusquedaDto
}
