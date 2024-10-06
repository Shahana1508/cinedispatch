package com.example.cinedispatch.utils


import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.cinedispatch.R
import com.example.cinedispatch.api.Constants.base_url

fun View.visible(){
    this.visibility=View.VISIBLE
}
fun View.gone(){
    this.visibility=View.GONE
}
fun ImageView.loadFromUrl(url:String?){
    url?.let {
        if(url.isNotEmpty()){
            val base="https://image.tmdb.org/t/p/w500"
            val completeUrl=base+url
            Glide.with(this).load(completeUrl).transition(DrawableTransitionOptions.withCrossFade()).
            placeholder(R.drawable.placeholder).into(this)
        }else{
            this.setImageResource(R.drawable.placeholder)
        }
    }?:run {
        this.setImageResource(R.drawable.placeholder)
    }
}
fun ImageView.loadFromUrlForCast(url:String){
    val base="https://image.tmdb.org/t/p/w500"
    val completeUrl=base+url
    Glide.with(this).load(completeUrl).transition(DrawableTransitionOptions.withCrossFade()).
    placeholder(R.drawable.placeholder).error(R.drawable.avatar).into(this)
}
