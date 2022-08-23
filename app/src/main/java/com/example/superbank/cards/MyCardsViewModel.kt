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

    private val _ListItems = MutableStateFlow<List<MyCardsListItem>>(emptyList())
    val ListItems get() = _ListItems.asStateFlow()

    private var myCards = listOf<Card>()

    fun getCards() {
        viewModelScope.launch {
            val response = RetrofitClient.apiService.getCards()

            println(response)
            if (response.isSuccessful) {
                val cards = response.body()
                _cards.value = cards ?: emptyList()

                myCards = cards ?: emptyList()

                if (!cards.isNullOrEmpty()) {
                    getUpcomingPaymentAndTransactions(cards[0].cardNumber)
                }
            }
        }
    }

    fun onCardSelected(cardPosition: Int) {
        getUpcomingPaymentAndTransactions(myCards[cardPosition].cardNumber)
    }

    private fun getUpcomingPaymentAndTransactions(cardId: String) {

        viewModelScope.launch {
            val response = RetrofitClient.apiService.getUpcomingPayment()

            println(response)
            if (response.isSuccessful) {
                val upcomingPayment = response.body()
                buildList(upcomingPayment)
            }
        }
    }

    private fun buildList(upcomingPayment: UpcomingPayment?) {
        val items = mutableListOf<MyCardsListItem>()
        upcomingPayment?.let {
            items.add(MyCardsListItem.UpcomingPaymentItem(it))
        }
        items.add(MyCardsListItem.QuickAction())

        _ListItems.value = items
    }


}