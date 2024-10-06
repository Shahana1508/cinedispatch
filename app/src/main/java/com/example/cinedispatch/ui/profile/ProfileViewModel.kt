package com.example.cinedispatch.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinedispatch.local.MovieWithReviews
import com.example.cinedispatch.local.Movie_entity
import com.example.cinedispatch.repo.RoomRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(val roomRepo: RoomRepo):ViewModel(){
    val totalMovies=MutableLiveData<Int>()
    val duration=MutableLiveData<String>()
    val moviesList=MutableLiveData<List<Movie_entity>>()
    val reviewsVal=MutableLiveData<List<MovieWithReviews>>()
    val watchedMovies=MutableLiveData<List<Movie_entity>>()
    fun getTotalMovies(){
        viewModelScope.launch (Dispatchers.IO){
            val total=roomRepo.get_watched_movies_count()
            withContext(Dispatchers.Main){
                totalMovies.value=total
            }
        }
    }
    fun getDuration(){
        viewModelScope.launch (Dispatchers.IO){
            val minutes=roomRepo.get_total_watched_duration()
            withContext(Dispatchers.Main){
                duration.value=formatDuration(minutes)

            }
        }
    }
    fun formatDuration(minutes:Int):String{
        val hours=minutes/60
        val minute=minutes%60
        return "${hours}h${minute}m"
    }
    fun getFavMovies(){
        viewModelScope.launch(Dispatchers.IO) {
            val movies=roomRepo.get_fav_movies()
            withContext(Dispatchers.Main){
                moviesList.value=movies
            }
        }
    }
    fun getRecentWatched(){
        viewModelScope.launch(Dispatchers.IO){
            val movies=roomRepo.get_watched_movies()
            withContext(Dispatchers.Main){
                watchedMovies.value=movies
            }
        }
    }
    fun deleteReview(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            roomRepo.delete_review(id)
        }
    }
    fun getReviews(){
        viewModelScope.launch(Dispatchers.IO) {
            val reviews=roomRepo.get_reviews_with_movies()
            val reviewList= arrayListOf<MovieWithReviews>()
            for(review in reviews){
                val movie=roomRepo.get_movie(review.movieID)
                reviewList.add(MovieWithReviews(movie,review))
            }
            withContext(Dispatchers.Main){
                reviewsVal.value=reviewList
            }
        }
    }
}