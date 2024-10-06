package com.example.cinedispatch.local

import androidx.room.Database
import androidx.room.RoomDatabase
@Database(entities=[Movie_entity::class,Review_entity::class], version = 1)
abstract class MovieDatabase :RoomDatabase(){
    abstract fun getDAO():MovieDAO
}