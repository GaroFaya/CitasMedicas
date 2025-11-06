package com.garofaya.citasmedicas.api

data class Cita(
    val id: Int,
    val dni: String,
    val nombres: String,
    val apellidos: String,
    val email: String,
    val celular: String,
    val doctor_id: Int,
    val fecha: String,
    val hora_inicio: String,
    val motivo: String
)
