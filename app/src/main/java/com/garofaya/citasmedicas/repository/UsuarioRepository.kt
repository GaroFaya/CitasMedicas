package com.garofaya.citasmedicas.repository

import com.garofaya.citasmedicas.api.UsuarioService
import com.garofaya.citasmedicas.db.entity.Usuario
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UsuarioRepository {

    private val service: UsuarioService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://medisol-health.onrender.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(UsuarioService::class.java)
    }

    suspend fun login(dni: String, password: String): Response<ResponseBody> {
        val json = JSONObject().apply {
            put("dni", dni)
            put("password", password)
        }
        val body: RequestBody = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        return service.login(body)
    }
}
