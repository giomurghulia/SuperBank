package com.example.superbank.transactions.adapters.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


private var identity = 0

@Parcelize
data class InnerModel(
    val title: String,
    val type: TransactionType,
    val amount: Double,
    val cardLastDigits: String,
    val description: String,
    val cardType: CardType
) : Parcelable {
    val id = identity

    init {
        identity++
    }
}