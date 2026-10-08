package com.example.ejemploguias.papers.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Cliente único de OpenAlex. `by lazy` construye el Retrofit la primera
 * vez que se usa y lo reutiliza para siempre (singleton barato).
 */
object OpenApiClient {

    private const val BASE_URL = "https://api.openalex.org/"

    val api: OpenApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenApi::class.java)
    }
}
