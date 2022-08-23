package com.example.superbank.networking

import com.example.superbank.cards.Card
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.POST

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
    @POST("ab565edd-01b6-417e-bbdc-502c3b432cd9")
    suspend fun getCards(): Response<List<Card>>

    @POST("91fc90ba-bebe-476a-8198-9389d9306671")
    suspend fun getPayment(): Response<Any>
}