package com.romisspa.app.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.romisspa.app.data.local.dao.SpaDao
import com.romisspa.app.data.local.entities.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        ServicioEntity::class,
        CitaEntity::class,
        ClienteEntity::class,
        ProductoEntity::class,
        EmpleadoEntity::class,
        VentaEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class SpaDatabase : RoomDatabase() {
    abstract fun spaDao(): SpaDao

    companion object {
        @Volatile
        private var INSTANCE: SpaDatabase? = null

        fun getDatabase(context: Context): SpaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SpaDatabase::class.java,
                    "spa_database"
                )
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Pre-populate database on creation
                        INSTANCE?.let { database ->
                            CoroutineScope(Dispatchers.IO).launch {
                                populateDatabase(database.spaDao())
                            }
                        }
                    }
                })
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }

        private suspend fun populateDatabase(spaDao: SpaDao) {
            // Add initial services
            val initialServices = listOf(
                ServicioEntity(nombre = "Corte y Peinado", descripcion = "Corte dama/caballero + peinado express", precio = "S/ 45.00", duracion = "45 min"),
                ServicioEntity(nombre = "Manicure Completa", descripcion = "Limpieza, limado y esmaltado en gel", precio = "S/ 35.00", duracion = "60 min"),
                ServicioEntity(nombre = "Limpieza Facial", descripcion = "Hidratación profunda con vapor de ozono", precio = "S/ 80.00", duracion = "50 min"),
                ServicioEntity(nombre = "Masaje Relajante", descripcion = "Masaje de cuerpo completo con aceites", precio = "S/ 120.00", duracion = "60 min"),
                ServicioEntity(nombre = "Tinte y Color", descripcion = "Coloración completa con productos premium", precio = "S/ 150.00", duracion = "120 min"),
                ServicioEntity(nombre = "Pedicure Spa", descripcion = "Exfoliación, masaje y esmaltado", precio = "S/ 40.00", duracion = "60 min")
            )
            initialServices.forEach { spaDao.insertServicio(it) }

            // Add initial clients
            val initialClients = listOf(
                ClienteEntity("Ana García", "987 654 321", "12 Oct 2023", 5),
                ClienteEntity("Beatriz López", "912 345 678", "05 Nov 2023", 2),
                ClienteEntity("Carla Méndez", "998 877 665", "20 Nov 2023", 8),
                ClienteEntity("Diana Pérez", "944 556 677", "15 Nov 2023", 3),
                ClienteEntity("Elena Rivas", "922 113 344", "01 Nov 2023", 12)
            )
            initialClients.forEach { spaDao.insertCliente(it) }
        }
    }
}
