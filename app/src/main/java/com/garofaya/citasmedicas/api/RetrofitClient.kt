package com.garofaya.citasmedicas.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {

    private const val BASE_URL = "https://apis.ntechs.net.pe/medisol/api/"

    private val retrofit: Retrofit by lazy {
        // Logging para depurar llamadas HTTP
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    // 🧠 Servicio del chatbot (mantenerlo)
    val chatBotService: ChatBotService by lazy {
        retrofit.create(ChatBotService::class.java)
    }

    // 👤 Servicio de usuarios
    val usuarioService: UsuarioService by lazy {
        retrofit.create(UsuarioService::class.java)
    }

    // 🩺 Servicio de gestión de citas médicas (nuevo)
    val citaService: CitaService by lazy {
        retrofit.create(CitaService::class.java)
    }
}
