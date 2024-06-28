package com.example.dongi.api

import android.content.Context
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://91.228.186.176/" // For emulator

    private fun getOkHttpClient(context: Context): OkHttpClient {

        val authInterceptor = Interceptor { chain ->
            val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
            val token = sharedPreferences.getString("AUTH_TOKEN", null)

            val request: Request = if (token != null) {
                chain.request().newBuilder()
                    .addHeader("token", "$token")
                    .build()
            } else {
                chain.request()
            }

            chain.proceed(request)
        }

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    fun getInstance(context: Context): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getOkHttpClient(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }
}
