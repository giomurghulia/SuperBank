package com.example.superbank.cards

import com.example.superbank.transactions.adapters.models.TransactionType

sealed class CardDescriptionListItem(val viewType: ViewType) {
    enum class ViewType {
        UPCOMING_PAYMENT,
        QUICK_ACTION,
        CARD_TRANSACTIONS,
        ITEM_HEADER
    }

    data class UpcomingPaymentItem(
        val payment: UpcomingPayment
    ) : CardDescriptionListItem(ViewType.UPCOMING_PAYMENT)

    object QuickAction : CardDescriptionListItem(ViewType.QUICK_ACTION)

    data class CardTransactionsItem(
        val amount: Double,
        val type: TransactionType,
        val date: String,
    ) : CardDescriptionListItem(ViewType.CARD_TRANSACTIONS)

    data class ItemHeaderItem(
        val title: String
    ) : CardDescriptionListItem(ViewType.ITEM_HEADER)
}