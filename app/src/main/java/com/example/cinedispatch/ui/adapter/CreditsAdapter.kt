package com.example.cinedispatch.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cinedispatch.databinding.CastCardBinding
import com.example.cinedispatch.model.credits.People
import com.example.cinedispatch.utils.loadFromUrlForCast


class CreditsAdapter:RecyclerView.Adapter<CreditsAdapter.CreditsViewHolder>() {
    private val cast= arrayListOf<People>()
    lateinit var onclick:(String)->Unit
    inner class CreditsViewHolder(val castCardBinding: CastCardBinding):RecyclerView.ViewHolder(castCardBinding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditsViewHolder {
        val view=CastCardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CreditsViewHolder(view)
    }
    override fun getItemCount(): Int {
        return cast.size
    }
    override fun onBindViewHolder(holder: CreditsViewHolder, position: Int) {
        val person=cast[position]
        holder.castCardBinding.imageView13.loadFromUrlForCast(person.photoUrl.toString())
        holder.castCardBinding.castCard.setOnClickListener {
            onclick(person.id.toString())
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updatelist(newlist:List<People>){
        cast.clear()
        cast.addAll(newlist)
        notifyDataSetChanged()
    }
}