package com.example.superbank.offers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superbank.networking.RetrofitClient
import com.example.superbank.networking.responsestate.ResponseState
import com.example.superbank.offers.adapter.OfferModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class OffersViewModel : ViewModel() {
    private var _requestFlow = MutableStateFlow<ResponseState<List<OfferModel>>>(
        ResponseState.Success<List<OfferModel>>(listOf())
    )
    val requestState
        get() = _requestFlow.asStateFlow()

    fun getOffers() {
        viewModelScope.launch {
            getOffersFlow().collect {
                _requestFlow.value = it
            }
        }
    }

    private fun getOffersFlow() = flow {
        emit(ResponseState.Load())
        try {
            val response = RetrofitClient.apiService.getOffers()
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