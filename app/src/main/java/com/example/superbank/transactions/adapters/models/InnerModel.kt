package com.example.superbank.transactions.adapters.models
private var identity = 0
data class InnerModel( val title: String, val type: TransactionType, val amount: Double, val cardLastDigits: String, val description: String){
    val id = identity
    init{
        identity++
    }
}