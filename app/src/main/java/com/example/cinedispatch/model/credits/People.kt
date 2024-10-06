package com.example.cinedispatch.model.credits

sealed class People(var id: String?,val photoUrl: String?){
     class Cast( id:String?,photoUrl:String?): People(id,photoUrl)
    class Crew( id:String?, photoUrl:String): People(id,photoUrl)
}