package com.example.dongi.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
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

data class UserDataResponse(
    val user: User
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

data class AddGroupRequest(
    val name: String
)

data class AddGroupResponse(
    val group: Group
)


interface ApiService {
    @POST("api/user/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("api/user/signup")
    fun signup(@Body request: SignupRequest): Call<SignupResponse>

    @DELETE("api/user/logout")
    fun logoutUser(): Call<Void>

    @GET("api/user/data")
    fun getUserData(): Call<UserDataResponse>

    @POST("api/user/update")
    fun updateUserData(@Body request: Map<String, String>): Call<Void>

    @GET("api/group/list")
    fun getGroups(): Call<GroupsResponse>

    @GET("api/group/data/{id}")
    fun getGroupDetails(@Path("id") id: String): Call<Group>

    @POST("api/group/add")
    fun addGroup(@Body request: AddGroupRequest): Call<AddGroupResponse>
}