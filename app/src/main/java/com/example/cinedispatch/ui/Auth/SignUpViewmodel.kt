package com.example.cinedispatch.ui.Auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinedispatch.repo.AuthRepo
import com.example.cinedispatch.ui.uistate.AuthUiState
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignUpViewmodel @Inject constructor(val repo:AuthRepo,val db:FirebaseFirestore):ViewModel() {
    val uistate=MutableLiveData<AuthUiState>()
    val alreadySignedUp=MutableLiveData<Boolean>()
    fun signup(email:String,password:String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.signUp(email, password).collect() {
                when {
                    it.loading -> {
                        withContext(Dispatchers.Main) {
                            uistate.value = AuthUiState.Loading
                        }

                    }

                    it.user != null -> {
                        withContext(Dispatchers.Main) {
                            uistate.value = AuthUiState.Success(true)
                        }
                    }

                    it.error != null -> {
                        withContext(Dispatchers.Main) {
                            uistate.value = AuthUiState.Error(it.error.toString())
                        }
                    }
                }
            }
        }
    }
    fun saveUserData(email: String, password: String, name: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val user = hashMapOf(
            "email" to email,
            "password" to password,
            "name" to name
        )

        db.collection("users")
            .document(email)
            .set(user)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
    fun getInfo(){
        alreadySignedUp.value=false
        if(repo.getInfo()){
            alreadySignedUp.value=true
        }
    }

}