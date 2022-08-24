package com.example.superbank.transactions.adapters.models

import com.example.superbank.R

enum class TransactionType {
    GROCERY{
        override fun icon() = R.drawable.shopping

    },
    CARD{
        override fun icon() = R.drawable.ic_card_icon
    },
    TRANSFER{
        override fun icon() = R.drawable.trasfer
    },
    BILL{
        override fun icon() = R.drawable.bill
    },
    SALARY{
        override fun icon() = R.drawable.salary
    };
    abstract fun icon(): Int
}