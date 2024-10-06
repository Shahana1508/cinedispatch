package com.example.cinedispatch.ui.adapter

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.cinedispatch.local.MovieWithReviews
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cinedispatch.databinding.ProfilereviewCardBinding
import com.example.cinedispatch.repo.RoomRepo
import javax.inject.Inject



class ProfileReviewsAdapter @Inject constructor(val roomRepo: RoomRepo): ListAdapter<MovieWithReviews,ProfileReviewsAdapter.ProfileReviewsViewHolder>(DiffCallback()){
    @Inject
    lateinit var sharedPreferences:SharedPreferences
    inner class ProfileReviewsViewHolder(val profilereviewCardBinding: ProfilereviewCardBinding):
        RecyclerView.ViewHolder(profilereviewCardBinding.root)
    class DiffCallback : DiffUtil.ItemCallback<MovieWithReviews>() {
        override fun areItemsTheSame(oldItem: MovieWithReviews, newItem: MovieWithReviews): Boolean {
            return oldItem.movie.movieID== newItem.movie.movieID
        }
        override fun areContentsTheSame(oldItem: MovieWithReviews, newItem: MovieWithReviews): Boolean {
            return oldItem.movie == newItem.movie
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileReviewsViewHolder {
        val view=ProfilereviewCardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProfileReviewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileReviewsViewHolder, position: Int) {
        val review=getItem(position)
        holder.profilereviewCardBinding.review=review.review
        holder.profilereviewCardBinding.accname.text=sharedPreferences.getString("user","username")?:"User"
        holder.profilereviewCardBinding.movie=review.movie
    }

    fun update(newList:List<MovieWithReviews>){
        submitList(newList)
    }
    fun deleteItem(i:Int){
        val list=currentList.toMutableList()
        list.removeAt(i)
        submitList(list)
    }
}