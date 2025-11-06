package com.garofaya.citasmedicas.model

data class RespuestaBot(
    val mensaje: String? = null,
    val error: String? = null,
    val campos_faltantes: List<String>? = null,
    val especialidades: List<Especialidad>? = null,
    val doctores: List<Doctor>? = null,
    val horarios: List<Horario>? = null,
    val disponibilidades: List<Disponibilidad>? = null
)

data class Especialidad(
    val id: Int,
    val nombre: String,
    val descripcion: String
)

data class Doctor(
    val id: Int,
    val nombres: String,
    val apellidos: String,
    val clinica_id: Int,
    val titulo: String,
    val precio_consulta_base: Double
)

data class Horario(
    val id: Int,
    val doctor_id: Int,
    val dia_semana: Int,
    val hora_inicio: String,
    val hora_fin: String,
    val duracion_cita: Int
)

data class Disponibilidad(
    val id: Int,
    val doctor_id: Int,
    val fecha: String,
    val hora_inicio: String,
    val hora_fin: String
)
