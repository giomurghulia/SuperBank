package com.example.superbank.cards

data class Card(
    val cardNumber: String,
    val cardBallance: Double,
    val cardDate: String,
    val cardCvc: Int,
    val cardType: String,
)