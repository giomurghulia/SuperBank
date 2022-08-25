package com.example.superbank.transactions.adapters.models

import com.example.superbank.R

enum class TransactionType {
    GROCERY{
        override fun icon() = R.drawable.shopping
        override fun toString() = "Grocery"
    },
    CARD{
        override fun icon() = R.drawable.ic_card_icon
        override fun toString() = "Card"
    },
    TRANSFER{
        override fun icon() = R.drawable.trasfer
        override fun toString() = "Transfer"
    },
    BILL{
        override fun icon() = R.drawable.bill
        override fun toString() = "Bill"
    },
    SALARY{
        override fun icon() = R.drawable.salary
        override fun toString() = "Salary"
    };
    abstract fun icon(): Int
    companion object{
        fun stringToType(type: String) = when(type){
            "GROCERY" -> GROCERY
            "CARD" -> CARD
            "TRANSFER" -> TRANSFER
            "BILL" -> BILL
            "SALARY" -> SALARY
            else -> GROCERY
        }
    }
}