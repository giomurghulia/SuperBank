package com.example.superbank.authorized

import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.example.superbank.AuthorizedUser
import com.example.superbank.cards.CardDescriptionListItem
import com.example.superbank.networking.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class AuthorizedUserViewModel : ViewModel() {

    private val _authorizedUserData = MutableStateFlow<AuthorizedUser?>(null)
    val authorizedUserData get() = _authorizedUserData.asStateFlow()

    fun logInUser(user: AuthorizedUser) {
        _authorizedUserData.value = user
    }

    fun checkUser() {
        if (RetrofitClient.token.equals(null)) {
            _authorizedUserData.value = null
        }
    }
}