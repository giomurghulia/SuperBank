package com.example.superbank.currency

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superbank.networking.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CurrencyViewModel : ViewModel() {

    private val _ratesState = MutableStateFlow<Resource>(Resource.Loading)
    val ratesState get() = _ratesState.asStateFlow()

    private val _result = MutableStateFlow<Double?>(0.0)
    val result get() = _result.asStateFlow()

    private var amount = 0.0
    private var currencyItem: CurrencyItem = CurrencyItem(0.0, 0.0, 0.0, 0.0)
    private var currencyRate: Double = 0.0


    private var allCurrencyItem = mutableMapOf<Int, CurrencyItem>()

    private fun calculateResult() {
        _result.tryEmit(amount * currencyRate)
    }

    fun setDate(position1: Int, position2: Int, num: Double) {
        currencyItem = allCurrencyItem[position1]!!
        setCurrencyRate(position2)
        amount = num

        calculateResult()
    }

    private fun setCurrencyRate(position2: Int) {
        when (position2) {
            0 -> {
                currencyRate = currencyItem.GEL
            }
            1 -> {
                currencyRate = currencyItem.USD
            }
            2 -> {
                currencyRate = currencyItem.EUR
            }
            3 -> {
                currencyRate = currencyItem.GBP
            }
        }
    }

    fun getCurrency(items: Array<String>) {
        items.forEachIndexed { index, element ->
            viewModelScope.launch {
                val url = CURRENCY_URL + element

                val response = RetrofitClient.apiService.getCurrency(url)

                if (response.isSuccessful) {
                    response.body()?.let { it -> allCurrencyItem.put(index, it.rates) }
                    if (allCurrencyItem.size == 4) {
                        _ratesState.tryEmit(Resource.Success(Unit))
                    }
                }
            }
        }

    }

    companion object {
        private const val CURRENCY_URL = "https://open.er-api.com/v6/latest/"
    }
}