package com.example.ejemploguias.papers.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Acceso a la tabla "papers". Room genera la implementación con KSP.
 * `favoritos()` devuelve un Flow: cada cambio en la tabla re-emite la lista
 * y la UI que lo escuche se redibuja sola (patrón reactivo de la Guía 11).
 */
@Dao
interface PaperDao {

    fun favoritos(): Flow<List<PaperEntity>>

    @Query("SELECT * FROM papers WHERE id = :id")
    suspend fun obtenerPorId(id: String): PaperEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardar(paper: PaperEntity)

    @Query("UPDATE papers SET nota = :nota WHERE id = :id")
    suspend fun actualizarNota(id: String, nota: String)

    @Delete
    suspend fun eliminar(paper: PaperEntity)
}
