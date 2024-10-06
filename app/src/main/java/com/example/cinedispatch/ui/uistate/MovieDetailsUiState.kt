package com.example.cinedispatch.ui.uistate

import com.example.cinedispatch.model.movie.MovieDetailResponse

sealed class MovieDetailsUiState {
    data class Success(val moviedetails: MovieDetailResponse):MovieDetailsUiState()
    data class Error(val message:String):MovieDetailsUiState()
    data object Loading:MovieDetailsUiState()
}