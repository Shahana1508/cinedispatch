package com.example.cinedispatch.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cinedispatch.databinding.ReviewCardBinding
import com.example.cinedispatch.model.review.Review
import com.example.cinedispatch.utils.gone
import com.example.cinedispatch.utils.visible

class ReviewsAdapter:RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder>() {
    val reviews= arrayListOf<Review>()
    private var show:Boolean=false
    inner class ReviewsViewHolder(val reviewCardBinding: ReviewCardBinding):RecyclerView.ViewHolder(reviewCardBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewsViewHolder {
        val view=ReviewCardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ReviewsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if(show){
             reviews.size
        }else{
            minOf(reviews.size,3)
        }
    }

    override fun onBindViewHolder(holder: ReviewsViewHolder, position: Int) {
        val review=reviews[position]
        holder.reviewCardBinding.review=review
        val lines:Int?=review.content?.length?.let {
            it/55
        }
        if (lines != null) {
            if(lines>3){
                holder.reviewCardBinding.reviews.maxLines=3
                holder.reviewCardBinding.readmoretext.visible()
            }else{
                holder.reviewCardBinding.reviews.maxLines=Int.MAX_VALUE
                holder.reviewCardBinding.readmoretext.gone()
            }
        }

        holder.reviewCardBinding.readmoretext.setOnClickListener {
            if(holder.reviewCardBinding.reviews.maxLines==3){
                holder.reviewCardBinding.reviews.maxLines=Int.MAX_VALUE
                holder.reviewCardBinding.readmoretext.text="Show less"
            }else{
                holder.reviewCardBinding.reviews.maxLines=3
                holder.reviewCardBinding.readmoretext.text="Read more>"
            }
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun showrv(seeAll:Boolean){
        show=seeAll
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList:List<Review>){
        reviews.clear()
        reviews.addAll(newList)
        notifyDataSetChanged()
    }
}