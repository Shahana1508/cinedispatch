package com.example.cinedispatch.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cinedispatch.databinding.MovieCardBinding
import com.example.cinedispatch.model.movie.PopularMovie

class PopularMovieAdapter:RecyclerView.Adapter<PopularMovieAdapter.PopularMovieViewHolder>() {
    val popularMovies= arrayListOf<PopularMovie>()
    lateinit var onclick:(String)->Unit
    inner class PopularMovieViewHolder(val movieCardBinding: MovieCardBinding):
        RecyclerView.ViewHolder(movieCardBinding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMovieViewHolder {
        val view=MovieCardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PopularMovieViewHolder(view)
    }
    override fun getItemCount(): Int {
        return popularMovies.size
    }
    override fun onBindViewHolder(holder: PopularMovieViewHolder, position: Int) {
        val movie=popularMovies[position]
        holder.movieCardBinding.movie=movie
        holder.movieCardBinding.popularmoviecard.setOnClickListener {
            onclick(movie.id.toString())
        }
    }
    fun updateList(newList:List<PopularMovie>){
        popularMovies.clear()
        popularMovies.addAll(newList)
        notifyDataSetChanged()
    }
}