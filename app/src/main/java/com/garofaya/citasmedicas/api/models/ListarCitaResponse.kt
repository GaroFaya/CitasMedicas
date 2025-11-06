package com.garofaya.citasmedicas.api.models

data class ListarCitaResponse(
    val id: Int,
    val clinica_nombre: String,
    val doctor_nombre: String,
    val fecha: String,
    val hora_inicio: String,
    val hora_fin: String,
    val motivo: String,
    val estado: String
)
