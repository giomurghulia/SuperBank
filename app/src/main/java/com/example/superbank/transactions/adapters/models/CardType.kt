package com.example.superbank.transactions.adapters.models

import com.example.superbank.R

enum class CardType {
    VISA {
        override val icon: Int
            get() = R.drawable.ic_visa

        override fun toString() = "VISA"
    },
    MASTERCARD {
        override val icon: Int
            get() = R.drawable.ic_mastercard

        override fun toString() = "MASTERCARD"
    },
    DEFAULT {
        override val icon: Int
            get() = R.drawable.ic_visa

        override fun toString() = "VISA"
    };

    abstract val icon: Int

    companion object {
        fun stringToType(type: String) = when (type) {
            "VISA" -> VISA
            "Mastercard" -> MASTERCARD
            else -> DEFAULT
        }
    }
}