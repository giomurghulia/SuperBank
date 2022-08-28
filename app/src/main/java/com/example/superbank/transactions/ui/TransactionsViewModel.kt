package com.example.superbank.transactions.ui


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superbank.networking.RetrofitClient
import com.example.superbank.networking.responsestate.ResponseState
import com.example.superbank.transactions.DATE_FORMAT
import com.example.superbank.transactions.TODAY
import com.example.superbank.transactions.YESTERDAY
import com.example.superbank.transactions.adapters.models.OuterModel
import com.example.superbank.transactions.getmodel.TransactionsGetModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class TransactionsViewModel : ViewModel() {
    private var _transactionsListFlow: MutableStateFlow<ResponseState<List<TransactionsGetModel>>> = MutableStateFlow(ResponseState.Success(listOf()))
    val transactionsListFlow = _transactionsListFlow.asStateFlow()


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
        val formatter = SimpleDateFormat(DATE_FORMAT)
        for (i in currentList) {
            temp = when (i.time) {
                TODAY -> Calendar.getInstance().time.time
                YESTERDAY -> Calendar.getInstance().time.time - 86400000
                else -> {
                    formatter.parse(i.time).time
                }
            }
            if (temp < date.time + 86400000)
                break
            ind++
        }
        return currentList.subList(ind, currentList.size)
    }

    fun getTransactions() {
        viewModelScope.launch {
            getTransactionFlow().collect {
                _transactionsListFlow.value = it
            }
        }
    }

    private fun getTransactionFlow() = flow {
        emit(ResponseState.Load())
        try {
            val response = RetrofitClient.apiService.getAllTransactions()
            if (response.isSuccessful) {
                emit(ResponseState.Success(response.body() ?: listOf()))
            } else {
                emit(ResponseState.Error(response.errorBody().toString()))
            }

        } catch (e: Exception) {
            emit(ResponseState.Error(e.message.toString()))
        }
    }
}