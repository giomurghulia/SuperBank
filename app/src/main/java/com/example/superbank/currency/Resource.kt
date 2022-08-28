package com.example.superbank.currency

sealed class Resource {
    data class Success(val unit: Unit) : Resource()
    data class Error(val errorMessage: String) : Resource()
    object Loading : Resource()
}