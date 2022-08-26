package com.example.superbank.transactions.adapters.models

import com.example.superbank.R

enum class TransactionType {
    GROCERY{
        override fun icon() = R.drawable.shopping
        override val backgroundTint: Int
            get() = R.color.grocery_tint_color
        override fun toString() = "Grocery"
    },
    CARD{
        override fun icon() = R.drawable.ic_card_icon
        override val backgroundTint: Int
            get() = R.color.card_tint_color
        override fun toString() = "Card"
    },
    TRANSFER{
        override fun icon() = R.drawable.trasfer
        override val backgroundTint: Int
            get() = R.color.transfer_tint_color
        override fun toString() = "Transfer"
    },
    BILL{
        override fun icon() = R.drawable.bill
        override val backgroundTint: Int
            get() = R.color.bill_tint_color
        override fun toString() = "Bill"
    },
    SALARY{
        override fun icon() = R.drawable.salary
        override val backgroundTint: Int
            get() = R.color.salary_tint_color
        override fun toString() = "Salary"
    };
    abstract fun icon(): Int
    abstract val backgroundTint: Int
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