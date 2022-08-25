package com.example.superbank.transfer

data class Transaction(
    val fromCardId: String,
    val toAddress: String,
    val amount: Double,
)