package com.example.dongi.api

import android.icu.text.DateFormat
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
    val name: String,
    val members: List<User>
)

data class ShareMembers(
    val user: String,
    val share: Number
)

data class GroupsResponse(
    val groups: List<Group>
)

data class AddGroupRequest(
    val name: String
)

data class Share (
    val user: String,
    val share: String
)

data class AddExpenseRequest(
    val group: String,
    val payer: String,
    val description: String,
    val amount: String,
    val date: String,
    val shares: List<Share>
)

data class updateExpenseRequest(
    val expense_id: String,
    val payer: String,
    val description: String,
    val amount: String,
    val date: String,
    val shares: List<Share>
)

data class RemoveExpenseRequest(
    val expense_id: String
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

    @POST("/api/expense/add")
    fun addExpense(@Body request: AddExpenseRequest): Call<Void>

    @POST("/api/expense/remove")
    fun removeExpense(@Body request: RemoveExpenseRequest): Call<Void>

    @POST("/api/expense/update")
    fun updateExpense(@Body request: updateExpenseRequest): Call<Void>
}