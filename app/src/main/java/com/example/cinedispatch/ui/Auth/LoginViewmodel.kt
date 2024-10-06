package com.example.cinedispatch.ui.Auth

import android.content.SharedPreferences
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

@Suppress("NAME_SHADOWING")
@HiltViewModel
class LoginViewmodel @Inject constructor(private val sharedPreferences: SharedPreferences,private val repo: AuthRepo,private val db:FirebaseFirestore) :ViewModel(){
    val uiState=MutableLiveData<AuthUiState>()
    val error=MutableLiveData<String>()
    val alreadyLoggedIn=MutableLiveData<Boolean>()
    fun login(email: String,password:String){
        viewModelScope.launch(Dispatchers.IO) {
            repo.loginUser(email,password).collect{
                when{
                    it.loading->{
                        withContext(Dispatchers.Main){
                            uiState.value= AuthUiState.Loading
                        }
                    }
                    it.error!=null->{
                        withContext(Dispatchers.Main){
                            uiState.value= AuthUiState.Error(it.error)
                        }
                    }
                    it.user!=null->{
                        withContext(Dispatchers.Main){
                            uiState.value= AuthUiState.Success(true)
                        }
                    }
                }
            }
        }
    }

    fun getName(email: String){
        val docRef = db.collection("users").document(email)

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val userName = document.getString("name")
                    val e_mail=document.getString("email")
                    sharedPreferences.edit().putString("emaill",e_mail).apply()
                    sharedPreferences.edit().putString("user",userName).apply()
                } else {
                    error.value="No such document"
                }
            }
            .addOnFailureListener { exception ->
               error.value=exception.localizedMessage?.toString()
            }
    }
    fun getinfo(){
        alreadyLoggedIn.value=false
        if(repo.getInfo()){
            alreadyLoggedIn.value=true
        }
    }
}