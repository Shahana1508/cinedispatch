package com.example.cinedispatch.api

import com.example.cinedispatch.model.credits.CreditsResponse
import com.example.cinedispatch.model.movie.MovieDetailResponse
import com.example.cinedispatch.model.PersonDetailsResponse
import com.example.cinedispatch.model.movie.PopularMovieResponse
import com.example.cinedispatch.model.review.ReviewResponse
import com.example.cinedispatch.model.movie.SearchMovieResponse
import com.example.cinedispatch.model.movie.TopRatedResponse
import com.example.cinedispatch.model.movie.UpcomingMovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {

    @GET("movie/popular")
    suspend fun getPopularMovies():Response<PopularMovieResponse>
    @GET("movie/top_rated")
    suspend fun getTopRatedMovies():Response<TopRatedResponse>
    @GET("movie/upcoming")
    suspend fun getUpcomingMovies():Response<UpcomingMovieResponse>
    @GET("search/movie")
    suspend fun searchMovies(@Query("query")query:String):Response<SearchMovieResponse>
    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(@Path("movie_id") id:Int):Response<MovieDetailResponse>
    @GET("movie/{movie_id}/credits")
    suspend fun getCredits(@Path("movie_id") id:Int):Response<CreditsResponse>
    @GET("movie/{movie_id}/reviews")
    suspend fun getMovieReviews(@Path("movie_id") id:Int ):Response<ReviewResponse>
    @GET("person/{person_id}")
    suspend fun getPersonDetails(@Path("person_id") id:Int):Response<PersonDetailsResponse>
}