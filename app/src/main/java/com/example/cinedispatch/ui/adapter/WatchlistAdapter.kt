package com.example.cinedispatch.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cinedispatch.databinding.WatchlistCardBinding
import com.example.cinedispatch.local.Movie_entity

class WatchlistAdapter : ListAdapter<Movie_entity, WatchlistAdapter.WatchlistViewHolder>(DiffCallback()){
    private val movies= arrayListOf<Movie_entity>()
    lateinit var onclick:(String)->Unit
    inner class WatchlistViewHolder(val watchlistCardBinding: WatchlistCardBinding):
        RecyclerView.ViewHolder(watchlistCardBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchlistViewHolder {
        val view=WatchlistCardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return WatchlistViewHolder(view)
    }
    override fun getItemCount(): Int {
        return movies.size
    }
    override fun onBindViewHolder(holder: WatchlistViewHolder, position: Int) {
        val movie=movies[position]
        holder.watchlistCardBinding.watchlistcard.setOnClickListener {
            onclick(movie.movieID.toString())
        }
        holder.watchlistCardBinding.movie=movie
    }
    class DiffCallback : DiffUtil.ItemCallback<Movie_entity>() {
        override fun areItemsTheSame(oldItem: Movie_entity, newItem: Movie_entity): Boolean {
            return oldItem.movieID == newItem.movieID
        }
        override fun areContentsTheSame(oldItem: Movie_entity, newItem: Movie_entity): Boolean {
            return oldItem == newItem
        }
    }
    fun update(newlist:List<Movie_entity>){
        movies.clear()
        movies.addAll(newlist)
        notifyDataSetChanged()
    }
}