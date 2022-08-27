package com.example.superbank.offers.adapter

import com.squareup.moshi.Json

data class OfferModel(
    val id: Int,
    val title: String,
    val category: String,
    val avatar: String,
    val url: String
)
