package com.example.superbank.transactions.getmodel

import com.example.superbank.transactions.adapters.models.InnerModel
import com.example.superbank.transactions.adapters.models.OuterModel
import com.example.superbank.transactions.adapters.models.TransactionType

data class TransactionsGetModel(
    val title: String,
    val type: String,
    val amount: Double,
    val date: String,
    val cardLastDigit: String,
    val description: String
)

fun List<TransactionsGetModel>.toOuterList(): MutableList<OuterModel> {
    val list = mutableListOf<OuterModel>()
    this.forEach {
        if (list.lastIndex == -1 || list[list.lastIndex].time != it.date)
            list.add(
                OuterModel(
                    it.date, mutableListOf(
                        InnerModel(
                            it.title, TransactionType.stringToType(it.type),
                            it.amount, it.cardLastDigit, it.description
                        )
                    )
                )
            )
        else
            (list[list.lastIndex].transactions as MutableList).add(
                InnerModel(
                    it.title, TransactionType.stringToType(it.type),
                    it.amount, it.cardLastDigit, it.description
                )
            )
    }
    return list
}
