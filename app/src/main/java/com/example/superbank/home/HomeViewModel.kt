package com.example.superbank.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superbank.cards.Card
import com.example.superbank.networking.RetrofitClient
import com.example.superbank.transactions.adapters.models.CardType
import com.example.superbank.transactions.adapters.models.TransactionType
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

    private val _balance = MutableStateFlow("0.00")
    val balance get() = _balance.asStateFlow()

    private val _action = MutableSharedFlow<HomeActionEnum>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val action get() = _action.asSharedFlow()

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
                items.add(
                    HomeListItem.TransactionsItem(
                        item.title,
                        changeTypeToEnum(item.type),
                        item.amount,
                        item.cardLastDigit,
                        CardType.stringToType(item.type),
                        item.description
                    )
                )
            }
        }
        _listItems.value = items
    }

    private fun setBalance(cards: List<Card>?) {
        var balance: Double = 0.0
        cards?.forEach {
            balance += it.cardBalance
        }

        _balance.tryEmit(balance.toString())
    }

    private fun changeTypeToEnum(type: String): TransactionType {
        return when (type) {
            "GROCERY" -> TransactionType.GROCERY
            "CARD" -> TransactionType.CARD
            "TRANSFER" -> TransactionType.TRANSFER
            "BILL" -> TransactionType.BILL
            "SALARY" -> TransactionType.SALARY
            else -> TransactionType.GROCERY
        }
    }

    fun getTransactionsAndCard() {
        viewModelScope.launch {
            var cards: List<Card>? = null
            var transactions: List<TransactionsGetModel>? = null

            val responseTransactions = RetrofitClient.apiService.getAllTransactions()

            if (responseTransactions.isSuccessful) {
                transactions = responseTransactions.body()
            }

            val response = RetrofitClient.apiService.getCards()

            if (response.isSuccessful) {
                cards = response.body()
                setBalance(cards)
            }

            buildListItem(cards, transactions)
        }
    }


    fun homeAction(action: HomeActionEnum) {
        when (action) {
            HomeActionEnum.TRANSFER -> {
                _action.tryEmit(HomeActionEnum.TRANSFER)
            }
            HomeActionEnum.OFFER -> {
                _action.tryEmit(HomeActionEnum.OFFER)
            }
            HomeActionEnum.CURRENCY -> {
                _action.tryEmit(HomeActionEnum.CURRENCY)
            }
            else -> {}
        }
    }
}