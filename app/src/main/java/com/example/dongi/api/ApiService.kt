package com.example.dongi.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class LoginRequest(
    val email: String,
    val password: String
)

interface ApiService {
    @POST("login")
    fun login(@Body request: LoginRequest): Call<ResponseBody>
}