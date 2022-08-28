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
    @POST("0df7330a-07af-4a14-bcd1-48e7d9dda304")
    suspend fun getAuthorizedUserDate(): Response<AuthorizedUser>

    @POST("a02873c1-ed55-4a92-b5bf-733a70ea9030")
    suspend fun getCards(): Response<List<Card>>

    @POST("090e51b1-fa61-4996-879e-9b5666b6bf20")
    suspend fun getUserWithNumber(@Query("number") number: String): Response<User>

    @POST("090e51b1-fa61-4996-879e-9b5666b6bf20")
    suspend fun getUserWithIban(@Query("iban") iban: String): Response<User>

    @POST("97b1fc29-5d3d-44c1-831e-02a4c417f78f")
    suspend fun getUpcomingPayment(@Query("uniqueId") uniqueId: String): Response<UpcomingPayment>

    @POST("13269c18-b9ee-468e-ba94-93d877987243")
    suspend fun getCardTransactions(@Query("uniqueId") uniqueId: String): Response<List<CardTransactions>>

    @POST(" ")
    suspend fun makeTransfer(@Body body: Transaction): Response<String>

    @POST("fb3cde5d-eb2f-49d6-8ea2-6f774593164c")
    suspend fun getAllTransactions(): Response<List<TransactionsGetModel>>

    @GET("9b8dce28-9481-4194-9a93-34a9841038d4")
    suspend fun getOffers(): Response<List<OfferModel>>

    @GET
    suspend fun getCurrency(@Url link: String): Response<CurrencyItemModel>
}