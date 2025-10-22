package com.garofaya.citasmedicas.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuario")
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val dni: String,
    val password: String,
    val token: String? = null
)
