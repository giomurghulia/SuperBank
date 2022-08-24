package com.example.superbank.transactions.adapters.models
private var identity = 0
data class InnerModel( val title: String, val type: TransactionType, val amount: Double){
    val id = identity
    init{
        identity++
    }
}