package com.example.cinedispatch.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cinedispatch.databinding.UpcomingCardBinding
import com.example.cinedispatch.model.movie.SearchMovie
import com.example.cinedispatch.utils.loadFromUrl

class SearchAdapter :RecyclerView.Adapter<SearchAdapter.SearchViewHolder>(){
    private val searchList= arrayListOf<SearchMovie>()
    lateinit var onclick:(String)->Unit
    inner class SearchViewHolder(val upcomingCardBinding: UpcomingCardBinding):RecyclerView.ViewHolder(upcomingCardBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view=UpcomingCardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SearchViewHolder(view)
    }

    override fun getItemCount(): Int {
        return searchList.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val movie=searchList[position]
        holder.upcomingCardBinding.imageView11.loadFromUrl(movie.posterPath.toString())
        holder.upcomingCardBinding.textView18.text=movie.title
        holder.upcomingCardBinding.upcomingCard.setOnClickListener {
            onclick(movie.id.toString())
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList:List<SearchMovie>){
        searchList.clear()
        searchList.addAll(newList)
        notifyDataSetChanged()
    }
}