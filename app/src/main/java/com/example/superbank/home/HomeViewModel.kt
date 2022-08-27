package com.example.superbank.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superbank.cards.Card
import com.example.superbank.cards.CardDescriptionListItem
import com.example.superbank.networking.RetrofitClient
import com.example.superbank.transactions.getmodel.TransactionsGetModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _listItems = MutableStateFlow<List<HomeListItem>>(emptyList())
    val listItems get() = _listItems.asStateFlow()

    private val _action = MutableSharedFlow<HomeActionEnum>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val action get() = _action.asSharedFlow()

    fun getTransactionsAndCard() {
        viewModelScope.launch {
            var cards: List<Card>? = null
            var transactions: List<TransactionsGetModel>? = null

            val responseTransactions = RetrofitClient.apiService.getTransactions()

            if (responseTransactions.isSuccessful) {
                transactions = responseTransactions.body()
            }

            val response = RetrofitClient.apiService.getCards()

            if (response.isSuccessful) {
                cards = response.body()
            }

            buildListItem(cards, transactions)
        }
    }

    private fun buildListItem(
        cards: List<Card>?,
        transactions: List<TransactionsGetModel>?,
    ) {
        val items = mutableListOf<HomeListItem>()

        items.add(HomeListItem.QuickAction)
        cards?.let {
            items.add(HomeListItem.ItemHeaderItem("My Cards", HomeActionEnum.ALL_CARD))
            cards.map {
                items.add(HomeListItem.CardsItem(it))
            }
        }


        transactions?.let {
            items.add(
                HomeListItem.ItemHeaderItem(
                    "Recent Transactions",
                    HomeActionEnum.ALL_TRANSACTION
                )
            )

            transactions.map { item ->
                items.add(HomeListItem.TransactionsItem(item))
            }
        }
        _listItems.value = items
    }

    fun homeAction(action:HomeActionEnum){
        when (action) {
            HomeActionEnum.TRANSFER -> { _action.tryEmit(HomeActionEnum.TRANSFER) }
            HomeActionEnum.OFFER -> { _action.tryEmit(HomeActionEnum.OFFER) }
            HomeActionEnum.CURRENCY -> { _action.tryEmit(HomeActionEnum.CURRENCY) }
            HomeActionEnum.ALL_CARD -> { _action.tryEmit(HomeActionEnum.ALL_CARD) }
            HomeActionEnum.ALL_TRANSACTION -> { _action.tryEmit(HomeActionEnum.ALL_TRANSACTION) }
        }
    }
}