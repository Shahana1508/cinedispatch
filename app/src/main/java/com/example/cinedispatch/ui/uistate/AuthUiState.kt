package com.example.cinedispatch.ui.uistate

sealed class AuthUiState {
    data class Success(val login:Boolean): AuthUiState()
    data object Loading: AuthUiState()
    data class Error(val message:String): AuthUiState()
}