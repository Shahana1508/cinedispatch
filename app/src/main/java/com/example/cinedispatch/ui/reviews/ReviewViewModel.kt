package com.example.cinedispatch.ui.reviews


import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinedispatch.api.NetworkResponse
import com.example.cinedispatch.local.Movie_entity
import com.example.cinedispatch.local.Review_entity
import com.example.cinedispatch.repo.MovieRepo
import com.example.cinedispatch.repo.RoomRepo
import com.example.cinedispatch.ui.uistate.MovieDetailsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(private val roomRepo: RoomRepo,val sharedPreferences: SharedPreferences,val repo:MovieRepo):ViewModel() {

    val reviewUiState = MutableLiveData<MovieDetailsUiState>()
    val is_fav=MutableLiveData<Boolean>()
    val ratingVal=MutableLiveData<Float>()
    fun get_details(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.get_movie_details(id).collect {
                when (it) {
                    is NetworkResponse.Success -> {
                        withContext(Dispatchers.Main) {
                            reviewUiState.value = MovieDetailsUiState.Success(it.data)
                        }
                    }

                    is NetworkResponse.Error -> {
                        withContext(Dispatchers.Main) {
                            reviewUiState.value = MovieDetailsUiState.Error(it.message.toString())
                        }
                    }

                    is NetworkResponse.Loading -> {
                        withContext(Dispatchers.Main) {
                            reviewUiState.value = MovieDetailsUiState.Loading
                        }
                    }
                }
            }
        }
    }
    fun updateRating(movie: Movie_entity){
        viewModelScope.launch(Dispatchers.IO) {
            val current=roomRepo.get_movie(movie.movieID)
            if(current!=null){
                current.rating=movie.rating?:0.0f
                roomRepo.update(current)
                withContext(Dispatchers.Main){
                    ratingVal.value=movie.rating?:0.0f
                }
            }else{
                roomRepo.addMovie(movie)
                withContext(Dispatchers.Main){
                    ratingVal.value=movie.rating?:0.0f
                }
            }
        }
    }
    fun getRating(movieId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            val current=roomRepo.get_movie(movieId)
            if(current!=null){
                current.rating?.let {
                    withContext(Dispatchers.Main) {
                        ratingVal.value = it
                    }
                }?:run {
                    withContext(Dispatchers.Main) {
                        ratingVal.value = 0.0f
                    }
                }
            }else{
                withContext(Dispatchers.Main){
                    ratingVal.value = 0.0f
                }
            }
        }
    }
    @SuppressLint("SuspiciousIndentation")
    fun isFavorite(movieId:Int){
        viewModelScope.launch(Dispatchers.IO) {
            val current=roomRepo.get_movie(movieId)
                if(current!=null){
                    withContext(Dispatchers.Main){
                    is_fav.value=current.isFav
                    }
                }else {
                    withContext(Dispatchers.Main){
                    is_fav.value=false}
                }
            }
        }

    fun toggleFav(movie: Movie_entity){
        viewModelScope.launch (Dispatchers.IO){
            val current=roomRepo.get_movie(movie.movieID)
            if(current!=null){
                if(current.isFav){
                    current.isFav=false
                    roomRepo.update(current)
                    withContext(Dispatchers.Main){
                        is_fav.value=false
                    }
                }else{
                    current.isFav=true
                    roomRepo.update(current)
                    withContext(Dispatchers.Main){
                        is_fav.value=true
                    }
                }
            }else{
                movie.isFav=true
                roomRepo.addMovie(movie)
                withContext(Dispatchers.Main){
                    is_fav.value=true
                }
            }
        }
    }
    fun addReview(reviewEntity: Review_entity,movieEntity: Movie_entity){
        viewModelScope.launch(Dispatchers.IO) {
            val movie = roomRepo.get_movie(movieEntity.movieID)
            if(movie!=null){
                roomRepo.addReview(reviewEntity)
            }else{
                roomRepo.addMovie(movie)
                roomRepo.addReview(reviewEntity)
            }
        }
    }
}

