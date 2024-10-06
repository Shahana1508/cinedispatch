package com.example.cinedispatch.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinedispatch.api.NetworkResponse
import com.example.cinedispatch.repo.MovieRepo
import com.example.cinedispatch.ui.uistate.SearchUiState
import com.example.cinedispatch.ui.uistate.UpcomingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(val repo:MovieRepo):ViewModel() {
    val upcomingUiState=MutableLiveData<UpcomingUiState>()
    val searchUiState=MutableLiveData<SearchUiState>()
    val showText=MutableLiveData<Boolean>()
    fun getUpcomingMovies(){
        viewModelScope.launch(Dispatchers.IO){
            repo.get_upcoming().collect{state->
                when(state){
                    is NetworkResponse.Success->{
                        state.data.results?.let {
                            withContext(Dispatchers.Main){
                                upcomingUiState.value=UpcomingUiState.Success(it)
                            }
                        }
                    }
                    is NetworkResponse.Error->{
                        withContext(Dispatchers.Main) {
                            upcomingUiState.value = UpcomingUiState.Error(state.message.toString())
                        }
                    }
                    is NetworkResponse.Loading->{
                        withContext(Dispatchers.Main) {
                            upcomingUiState.value = UpcomingUiState.Loading
                        }
                    }
                }
            }
        }
    }
    fun searchMovies(query:String){
        viewModelScope.launch(Dispatchers.IO) {
            repo.get_all_movies(query).collect{state->
                when(state){
                    is NetworkResponse.Success->{
                        state.data.results?.let {
                            withContext(Dispatchers.Main){
                                searchUiState.value=SearchUiState.Success(it)
                            }
                        }
                    }
                    is NetworkResponse.Error->{
                        withContext(Dispatchers.Main){
                            searchUiState.value=SearchUiState.Error(state.message.toString())
                        }
                    }
                    is NetworkResponse.Loading-> {
                        withContext(Dispatchers.Main) {
                            searchUiState.value = SearchUiState.Loading
                        }

                    }
                }
            }
        }
    }
}