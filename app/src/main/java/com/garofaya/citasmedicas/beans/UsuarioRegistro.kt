package com.garofaya.citasmedicas.beans

data class UsuarioRegistro(
    val tipoDocumento: String, // <-- agregar este campo
    val dni: String,
    val email: String,
    val username: String,
    val password: String,
    val password2: String,
    val first_name: String,
    val last_name: String,
    val telefono: String,
    val direccion: String
)
