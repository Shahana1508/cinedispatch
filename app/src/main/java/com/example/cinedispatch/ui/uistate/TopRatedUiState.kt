package com.example.cinedispatch.ui.uistate

import com.example.cinedispatch.model.movie.TopRatedMovie

sealed class TopRatedUiState {
    data class Success(val movies:List<TopRatedMovie>): TopRatedUiState()
    data class Error(val message:String?): TopRatedUiState()
    data object Loading: TopRatedUiState()
}