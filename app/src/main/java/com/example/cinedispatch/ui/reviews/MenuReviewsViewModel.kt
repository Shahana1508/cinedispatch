package com.example.cinedispatch.ui.reviews

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinedispatch.local.MovieWithReviews
import com.example.cinedispatch.repo.RoomRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MenuReviewsViewModel @Inject constructor(val roomRepo: RoomRepo):ViewModel() {
    val reviewsVal= MutableLiveData<List<MovieWithReviews>>()
    fun getReviews(){
        viewModelScope.launch(Dispatchers.IO) {
            val reviews=roomRepo.get_reviews_with_movies()
            val withReviews= arrayListOf<MovieWithReviews>()
            for(review in reviews){
                val movie=roomRepo.get_movie(review.movieID)
                withReviews.add(MovieWithReviews(movie,review))
            }
            withContext(Dispatchers.Main){
                reviewsVal.value=withReviews
            }
        }
    }
}