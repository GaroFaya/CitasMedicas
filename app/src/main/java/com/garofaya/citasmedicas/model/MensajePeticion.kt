package com.garofaya.citasmedicas.model

data class MensajePeticion(
    val dni: String,
    val nombres: String,
    val apellidos: String,
    val celular: String,
    val doctor_id: Int,
    val fecha: String,
    val hora_inicio: String,
    val motivo: String
)
