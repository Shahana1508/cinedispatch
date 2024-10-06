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
class WatchlistViewModel @Inject constructor(val repo:RoomRepo):ViewModel(){
    val watchlisted= MutableLiveData<List<Movie_entity>>()

    fun get_watchlist_movies(){
        viewModelScope.launch(Dispatchers.IO) {
            val movies=repo.get_movies_from_watchlist()
            withContext(Dispatchers.Main){
                watchlisted.value=movies
            }
        }
    }

}