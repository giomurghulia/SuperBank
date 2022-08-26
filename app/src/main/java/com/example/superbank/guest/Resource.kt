package com.example.superbank.guest

sealed class Resource {
    data class Success(val unit: Unit) : Resource()
    data class Error(val errorMessage: String) : Resource()
}