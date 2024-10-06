package com.example.cinedispatch.local


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface MovieDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMovie(movieEntity: Movie_entity)
    @Query("SELECT * FROM movie_table where movieID=:movieId")
    suspend fun getMovie(movieId:Int):Movie_entity
    @Query("SELECT * FROM movie_table WHERE isWatchListed = 1 ORDER BY createdAt DESC")
    fun getWatchlist(): List<Movie_entity>
    @Query("SELECT * FROM movie_table WHERE iswatched=1  ORDER BY createdAt DESC")
    fun getWatched():List<Movie_entity>
    @Query("SELECT *FROM movie_table WHERE isFavorite=1 ORDER BY createdAt DESC")
    fun getFavMovies():List<Movie_entity>
    @Query("SELECT COUNT(*) FROM movie_table WHERE (isWatched = 1  OR isFavorite = 1)")
    fun getWatchedMoviesCount(): Int
    @Query("SELECT SUM(duration) FROM movie_table WHERE (isWatched = 1 OR isFavorite=1)")
    fun getTotalWatchedDuration(): Int
    @Update
    suspend fun updateMovie(movie: Movie_entity)
    @Delete
    suspend fun deleteMovie(movieEntity: Movie_entity)
    @Query("SELECT Rating FROM movie_table WHERE movieID = :movieId")
    fun getRating(movieId: Int):Float?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addReview(reviewEntity: Review_entity)
    @Query("SELECT * FROM Review_table WHERE movieID = :movieId")
    fun getReviewsByMovieId(movieId: Int): List<Review_entity>
    @Query("SELECT * FROM Review_table ORDER BY createdAt DESC")
    fun getAllReviews(): List<Review_entity>
    @Query("DELETE FROM Review_table WHERE reviewID = :reviewId")
    fun deleteReviewById(reviewId: Int)
}