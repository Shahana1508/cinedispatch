package com.example.cinedispatch.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinedispatch.api.NetworkResponse
import com.example.cinedispatch.repo.MovieRepo
import com.example.cinedispatch.ui.uistate.MovieUiState
import com.example.cinedispatch.ui.uistate.TopRatedUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val repo:MovieRepo):ViewModel() {
    val uiState=MutableLiveData<MovieUiState>()
    val uiState2=MutableLiveData<TopRatedUiState>()
    fun getMovies(){
        viewModelScope.launch(Dispatchers.IO) {
            repo.get_popular_movies().collect(){ state ->
                when(state){
                    is NetworkResponse.Error->{
                        withContext(Dispatchers.Main){
                            uiState.value= MovieUiState.Error(state.message.toString())
                        }
                    }
                    is NetworkResponse.Success->{
                        withContext(Dispatchers.Main){
                            state.data.results?.let {
                                uiState.value= MovieUiState.Success(it)
                            }
                        }
                    }
                    is NetworkResponse.Loading->{
                        withContext(Dispatchers.Main){
                            uiState.value= MovieUiState.Loading
                        }
                    }
                }
            }
        }
    }
    fun getTopRated(){
        viewModelScope.launch(Dispatchers.IO){
            repo.get_top_rated().collect(){state->
                when(state){
                    is NetworkResponse.Error->{
                        withContext(Dispatchers.Main){
                            uiState2.value= TopRatedUiState.Error(state.message.toString())
                        }
                    }
                    is NetworkResponse.Loading->{
                        withContext(Dispatchers.Main){
                            uiState2.value= TopRatedUiState.Loading
                        }
                    }
                    is NetworkResponse.Success->{
                        withContext(Dispatchers.Main){
                            state.data.results?.let {
                                uiState2.value= TopRatedUiState.Success(it)
                            }
                        }
                    }
                }
            }
        }
    }
}