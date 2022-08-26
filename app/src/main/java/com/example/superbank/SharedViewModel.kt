package com.example.superbank

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superbank.networking.RetrofitClient
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SharedViewModel : ViewModel() {
    private val _authorizedUserData = MutableStateFlow<AuthorizedUser?>(null)
    val authorizedUserData get() = _authorizedUserData.asStateFlow()

    private val _logOutUser = MutableSharedFlow<Unit>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val logOutUser get() = _logOutUser.asSharedFlow()


    fun checkAuthorizedUser() {
        Firebase.auth.currentUser?.reload()?.addOnCompleteListener{
            val currentUser = Firebase.auth.currentUser

            if (currentUser == null) {
                logOutUser()
            }
        }
    }

    fun logOutUser() {
        _logOutUser.tryEmit(Unit)
        _authorizedUserData.value = null
    }

    fun getAuthorizedUserDate() {
        viewModelScope.launch {
            val response = RetrofitClient.apiService.getAuthorizedUserDate()
            val user = response.body()

            val authorizedUser = Firebase.auth.currentUser?.email?.let { user?.copy(email = it) }

            _authorizedUserData.value = authorizedUser
        }
    }
}