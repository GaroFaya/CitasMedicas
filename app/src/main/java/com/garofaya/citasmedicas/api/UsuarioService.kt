package com.garofaya.citasmedicas.api

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UsuarioService {
    @POST("token/") // ruta de tu API
    suspend fun login(@Body body: RequestBody): Response<ResponseBody>
}
//44546387