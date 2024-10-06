package com.example.cinedispatch.ui.uistate

import com.example.cinedispatch.model.PersonDetailsResponse

sealed class BottomSheetUiState {
    data object Loading:BottomSheetUiState()
    data class Success(val details:PersonDetailsResponse):BottomSheetUiState()
    data class Error(val message:String):BottomSheetUiState()
}