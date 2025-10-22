package com.garofaya.citasmedicas.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.garofaya.citasmedicas.db.dao.UsuarioDao
import com.garofaya.citasmedicas.db.entity.Usuario

@Database(entities = [Usuario::class], version = 1)
abstract class LocalDataBase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao

    companion object {
        @Volatile
        private var INSTANCE: LocalDataBase? = null

        fun getInstance(context: Context): LocalDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalDataBase::class.java,
                    "localdatabase"
                )
                    .fallbackToDestructiveMigration() // borra DB vieja
                    .allowMainThreadQueries() // aunque ideal sería Async
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}