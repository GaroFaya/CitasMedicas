package com.garofaya.citasmedicas.api

import com.garofaya.citasmedicas.api.models.ListarCitaResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface CitaService {

    @GET("clinicas/citas/mis_citas/")
    fun listarCitas(
        @Header("Authorization") token: String
    ): Call<List<ListarCitaResponse>>
}
