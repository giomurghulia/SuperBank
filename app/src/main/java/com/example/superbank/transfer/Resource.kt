package com.example.superbank.transfer

sealed class Resource {
    data class Success(val user: User) : Resource()
    data class Error(val errorMessage: String) : Resource()

    data class SuccessTransfer(val Transaction: Transaction) : Resource()
    data class ErrorTransfer(val errorMessage: String) : Resource()
}