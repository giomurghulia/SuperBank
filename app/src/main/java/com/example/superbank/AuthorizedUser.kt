package com.example.superbank

data class AuthorizedUser(
    val userId: String,
    val email: String,
    val mobile:String?,
    val fullName:String?,
    val image:String?,
)