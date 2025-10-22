package com.garofaya.citasmedicas.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.garofaya.citasmedicas.db.entity.Usuario

@Dao
interface UsuarioDao {
    @Insert
    fun insert(usuario: Usuario)

    @Query("SELECT * FROM usuario LIMIT 1")
    fun listar(): Usuario?

    @Delete
    fun eliminar(usuario: Usuario)  // <- este método elimina un usuario

    @Query("SELECT * FROM usuario WHERE dni = :dni AND password = :password")
    fun validarUsuario(dni: String, password: String): Usuario?
}
