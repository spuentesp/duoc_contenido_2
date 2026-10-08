package com.example.ejemploguias.papers.data

import android.content.Context
import com.example.ejemploguias.papers.repository.PaperRepository

/**
 * ServiceLocator: el punto único donde se arma la capa de datos.
 * La app no usa Hilt, así que este objeto cumple su función mínima:
 * mantener UN repository (y por tanto UNA base de datos) durante toda la app.
 * La UI lo pide con `PaperServiceLocator.repositorio(contexto)`.
 */
object PaperServiceLocator {

    @Volatile
    private var repositorio: PaperRepository? = null

    fun repositorio(contexto: Context): PaperRepository {
        return repositorio ?: synchronized(this) {
            repositorio ?: PaperRepository(
                api = OpenApiClient.api,
                dao = PaperDatabase.obtener(contexto).paperDao()
            ).also { repositorio = it }
        }
    }
}
