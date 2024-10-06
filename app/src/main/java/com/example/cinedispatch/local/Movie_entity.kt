package com.example.cinedispatch.local

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
@Entity(tableName = "Movie_table")
data class Movie_entity (
    @PrimaryKey
    val movieID:Int,
    @ColumnInfo("Title")
    var title: String,
    @ColumnInfo("Poster_path")
    var posterPath: String?,
    @ColumnInfo("Iswatched")
    var isWatched:Boolean=false,
    @ColumnInfo("Iswatchlisted")
    var isWatchListed:Boolean=false,
    @ColumnInfo("Duration")
    var duration:Int=0,
    @ColumnInfo("isFavorite")
    var isFav:Boolean=false,
    @ColumnInfo("Rating")
    var rating:Float?=0.0f,
    @ColumnInfo("createdAt")
    var createdAt: Long = System.currentTimeMillis()
)


