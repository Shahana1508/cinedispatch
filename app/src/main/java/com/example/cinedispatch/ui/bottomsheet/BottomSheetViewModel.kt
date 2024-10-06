package com.example.cinedispatch.ui.bottomsheet

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinedispatch.api.NetworkResponse
import com.example.cinedispatch.repo.MovieRepo
import com.example.cinedispatch.ui.uistate.BottomSheetUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class bottomSheetViewModel @Inject constructor(val movieRepo: MovieRepo):ViewModel() {
    val bottomSheetUiState=MutableLiveData<BottomSheetUiState>()
    fun getCastDetails(id:Int){
        viewModelScope.launch(Dispatchers.IO) {
            movieRepo.get_person_details(id).collect{
                when(it){
                    is NetworkResponse.Loading->{
                        withContext(Dispatchers.Main){
                            bottomSheetUiState.value=BottomSheetUiState.Loading

                        }
                    }
                    is NetworkResponse.Success->{
                        it.data?.let {
                            withContext(Dispatchers.Main){
                                bottomSheetUiState.value=BottomSheetUiState.Success(it)
                            }
                        }
                    }
                    is NetworkResponse.Error->{
                        withContext(Dispatchers.Main){
                            bottomSheetUiState.value=BottomSheetUiState.Error(it.message.toString())
                        }
                    }
                }
            }
        }
    }
}