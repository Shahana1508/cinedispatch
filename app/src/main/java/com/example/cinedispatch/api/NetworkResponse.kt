package com.example.cinedispatch.api

sealed class NetworkResponse<out T>{
    class Success<out T>(val data:T):NetworkResponse<T>()
    class Error<T>(val message:String?):NetworkResponse<T>()
    class Loading<Nothing>:NetworkResponse<Nothing>()

}