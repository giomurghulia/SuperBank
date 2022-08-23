package com.example.superbank.cards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superbank.networking.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MyCardsViewModel : ViewModel() {

    private val _cards = MutableStateFlow<List<Card>>(emptyList())
    val cards get() = _cards.asStateFlow()

    fun getCards() {
        viewModelScope.launch {
            val response = RetrofitClient.apiService.getCards()

            println(response)
            if (response.isSuccessful) {
                val cards = response.body()
                _cards.value = cards ?: emptyList()
            }
        }
    }
}