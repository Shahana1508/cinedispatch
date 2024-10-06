package com.example.cinedispatch.ui.adapter
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cinedispatch.databinding.ProfileMovieCardBinding
import com.example.cinedispatch.local.Movie_entity

class ProfileMovieAdapter: ListAdapter<Movie_entity,ProfileMovieAdapter.ProfileMovieViewHolder>(ProfileMovieAdapter.DiffCallback()){
    inner class ProfileMovieViewHolder(val profileMovieCardBinding: ProfileMovieCardBinding):RecyclerView.ViewHolder(profileMovieCardBinding.root)
    lateinit var onclick:(String)->Unit
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileMovieViewHolder {
        val view=ProfileMovieCardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProfileMovieViewHolder(view)
    }
    override fun onBindViewHolder(holder: ProfileMovieViewHolder, position: Int) {
        val movie=getItem(position)
        holder.profileMovieCardBinding.movie=movie
        holder.profileMovieCardBinding.profilemoviecard.setOnClickListener {
            onclick(movie.movieID.toString())
        }

    }
    fun update(newlist:List<Movie_entity>){
        submitList(newlist)
    }
    class DiffCallback : DiffUtil.ItemCallback<Movie_entity>() {
        override fun areItemsTheSame(oldItem: Movie_entity, newItem: Movie_entity): Boolean {
            return oldItem.movieID == newItem.movieID
        }
        override fun areContentsTheSame(oldItem: Movie_entity, newItem: Movie_entity): Boolean {
            return oldItem == newItem
        }
    }

}