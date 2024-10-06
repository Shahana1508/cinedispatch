package com.example.cinedispatch.ui.uistate

import com.example.cinedispatch.model.review.Review

sealed class MovieReviewsUiState {
    data class Success(val reviews:List<Review>):MovieReviewsUiState()
    data class Error(val message:String):MovieReviewsUiState()
    data object Loading:MovieReviewsUiState()
}