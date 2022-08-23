package com.example.superbank.networking

import com.example.superbank.transfer.User
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.POST
import retrofit2.http.Query

object RetrofitClient {
    private const val BASE_URL = "https://run.mocky.io/v3/"

    private val interceptor = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }
    private val client = OkHttpClient.Builder().addInterceptor(interceptor).build()


    private val retrofitBuilder by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    val apiService by lazy {
        retrofitBuilder.create(ApiService::class.java)
    }
}

interface ApiService {
//    @POST("ab565edd-01b6-417e-bbdc-502c3b432cd9")
//    suspend fun getCards(): Response
//
//    @POST("91fc90ba-bebe-476a-8198-9389d9306671")
//    suspend fun getPayment(): Response

    @POST("090e51b1-fa61-4996-879e-9b5666b6bf20")
    suspend fun getUserWithNumber(@Query("number") number: String): Response<User>

    @POST("090e51b1-fa61-4996-879e-9b5666b6bf20")
    suspend fun getUserWithIban(@Query("iban") iban: String): Response<User>
}