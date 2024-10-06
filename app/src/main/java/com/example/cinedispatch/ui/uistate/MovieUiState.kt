package com.example.cinedispatch.ui.uistate

import com.example.cinedispatch.model.movie.PopularMovie

sealed class MovieUiState {
    data class Success(val movies:List<PopularMovie>): MovieUiState()
    data class Error(val message:String?): MovieUiState()
    data object Loading: MovieUiState()
}
