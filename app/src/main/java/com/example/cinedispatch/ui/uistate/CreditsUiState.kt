package com.example.cinedispatch.ui.uistate

import com.example.cinedispatch.model.credits.CreditsResponse

sealed class CreditsUiState {
    data object Loading:CreditsUiState()
    data class Success(val credits: CreditsResponse):CreditsUiState()
    data class Error(val message:String):CreditsUiState()
}