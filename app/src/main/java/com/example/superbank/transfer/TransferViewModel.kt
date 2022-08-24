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

    val IBAN = "iban"
    val NUMBER = "number"

    private val _cards = MutableStateFlow<List<Card>>(emptyList())
    val cards get() = _cards.asStateFlow()

    private val _fields = MutableStateFlow<Resource>(Resource.Success(User("")))
    val fields get() = _fields.asStateFlow()

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
        _fields.value
    }

    fun getFields(address: String) {
        if (address.length == 6) {
            var checkIban = false

            if (!isInteger(address[0].toString())
                && !isInteger(address[1].toString())
                && isInteger(address.subSequence(2, 5).toString())
            ) {
                checkIban = true
            }

            if (checkIban) {
                viewModelScope.launch {
                    val response = RetrofitClient.apiService.getUserWithIban(address)

                    if (response.isSuccessful) {
                        val body = response.body()
                        _fields.value = Resource.Success(body ?: User(""))
                    } else {
                        val errorBody = response.errorBody()
                        _fields.value = Resource.Error(errorBody?.toString() ?: "User Did Not Find")
                    }
                }
            } else {
                _fields.value = Resource.Error("Incorrect IBAN")
            }

        } else if (address.length == 9) {
            if (isInteger(address)) {
                viewModelScope.launch {
                    val response = RetrofitClient.apiService.getUserWithNumber(address)

                    if (response.isSuccessful) {
                        val body = response.body()
                        _fields.value = Resource.Success(body ?: User(""))
                    } else {
                        val errorBody = response.errorBody()
                        _fields.value = Resource.Error(errorBody?.toString() ?: "User Did Not Find")
                    }
                }
            } else {
                _fields.value = Resource.Error("Incorrect Number")
            }
        } else if (address.length > 9) {
            _fields.value = Resource.Error("Incorrect Address")
        } else {
            _fields.value = Resource.Error("")
        }
    }

    private fun isInteger(str: String?) = str?.toIntOrNull()?.let { true } ?: false

}