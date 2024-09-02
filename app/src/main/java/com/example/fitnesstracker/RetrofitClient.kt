package com.example.fitnesstracker

import android.os.Build
import androidx.annotation.RequiresApi
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Base64

@RequiresApi(Build.VERSION_CODES.O)
object RetrofitClient {
    private const val BASE_URL = "https://api.api-ninjas.com/" // Replace with your actual API base URL
    private const val API_KEY = "h+BQn0ypyYzsc+R2u2yYIQ==jEqkb31lwp2RvlIJ" // Replace with your actual API key
    private const val USERNAME = "username" // Replace with your username, if using Basic Auth
    private const val PASSWORD = "password" // Replace with your password, if using Basic Auth

    // Create a logging interceptor
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Log request and response body
    }

    // Create OkHttpClient with interceptors
    @RequiresApi(Build.VERSION_CODES.O)
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor) // Add logging interceptor
        .addInterceptor { chain ->
            val originalRequest: Request = chain.request()

            // Add API Key header
            val requestWithApiKey: Request = originalRequest.newBuilder()
                .addHeader("X-Api-Key", API_KEY) // Use "Authorization" if needed
                .build()

            // If using Basic Authentication (comment out if not needed)
            val credentials = "$USERNAME:$PASSWORD"
            val authHeader = "Basic ${Base64.getEncoder().encodeToString(credentials.toByteArray())}"
            val requestWithAuth: Request = requestWithApiKey.newBuilder()
                .addHeader("Authorization", authHeader)
                .build()

            // Proceed with the customized request
            chain.proceed(requestWithAuth)
        }
        .build()

    // Create Retrofit instance
    val instance: ExerciseService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ExerciseService::class.java)
    }
}
