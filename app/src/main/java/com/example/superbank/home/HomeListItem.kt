package com.example.superbank.home

import com.example.superbank.cards.Card
import com.example.superbank.transactions.adapters.models.CardType
import com.example.superbank.transactions.adapters.models.TransactionType

sealed class HomeListItem(val viewType: ViewType) {
    enum class ViewType {
        QUICK_ACTION,
        ITEM_HEADER,
        CARDS,
        TRANSACTIONS,
    }

    object QuickAction : HomeListItem(ViewType.QUICK_ACTION)

    data class ItemHeaderItem(
        val title: String,
        val action: HomeActionEnum
    ) : HomeListItem(ViewType.ITEM_HEADER)

    data class CardsItem(
        val card: Card
    ) : HomeListItem(ViewType.CARDS)

    data class TransactionsItem(
        val title: String,
        val type: TransactionType,
        val amount: Double,
        val cardLastDigits: String,
        val cardType: CardType,
        val description: String
    ) : HomeListItem(ViewType.TRANSACTIONS)

}