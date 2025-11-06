package com.garofaya.citasmedicas.api

import com.garofaya.citasmedicas.api.models.RegistrarCitaRequest
import com.garofaya.citasmedicas.api.models.RegistrarCitaResponse
import com.garofaya.citasmedicas.api.models.ValidarDniRequest
import com.garofaya.citasmedicas.api.models.ValidarDniResponse
import com.garofaya.citasmedicas.api.models.ChatbotInfoResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ChatBotService {

    @GET("chatbot/info/")
    fun getChatbotInfo(): Call<ChatbotInfoResponse>

    @POST("chatbot/validar-dni/")
    fun validarDni(@Body request: ValidarDniRequest): Call<ValidarDniResponse>

    @POST("chatbot/registrar-cita/")
    fun registrarCita(@Body body: RegistrarCitaRequest): Call<RegistrarCitaResponse>
}
