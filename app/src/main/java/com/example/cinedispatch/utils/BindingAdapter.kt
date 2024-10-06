package com.example.cinedispatch.utils

import android.content.SharedPreferences
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.cinedispatch.api.Constants.base_url
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@BindingAdapter("loadImageApi")
fun loadImage(imageView: ImageView,Url:String){
    imageView.loadFromUrl(Url)
}
@BindingAdapter("setRatingText")
fun set_rating(ratingBar: RatingBar,rating:Float?){
    if(rating==null || rating<=0){
        ratingBar.gone()
    }
}
@BindingAdapter("formatRating")
fun format_rating(textView: TextView,rating: Double?){
    rating?.let {
        val formatted= String.format("%.1f",it/2)
        textView.text=formatted
    }?: run {
        textView.text="N/A"
    }
}
