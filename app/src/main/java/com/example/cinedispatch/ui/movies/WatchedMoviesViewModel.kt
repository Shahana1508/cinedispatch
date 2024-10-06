package com.example.cinedispatch.ui.movies

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinedispatch.local.Movie_entity
import com.example.cinedispatch.repo.RoomRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class WatchedMoviesViewModel @Inject constructor(val roomRepo: RoomRepo):ViewModel() {
    val watchedMovies=MutableLiveData<List<Movie_entity>>()
    fun watchedMovies(){
        viewModelScope.launch(Dispatchers.IO) {
            val watched_movies=roomRepo.get_watched_movies()
            withContext(Dispatchers.Main){
                watchedMovies.value=watched_movies
            }
        }
    }

}