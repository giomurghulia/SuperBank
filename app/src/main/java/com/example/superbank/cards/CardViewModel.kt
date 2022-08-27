package com.example.superbank.cards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superbank.networking.RetrofitClient
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CardViewModel : ViewModel() {
    private var selectedCard: Int = 0

    private val _cards = MutableStateFlow<List<Card>>(emptyList())
    val cards get() = _cards.asStateFlow()

    private val _listItems = MutableStateFlow<List<CardDescriptionListItem>>(emptyList())
    val listItems get() = _listItems.asStateFlow()

    private val _alertDialog = MutableSharedFlow<String>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val alertDialog get() = _alertDialog.asSharedFlow()

    private val _action = MutableSharedFlow<Unit>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val action get() = _action.asSharedFlow()


    private var myCards = listOf<Card>()

    fun getCards() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getCards()

                println(response)
                if (response.isSuccessful) {
                    val cards = response.body()
                    _cards.value = cards ?: emptyList()

                    myCards = cards ?: emptyList()

                    if (!cards.isNullOrEmpty()) {
                        getUpcomingPaymentAndTransactions(selectedCard)
                    }
                }
            } catch (e: Exception) {
            }
        }
    }

    fun onCardSelected(cardPosition: Int) {
        getUpcomingPaymentAndTransactions(cardPosition)
        selectedCard = cardPosition
    }

    private fun getUpcomingPaymentAndTransactions(cardPosition: Int) {
        viewModelScope.launch {
            var upcomingPayment: UpcomingPayment? = null
            var transactions: List<CardTransactions>? = null
            try {
                if (cardPosition != 1) {
                    val responseUpcomingPayment =
                        RetrofitClient.apiService.getUpcomingPayment(myCards[cardPosition].uniqueId)

                    if (responseUpcomingPayment.isSuccessful) {
                        upcomingPayment = responseUpcomingPayment.body()
                    }
                }

                val responseTransactions =
                    RetrofitClient.apiService.getCardTransactions(myCards[cardPosition].uniqueId)

                if (responseTransactions.isSuccessful) {
                    transactions = responseTransactions.body()
                }

                buildListItem(upcomingPayment, transactions, CardDescriptionListItem.QuickAction)
            }catch (e: Exception){}
        }
    }

    private fun buildListItem(
        upcomingPayment: UpcomingPayment?,
        transactions: List<CardTransactions>?,
        quickAction: CardDescriptionListItem.QuickAction?
    ) {
        val items = mutableListOf<CardDescriptionListItem>()

        upcomingPayment?.let {
            items.add(CardDescriptionListItem.UpcomingPaymentItem(upcomingPayment))
        }

        quickAction?.let {
            items.add(quickAction)
        }

        transactions?.let {
            items.add(CardDescriptionListItem.ItemHeaderItem("Recent Transactions"))
        }
        transactions?.map { item ->
            items.add(CardDescriptionListItem.CardTransactionsItem(item))
        }

        _listItems.value = items
    }

    fun onItemClick(itemId: QuickActionEnum) {
        when (itemId) {
            QuickActionEnum.CARD_INFO -> {
                _alertDialog.tryEmit(myCards[selectedCard].iban)
            }
            QuickActionEnum.CHANGE_PIN -> {
                _alertDialog.tryEmit("You will receive new PIN wia sms")
            }
            QuickActionEnum.REMOVE_CARD -> {
                _alertDialog.tryEmit("Card will remove soon, You will receive sms")
            }
            QuickActionEnum.All_TRANSACTION -> {
                _action.tryEmit(Unit)
            }
        }
    }
}

