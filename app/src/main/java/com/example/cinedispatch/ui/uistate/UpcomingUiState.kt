package com.example.cinedispatch.ui.uistate

import com.example.cinedispatch.model.movie.UpcomingMovie

sealed class UpcomingUiState {
    data class Success(val movies:List<UpcomingMovie>):UpcomingUiState()
    data class Error(val message:String):UpcomingUiState()
    data object Loading:UpcomingUiState()
}