package com.garofaya.citasmedicas.api

import com.garofaya.citasmedicas.api.models.RegistrarCitaRequest
import com.garofaya.citasmedicas.api.models.RegistrarCitaResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CitaService {
    @POST("clinicas/citas/")
    fun registrarCita(@Body request: RegistrarCitaRequest): Call<RegistrarCitaResponse>

    @GET("clinicas/citas/")
    fun listarCitas(@Query("paciente") pacienteId: Int): Call<List<RegistrarCitaResponse>>


}
