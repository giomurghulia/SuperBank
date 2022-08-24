package com.example.superbank

import com.example.superbank.transactions.adapters.models.InnerModel
import com.example.superbank.transactions.adapters.models.OuterModel
import com.example.superbank.transactions.adapters.models.TransactionType

val list = listOf(
    OuterModel(
        "Today",
        listOf(
            InnerModel("Grocerfuy", TransactionType.TRANSFER, 200.0),
            InnerModel("Grocery", TransactionType.TRANSFER, 200.0),
            InnerModel("Grocery", TransactionType.GROCERY, 200.0)
        )
    ),
    OuterModel(
        "Yesterday",
        listOf(
            InnerModel("Grocery", TransactionType.BILL, -200.0),
            InnerModel("Grocery", TransactionType.SALARY, 200.0),
            InnerModel("Grocery", TransactionType.CARD, 200.0)
        )
    ),
    OuterModel(
        "12.3.2022",
        listOf(
            InnerModel("Not Grocery", TransactionType.GROCERY, 200.0),
            InnerModel("Grocery", TransactionType.TRANSFER, -200.0),
            InnerModel("Grocery", TransactionType.CARD, -200.0)
        )
    ),
    OuterModel(
        "10.3.2022",
        listOf(
            InnerModel("Grocery", TransactionType.SALARY, 200.0),
            InnerModel("Grocery", TransactionType.GROCERY, -200.0),
            InnerModel("Grocery", TransactionType.CARD, -200.0)
        )
    )
)