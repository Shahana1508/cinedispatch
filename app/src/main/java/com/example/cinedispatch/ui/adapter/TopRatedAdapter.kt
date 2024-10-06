package com.example.cinedispatch.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cinedispatch.databinding.TopratedCardBinding
import com.example.cinedispatch.model.movie.TopRatedMovie

class TopRatedAdapter:RecyclerView.Adapter<TopRatedAdapter.TopRatedViewHolder> (){
    private val toprated= arrayListOf<TopRatedMovie>()
    lateinit var onclick:(String)->Unit
    inner class TopRatedViewHolder(val topratedCardBinding: TopratedCardBinding):RecyclerView.ViewHolder(topratedCardBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopRatedViewHolder {
        val view=TopratedCardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TopRatedViewHolder(view)
    }

    override fun getItemCount(): Int {
        return toprated.size
    }

    override fun onBindViewHolder(holder: TopRatedViewHolder, position: Int) {
        val item=toprated[position]
        holder.topratedCardBinding.toprated=item
        holder.topratedCardBinding.topratedcard.setOnClickListener {
            onclick(item.id.toString())
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newlist:List<TopRatedMovie>){
        toprated.clear()
        toprated.addAll(newlist)
        notifyDataSetChanged()
    }
}