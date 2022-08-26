package com.example.superbank

data class AuthorizedUser(
    val uniqueId: String,
    val email:String,
    val mobile:String,
    val fullName:String,
    val avatar:String,
)