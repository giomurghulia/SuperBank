package com.example.superbank.cards

sealed class MyCardsListItem(val viewType: ViewType) {
    enum class ViewType {
        UPCOMING_PAYMENT,
        QUICK_ACTION,
        TRANSACTIONS
    }

    data class UpcomingPaymentItem(
        val payment: UpcomingPayment
    ) : MyCardsListItem(ViewType.UPCOMING_PAYMENT)

    class QuickAction() : MyCardsListItem(ViewType.QUICK_ACTION)

//    data class Transactions(
//        val Transactions: List<Any>
//    ) : MyCardsListItem(ViewType.TRANSACTIONS)

}