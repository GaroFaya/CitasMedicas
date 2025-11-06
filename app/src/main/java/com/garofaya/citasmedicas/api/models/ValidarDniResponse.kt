package com.garofaya.citasmedicas.api.models

data class ValidarDniResponse(
    val valido: Boolean,
    val existe: Boolean,
    val usuario: Usuario?,
    val mensaje: String?
)

data class UsuarioValidacion(
    val nombres: String,
    val apellidos: String,
    val email: String,
    val celular: String
)
