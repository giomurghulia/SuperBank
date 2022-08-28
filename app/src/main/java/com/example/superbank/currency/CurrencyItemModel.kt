package com.example.superbank.currency

data class CurrencyItemModel(
    val rates: CurrencyItem
)

data class CurrencyItem(
    val GEL: Double,
    val USD: Double,
    val EUR: Double,
    val GBP: Double
)
