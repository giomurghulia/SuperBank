package com.example.superbank.transactions.ui


import androidx.lifecycle.ViewModel
import com.example.superbank.transactions.DATE_FORMAT
import com.example.superbank.transactions.TODAY
import com.example.superbank.transactions.YESTERDAY
import com.example.superbank.transactions.adapters.models.OuterModel
import java.text.SimpleDateFormat
import java.util.*



class TransactionsViewModel : ViewModel() {
    fun incomeList(currentList: List<OuterModel>): List<OuterModel> {
        val newList: MutableList<OuterModel> = mutableListOf()
        for (i in currentList) {
            newList.add(i.copy(transactions = i.transactions.filter { it.amount > 0 }))
        }
        return newList.filter { it.transactions.isNotEmpty() }
    }

    fun expenseList(currentList: List<OuterModel>): List<OuterModel> {
        val newList: MutableList<OuterModel> = mutableListOf()
        for (i in currentList) {
            newList.add(i.copy(transactions = i.transactions.filter { it.amount < 0 }))
        }
        return newList.filter { it.transactions.isNotEmpty() }
    }

    fun timeFilter(currentList: List<OuterModel>, date: Date): List<OuterModel> {
        var ind = 0
        var temp: Long
        var lst: List<String>
        val formatter = SimpleDateFormat(DATE_FORMAT)
        for(i in currentList){
            when(i.time){
                TODAY-> temp = Calendar.getInstance().time.time
                YESTERDAY-> temp = Calendar.getInstance().time.time - 86400000
                else -> {
                    temp = formatter.parse(i.time).time
                }
            }
            if(temp < date.time + 86400000)
                break
            ind++
        }
        return currentList.subList(ind, currentList.size)
    }

}