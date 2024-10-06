package com.example.cinedispatch.repo

import android.util.Log
import android.widget.Toast
import androidx.credentials.exceptions.domerrors.NetworkError
import com.example.cinedispatch.api.MovieService
import com.example.cinedispatch.api.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import retrofit2.http.Query
import javax.inject.Inject

class MovieRepo @Inject constructor(val movieService:MovieService) {
    suspend fun get_popular_movies()=safeApiRequest {
        movieService.getPopularMovies()
    }
    suspend fun get_top_rated()=safeApiRequest {
        movieService.getTopRatedMovies()
    }
    suspend fun get_upcoming()=safeApiRequest {
        movieService.getUpcomingMovies()
    }
    suspend fun get_all_movies(query: String)=safeApiRequest {
        movieService.searchMovies(query) }
    suspend fun get_movie_details(id:Int)=safeApiRequest {
        movieService.getMovieDetail(id)
    }
    suspend fun get_movie_credits(id:Int)=safeApiRequest {
        movieService.getCredits(id)
    }
    suspend fun get_movie_reviews(id:Int)=safeApiRequest {
        movieService.getMovieReviews(id)
    }
    suspend fun get_person_details(id:Int)=safeApiRequest {
        movieService.getPersonDetails(id)
    }
    private fun <T> safeApiRequest(apicall:suspend () -> Response<T>):Flow<NetworkResponse<T & Any>>{
        return flow<NetworkResponse<T&Any>> {
            emit(NetworkResponse.Loading())
            val response = apicall.invoke()
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(NetworkResponse.Success(it))
                }?: emit(NetworkResponse.Error("No data available"))
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                val statusCode = response.code()
                Log.e("API Error", "Status Code: $statusCode, Error: $errorMessage")
                emit(NetworkResponse.Error("failure"))
            }
        }.catch {
            emit(NetworkResponse.Error(it.localizedMessage?.toString() ?:"Unknown error"))
        }.flowOn(Dispatchers.IO)
    }

}