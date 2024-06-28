package com.example.dongi.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val user: User
)

data class User(
    val first_name: String,
    val last_name: String,
    val email: String
)

data class SignupRequest(
    val first_name: String,
    val last_name: String,
    val email: String,
    val password: String
)

data class SignupResponse(
    val token: String,
    val user: User
)

data class Group(
    val id: String,
    val name: String
)

data class GroupsResponse(
    val groups: List<Group>
)


interface ApiService {
    @POST("api/user/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("api/user/signup")
    fun signup(@Body request: SignupRequest): Call<SignupResponse>

    @GET("api/group/list")
    fun getGroups(): Call<GroupsResponse>

    @GET("api/group/data/{id}")
    fun getGroupDetails(@Path("id") id: String): Call<Group>
}