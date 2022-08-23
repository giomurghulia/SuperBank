package com.example.superbank.cards

data class UpcomingPayment(
    val amount: Double,
    val iban: String,
    val dueDate: String
)