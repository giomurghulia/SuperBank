package com.example.superbank.transfer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superbank.networking.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TransferViewModel : ViewModel() {
    val IBAN = "iban"
    val NUMBER = "number"

    private val _fields = MutableStateFlow<Resource>(Resource.Success(User("")))
    val fields get() = _fields.asStateFlow()

    fun getFields( address: String) {
        if (address.length == 6) {
            viewModelScope.launch {
                val response = RetrofitClient.apiService.getUserWithIban(address)

                if (response.isSuccessful) {
                    val body = response.body()
                    _fields.value = Resource.Success(body ?: User(""))
                } else {
                    val errorBody = response.errorBody()
                    _fields.value = Resource.Error(errorBody?.toString() ?: "")
                }
            }
        } else if (address.length == 9) {
            viewModelScope.launch {
                val response = RetrofitClient.apiService.getUserWithNumber(address)

                if (response.isSuccessful) {
                    val body = response.body()
                    _fields.value = Resource.Success(body ?: User(""))
                } else {
                    val errorBody = response.errorBody()
                    _fields.value = Resource.Error(errorBody?.toString() ?: "")
                }
            }
        } else {
            _fields.value = Resource.Loading
        }

    }
}