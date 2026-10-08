package com.example.ejemploguias.papers.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Fila de la tabla "papers" (SQLite vía Room).
 * Mismos datos que [com.example.ejemploguias.papers.model.Paper] más lo local:
 * la nota del alumno y cuándo se guardó.
 */
@Entity(tableName = "papers")
data class PaperEntity(
    @PrimaryKey val id: String,
    val titulo: String,
    val autores: String,
    val anio: Int,
    val doi: String?,
    val url: String?,
    val nota: String = "",
    val fechaGuardado: Long = 0L
)
