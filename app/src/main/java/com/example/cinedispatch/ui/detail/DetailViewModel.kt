package com.example.cinedispatch.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinedispatch.api.NetworkResponse
import com.example.cinedispatch.local.Movie_entity
import com.example.cinedispatch.model.credits.Cast
import com.example.cinedispatch.model.credits.Crew
import com.example.cinedispatch.repo.MovieRepo
import com.example.cinedispatch.repo.RoomRepo
import com.example.cinedispatch.ui.uistate.CreditsUiState
import com.example.cinedispatch.ui.uistate.MovieDetailsUiState
import com.example.cinedispatch.ui.uistate.MovieReviewsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(val repo:MovieRepo,val roomRepo: RoomRepo):ViewModel() {
    val movieDetailUiState=MutableLiveData<MovieDetailsUiState>()
    val creditsUiState=MutableLiveData<CreditsUiState>()
    val noreviews=MutableLiveData<Boolean>()
    val showSnackbar=MutableLiveData<Boolean>(false)
    val casts=MutableLiveData<List<Cast>>()
    val crew=MutableLiveData<List<Crew>>()
    val reviewsUiState=MutableLiveData<MovieReviewsUiState>()
    val isWatched=MutableLiveData<Boolean>(false)
    val showAll=MutableLiveData<Boolean>(false)
    val seeAll=MutableLiveData<String>()
    val isWatchlistedMovie=MutableLiveData<Boolean>()

    fun getMovieDetail(id:Int){
        viewModelScope.launch (Dispatchers.IO){
            repo.get_movie_details(id).collect{state->
                when(state){
                    is NetworkResponse.Success->{
                        state.data.let{
                            withContext(Dispatchers.Main){
                                movieDetailUiState.value=MovieDetailsUiState.Success(it)
                            }
                        }
                    }
                    is NetworkResponse.Error->{
                        withContext(Dispatchers.Main){
                            movieDetailUiState.value=MovieDetailsUiState.Error(state.message.toString())
                        }
                    }
                    is NetworkResponse.Loading->{
                        withContext(Dispatchers.Main){
                            movieDetailUiState.value=MovieDetailsUiState.Loading
                        }
                    }
                }
            }
        }
    }
    fun getMovieCredits(id:Int,isCast:Boolean){
        viewModelScope.launch(Dispatchers.IO){
            repo.get_movie_credits(id).collect{state->
                when(state){
                    is NetworkResponse.Loading->{
                        withContext(Dispatchers.Main){
                            creditsUiState.value=CreditsUiState.Loading
                        }
                    }
                    is NetworkResponse.Error->{
                        withContext(Dispatchers.Main){
                            creditsUiState.value=CreditsUiState.Error(state.message.toString())
                        }
                    }
                    is NetworkResponse.Success->{
                        state.data.let {
                            withContext(Dispatchers.Main){
                                creditsUiState.value=CreditsUiState.Success(it)
                                if(isCast){
                                    casts.value=it.cast
                                }else{
                                    crew.value=it.crew
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    fun is_watched(movie_id: Int){
        viewModelScope.launch {
            val movie=roomRepo.get_movie(movie_id)
            withContext(Dispatchers.Main){
                if(movie!=null){
                    isWatched.value=movie.isWatched
                }else{
                    isWatched.value=false
                }
            }
        }
    }
    fun resetShowSnackbar() {
        showSnackbar.value = false
    }
    fun toggle_watched(movie: Movie_entity){
        viewModelScope.launch(Dispatchers.IO){
            val current_movie=roomRepo.get_movie(movie.movieID)
            if(current_movie!=null){
                if(current_movie.isWatched){
                    current_movie.isWatched=false
                    roomRepo.update(current_movie)
                    withContext(Dispatchers.Main){
                        isWatched.value=false
                    }
                }else{
                    current_movie.isWatchListed=false
                    current_movie.isWatched=true
                    roomRepo.update(current_movie)
                    withContext(Dispatchers.Main){
                        showSnackbar.value=true
                        isWatched.value=true
                        isWatchlistedMovie.value=false
                    }
                }
            }else{
                movie.isWatched=true
                roomRepo.addMovie(movie)
                withContext(Dispatchers.Main){
                    showSnackbar.value=true
                    isWatched.value=true
                }
            }
        }
    }
    fun is_in_watchlist(movie_id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val movie = roomRepo.get_movie(movie_id)
            withContext(Dispatchers.Main) {
                if (movie != null) {
                    isWatchlistedMovie.value = movie.isWatchListed
                } else {
                    isWatchlistedMovie.value = false
                }
            }
        }
    }
    fun toggle_watchlist(movie:Movie_entity){
        viewModelScope.launch(Dispatchers.IO) {
            val currentMovie=roomRepo.get_movie(movie.movieID)
            if(currentMovie!=null){
                if(currentMovie.isWatchListed){
                    currentMovie.isWatchListed=false
                    roomRepo.update(currentMovie)
                    withContext(Dispatchers.Main){
                        isWatchlistedMovie.value=false
                    }
                }else{
                    currentMovie.isWatchListed=true
                    roomRepo.update(currentMovie)
                    withContext(Dispatchers.Main){
                        isWatchlistedMovie.value=true
                    }
                }
            }else{
                movie.isWatchListed = true
                roomRepo.addMovie(movie)
                withContext(Dispatchers.Main) {
                    isWatchlistedMovie.value = true
                }
            }
        }
    }
    fun show(){
        val current=showAll.value?:false
        showAll.value =!current
        if (showAll.value==true) {
            seeAll.value="See less"
        } else {
            seeAll.value="See all"
        }

    }
    fun formatDate(dateString: String): String {
        // Define the input format (the format you receive from the API)
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        // Define the output format (the format you want to display)
        val outputFormat = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())

        return try {
            // Parse the input date string to a Date object
            val date = inputFormat.parse(dateString)

            // Format the Date object to the desired output format
            outputFormat.format(date!!)
        } catch (e: Exception) {
            // Handle parsing or formatting errors
            "Invalid Date"
        }
    }
    fun getMovieReviews(id:Int){
        viewModelScope.launch(Dispatchers.IO){
            repo.get_movie_reviews(id).collect{state->
                when(state){
                    is NetworkResponse.Loading->{
                        withContext(Dispatchers.Main){
                            reviewsUiState.value=MovieReviewsUiState.Loading
                        }
                    }
                    is NetworkResponse.Error->{
                        withContext(Dispatchers.Main){
                            reviewsUiState.value=MovieReviewsUiState.Error(state.message.toString())
                        }
                    }
                    is NetworkResponse.Success->{
                        state.data.results?.let {
                            if(it.isNotEmpty()){
                                withContext(Dispatchers.Main){
                                    reviewsUiState.value=MovieReviewsUiState.Success(it)
                                }
                            }else{
                                withContext(Dispatchers.Main){
                                    noreviews.value=true

                                }
                            }
                        } ?:run{
                            withContext(Dispatchers.Main){
                                noreviews.value=true
                            }
                        }
                    }
                }
            }
        }

    }
}