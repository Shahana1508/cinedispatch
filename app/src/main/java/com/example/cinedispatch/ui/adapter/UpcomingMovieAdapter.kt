package com.example.cinedispatch.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cinedispatch.databinding.UpcomingCardBinding
import com.example.cinedispatch.model.movie.UpcomingMovie
import com.example.cinedispatch.utils.loadFromUrl

class UpcomingMovieAdapter:RecyclerView.Adapter<UpcomingMovieAdapter.UpcomingMovieViewHolder>() {
    val movies=ArrayList<UpcomingMovie>()
    lateinit var onclick:(String)->Unit
    inner class UpcomingMovieViewHolder(val upcomingCardBinding: UpcomingCardBinding):RecyclerView.ViewHolder(upcomingCardBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingMovieViewHolder {
        val view=UpcomingCardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return UpcomingMovieViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: UpcomingMovieViewHolder, position: Int) {
        val movie=movies[position]
        holder.upcomingCardBinding.imageView11.loadFromUrl(movie.posterPath.toString())
        holder.upcomingCardBinding.textView18.text=movie.title
        holder.upcomingCardBinding.upcomingCard.setOnClickListener {
            onclick(movie.id.toString())
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList:List<UpcomingMovie>){
        movies.clear()
        movies.addAll(newList)
        notifyDataSetChanged()
    }
}