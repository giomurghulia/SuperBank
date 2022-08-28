package com.example.superbank.cards

data class Card(
    val cardLastDigit: String,
    val cardBalance: Double,
    val cardDate: String,
    val cardCvc: Int,
    val cardType: String,
    val iban: String,
    val uniqueId: String
)