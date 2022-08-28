package com.example.superbank.networking

import com.example.superbank.AuthorizedUser
import com.example.superbank.cards.Card
import com.example.superbank.cards.CardTransactions
import com.example.superbank.cards.UpcomingPayment
import com.example.superbank.currency.CurrencyItemModel
import com.example.superbank.offers.adapter.OfferModel
import com.example.superbank.transactions.getmodel.TransactionsGetModel
import com.example.superbank.transfer.Transaction
import okhttp3.Interceptor
import com.example.superbank.transfer.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

object RetrofitClient {
    private const val BASE_URL = "https://run.mocky.io/v3/"
    private const val TOKEN_KEY = "token"
    val token: String?
        get() {
            return Firebase.auth.currentUser?.uid
        }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }


    private val retrofitBuilder by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(createOkhttpClient())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    val apiService: ApiService by lazy {
        retrofitBuilder.create(ApiService::class.java)
    }

    private fun createOkhttpClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
            .addInterceptor(addDefaultParams())
            .addInterceptor(loggingInterceptor)
        return httpClient.build()
    }

    private fun addDefaultParams(): Interceptor {
        return Interceptor { chain ->
            val url = chain.request()
                .url.newBuilder()
                .addQueryParameter(TOKEN_KEY, token)
                .build()

            chain.proceed(chain.request().newBuilder().url(url).build())

        }
    }
}

interface ApiService {
    @POST("5420dba3-7074-424f-9a18-b6bbfe395e6c")
    suspend fun getAuthorizedUserDate(): Response<AuthorizedUser>

    @POST("1ca0503a-753e-4523-9c63-297f58a5c64d")
    suspend fun getCards(): Response<List<Card>>

    @POST("090e51b1-fa61-4996-879e-9b5666b6bf20")
    suspend fun getUserWithNumber(@Query("number") number: String): Response<User>

    @POST("090e51b1-fa61-4996-879e-9b5666b6bf20")
    suspend fun getUserWithIban(@Query("iban") iban: String): Response<User>

    @POST("97b1fc29-5d3d-44c1-831e-02a4c417f78f")
    suspend fun getUpcomingPayment(@Query("uniqueId") uniqueId: String): Response<UpcomingPayment>

    @POST("35234356-0b36-417c-8563-0c36b842c3fb")
    suspend fun getCardTransactions(@Query("uniqueId") uniqueId: String): Response<List<CardTransactions>>

    @POST(" ")
    suspend fun makeTransfer(@Body body: Transaction): Response<String>

    @POST("7dfc1d37-00c4-44df-95cb-a496f3e03237?fbclid=IwAR0WFm0Qi6sopNKHFG-sfP0w_1Sgcu0uxEZGU7_Tu5_pL2w7pjhRyHbOqwU")
    suspend fun getTransactions(): Response<List<TransactionsGetModel>>

    @GET("bb3c6da5-2c18-42b4-98f9-d455ad89759f?fbclid=IwAR0fDWdRs2fpN0HY6yC0ezpiJ8MzT9p7F09VGFPVTwbmIie-vrnYPZcck1g")
    suspend fun getOffers(): Response<List<OfferModel>>

    @GET
    suspend fun getCurrency(@Url link: String): Response<CurrencyItemModel>
}