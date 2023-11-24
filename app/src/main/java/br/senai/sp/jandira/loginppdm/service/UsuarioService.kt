package br.senai.sp.jandira.loginppdm.service

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface UsuarioService {
    @Headers("Content-Type: application/json")
    @POST("/usuario/cadastrarUsuario")
    suspend fun cadastrarUsuario(@Body body: JsonObject): Response<JsonObject>

}