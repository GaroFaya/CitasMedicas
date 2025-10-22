package com.garofaya.citasmedicas.api

import com.garofaya.citasmedicas.beans.UsuarioRegistro
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UsuarioService {

    @POST("usuarios/registro/")
    suspend fun registrar(@Body usuario: UsuarioRegistro): Response<ResponseBody>

    @POST("token/") // tu login actual
    suspend fun login(@Body body: okhttp3.RequestBody): Response<ResponseBody>
}

//44546387