package com.example.cinedispatch.repo

import android.content.SharedPreferences
import com.example.cinedispatch.model.authentication.Authmodel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class AuthRepo @Inject constructor(private val firebaseAuth: FirebaseAuth,private val sharedPreferences: SharedPreferences){
    suspend fun loginUser(email:String,password:String): Flow<Authmodel> = flow {
        emit(Authmodel(loading = true))
        val authResult=firebaseAuth.signInWithEmailAndPassword(email,password).await()
        val user=authResult.user
        if(user!=null){
            emit(Authmodel(user=user))
            setInfo(true)
        }else{
            emit(Authmodel(error = "Failure Occured"))
        }
    }.catch {
        emit(Authmodel(error = it.localizedMessage?.toString()))
    }.flowOn(Dispatchers.IO)

    suspend fun signUp(email: String,password: String):Flow<Authmodel> = flow {
        emit(Authmodel(loading = true))
        val authresult=firebaseAuth.createUserWithEmailAndPassword(email,password).await()
        val user=authresult.user
        if(user!=null){
            setInfo(true)
            emit(Authmodel(user=user))
        }else{
            emit(Authmodel(error = "Failure Occured"))
        }
    }.catch {
        emit(Authmodel(error = it.localizedMessage?.toString()))
    }.flowOn(Dispatchers.IO)
    fun setInfo(isLoggedIn:Boolean){
        val editor=sharedPreferences.edit()
        editor.putBoolean("login",true)
        editor.apply()
    }
    fun getInfo():Boolean{
        return sharedPreferences.getBoolean("login",false)
    }
    fun logout(){
        sharedPreferences.edit().putBoolean("login",false).apply()
    }

}