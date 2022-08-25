package com.example.superbank.transfer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superbank.cards.Card
import com.example.superbank.networking.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TransferViewModel : ViewModel() {
    private var selectedCard: Int = 0

    private val _cards = MutableStateFlow<List<Card>>(emptyList())
    val cards get() = _cards.asStateFlow()

    private val _transfer = MutableStateFlow<Resource>(Resource.Error(""))
    val transfer get() = _transfer.asStateFlow()

    private var myCards = listOf<Card>()

    fun onCardSelected(cardPosition: Int) {
        selectedCard = cardPosition
    }

    fun getCards() {
        viewModelScope.launch {
            val response = RetrofitClient.apiService.getCards()

            println(response)
            if (response.isSuccessful) {
                val cards = response.body()
                _cards.value = cards ?: emptyList()

                myCards = cards ?: emptyList()
            }
        }
        _transfer.value
    }

    fun getFields(address: String) {
        if (address.length == 6 && checkIban(address)) {
            getUserWithIban(address)
        } else if (address.length == 9 && isInteger(address)) {
            getUserWithMobile(address)
        } else if (address.length > 9) {
            _transfer.value = Resource.Error("Incorrect Address")
        } else {
            _transfer.value = Resource.Error("")
        }
    }

    private fun isInteger(str: String?) = str?.toIntOrNull()?.let { true } ?: false

    private fun checkIban(iban: String): Boolean {
        if (!isInteger(iban[0].toString())
            && !isInteger(iban[1].toString())
            && isInteger(iban.substring(2, 6))
        ) {
            return true
        }
        return false
    }

    private fun getUserWithIban(iban: String) {
        viewModelScope.launch {
            val response = RetrofitClient.apiService.getUserWithIban(iban)

            if (response.isSuccessful) {
                val body = response.body()
                _transfer.value = Resource.Success(body ?: User(""))
            } else {
                val errorBody = response.errorBody()
                _transfer.value = Resource.Error(errorBody?.toString() ?: "User Did Not Find")
            }
        }
    }

    private fun getUserWithMobile(mobile: String) {
        viewModelScope.launch {
            val response = RetrofitClient.apiService.getUserWithNumber(mobile)

            if (response.isSuccessful) {
                val body = response.body()
                _transfer.value = Resource.Success(body ?: User(""))
            } else {
                val errorBody = response.errorBody()
                _transfer.value = Resource.Error(errorBody?.toString() ?: "User Did Not Find")
            }
        }
    }

    fun makeTransfer(address: String, amount: Double) {
        if (_transfer.value is Resource.Success) {
            viewModelScope.launch {
                val transaction = Transaction(myCards[selectedCard].uniqueId, address, amount)

                val response = RetrofitClient.apiService.makeTransfer(transaction)

                if (selectedCard != 1) {
                    _transfer.value = Resource.SuccessTransfer(transaction)
                } else {
                    _transfer.value = Resource.ErrorTransfer("Transfer Problem")
                }
            }
        }
        else{
            _transfer.value = Resource.Error("User Did Not Find")
        }
    }
}