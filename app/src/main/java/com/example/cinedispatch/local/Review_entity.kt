package com.example.cinedispatch.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Review_table")
data class Review_entity (
    @PrimaryKey(autoGenerate = true)
    val reviewID: Int = 0,
    @ColumnInfo("movieID")
    val movieID: Int,
    @ColumnInfo("Review_text")
    var reviewText: String,
    @ColumnInfo("createdAt")
    var createdAt: Long = System.currentTimeMillis()
)