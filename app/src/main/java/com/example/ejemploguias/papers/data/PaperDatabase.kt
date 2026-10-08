package com.example.ejemploguias.papers.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Base de datos SQLite local. Singleton estándar de Room:
 * `@Volatile` + `synchronized` garantizan una sola instancia en todo el proceso.
 */
@Database(entities = [PaperEntity::class], version = 1, exportSchema = false)
abstract class PaperDatabase : RoomDatabase() {

    abstract fun paperDao(): PaperDao

    companion object {
        @Volatile
        private var instancia: PaperDatabase? = null

        fun obtener(contexto: Context): PaperDatabase {
            return instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    contexto.applicationContext,
                    PaperDatabase::class.java,
                    "papers.db"
                ).build().also { instancia = it }
            }
        }
    }
}
