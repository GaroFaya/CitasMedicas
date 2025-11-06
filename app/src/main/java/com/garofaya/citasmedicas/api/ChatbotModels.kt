package com.garofaya.citasmedicas.api

data class ChatbotInfoResponse(
    val especialidades: List<Especialidad>,
    val doctores: List<Doctor>,
    val horarios: List<Horario>,
    val disponibilidades: List<Disponibilidad>
)

data class Especialidad(val id: Int, val nombre: String)
data class Doctor(val id: Int, val nombres: String, val apellidos: String, val titulo: String, val clinica_id: Int)
data class Horario(val id: Int, val doctor_id: Int, val dia_semana: Int, val hora_inicio: String, val hora_fin: String)
data class Disponibilidad(val id: Int, val doctor_id: Int, val fecha: String, val hora_inicio: String, val hora_fin: String)
