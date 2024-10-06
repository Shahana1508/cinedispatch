package com.example.cinedispatch.repo

import com.example.cinedispatch.local.MovieDAO
import com.example.cinedispatch.local.MovieWithReviews
import com.example.cinedispatch.local.Movie_entity
import com.example.cinedispatch.local.Review_entity
import javax.inject.Inject

class RoomRepo @Inject constructor(private val movieDAO: MovieDAO) {

     fun get_movies_from_watchlist():List<Movie_entity>{
        return movieDAO.getWatchlist()
     }
     fun get_watched_movies():List<Movie_entity>{
        return movieDAO.getWatched()
     }
    suspend fun get_movie(movieId: Int):Movie_entity{
        return movieDAO.getMovie(movieId)
    }
    suspend fun update(movieEntity: Movie_entity){
        movieDAO.updateMovie(movieEntity)
    }
    suspend fun delete(movieEntity: Movie_entity){
        movieDAO.deleteMovie(movieEntity)
    }
    fun get_watched_movies_count():Int{
        return movieDAO.getWatchedMoviesCount()
    }
    fun get_total_watched_duration():Int{
        return movieDAO.getTotalWatchedDuration()
    }
    fun get_fav_movies():List<Movie_entity>{
        return movieDAO.getFavMovies()
    }
    fun addMovie(movieEntity: Movie_entity){
        val movie=movieEntity.copy(createdAt = System.currentTimeMillis())
        movieDAO.addMovie(movie)
    }
    fun get_rating(movieId: Int){
        movieDAO.getRating(movieId)
    }
    fun addReview(reviewEntity: Review_entity){
        val review=reviewEntity.copy(createdAt = System.currentTimeMillis())
        movieDAO.addReview(review)
    }
    fun getReviews(movieid:Int){
        movieDAO.getReviewsByMovieId(movieid)
    }
    fun get_reviews_with_movies():List<Review_entity>{
        return movieDAO.getAllReviews()
    }
    fun delete_review(id:Int){
        movieDAO.deleteReviewById(id)
    }

}