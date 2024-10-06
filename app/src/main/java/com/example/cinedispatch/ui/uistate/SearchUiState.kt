package com.example.cinedispatch.ui.uistate

import com.example.cinedispatch.model.movie.SearchMovie

sealed class SearchUiState {
    data class Success(val search:List<SearchMovie>):SearchUiState()
    data class Error(val message:String):SearchUiState()
    data object Loading:SearchUiState()

}