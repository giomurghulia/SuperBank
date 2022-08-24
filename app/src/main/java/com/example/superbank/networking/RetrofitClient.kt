package com.example.superbank.networking

import com.example.superbank.cards.Card
import com.example.superbank.cards.CardTransactions
import com.example.superbank.cards.UpcomingPayment
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
    @POST("1ca0503a-753e-4523-9c63-297f58a5c64d")
    suspend fun getCards(): Response<List<Card>>

    @POST("97b1fc29-5d3d-44c1-831e-02a4c417f78f")
    suspend fun getUpcomingPayment(@Query("uniqueId") uniqueId: String): Response<UpcomingPayment>

    @POST("35234356-0b36-417c-8563-0c36b842c3fb")
    suspend fun getCardTransactions(@Query("uniqueId") uniqueId: String): Response<List<CardTransactions>>
}