package com.example.cinedispatch.ui.reviews


import android.os.Bundle
import android.view.View
import android.widget.RatingBar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.cinedispatch.R
import com.example.cinedispatch.base.BaseFragment
import com.example.cinedispatch.databinding.FragmentReviewBinding
import com.example.cinedispatch.local.Movie_entity
import com.example.cinedispatch.local.Review_entity
import com.example.cinedispatch.ui.uistate.MovieDetailsUiState
import com.example.cinedispatch.utils.loadFromUrl
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import io.github.muddz.styleabletoast.StyleableToast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class ReviewFragment : BaseFragment<FragmentReviewBinding>(FragmentReviewBinding::inflate) {
    private val args:ReviewFragmentArgs by navArgs()
    private val viewmodel by viewModels<ReviewViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movie_id=args.id.toInt()
        binding.backButtonReview.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.date.text=getCurrentDate()
        viewmodel.getRating(movie_id)
        viewmodel.get_details(movie_id)
        viewmodel.isFavorite(movie_id)
        observe()
    }
    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
        return dateFormat.format(Date())
    }
    fun observe(){

        viewmodel.ratingVal.observe(viewLifecycleOwner){
            binding.ratingBar.rating=it
        }
        viewmodel.is_fav.observe(viewLifecycleOwner){
            if(it){
                binding.likebutton.setImageResource(R.drawable.yellow_like)
            }else{
                binding.likebutton.setImageResource(R.drawable.likebutton)
            }
        }
        viewmodel.reviewUiState.observe(viewLifecycleOwner){
            when(it){
                is MovieDetailsUiState.Success->{
                    val id=it.moviedetails.id?:0
                    val title=it.moviedetails.title?:" "
                    val poster=it.moviedetails.posterPath
                    binding.likebutton.setOnClickListener {
                        val movie_entity=Movie_entity(movieID = id,title=title, posterPath =poster)
                        viewmodel.toggleFav(movie_entity)
                    }
                    binding.ratingBar.onRatingBarChangeListener=RatingBar.OnRatingBarChangeListener{_,rating,_->
                        val movie_entity=Movie_entity(movieID = id,title=title, posterPath =poster,rating=rating)
                        viewmodel.updateRating(movie_entity)
                    }
                    binding.publishbtn.setOnClickListener{
                        val rating=binding.ratingBar.rating
                        if(rating>0 ){
                            if (binding.reviewtext.text.isNotEmpty()) {
                                val movie_entity=Movie_entity(movieID = id,title=title, posterPath =poster)
                                viewmodel.addReview(Review_entity(movieID=id, reviewText = binding.reviewtext.text.toString()),movie_entity)
                                Snackbar.make(requireContext(),requireView(),"Review added!",
                                    Snackbar.LENGTH_SHORT).setAction("see"){
                                    findNavController().navigate(R.id.menuReviewsFragment)
                                }.setActionTextColor(requireContext().getColor(R.color.yellow)).show()
                                binding.reviewtext.text.clear()
                            } else {
                                StyleableToast.makeText(requireContext(),"Empty review can not be published!",R.style.customToast).show()
                            }
                        }else{
                            if(binding.reviewtext.text.isEmpty()){
                                StyleableToast.makeText(requireContext(),"Empty review can not be published!",R.style.customToast).show()
                            }else{
                                StyleableToast.makeText(requireContext(),"Please select a rating before publishing your review.",R.style.customToast).show()
                            }
                        }

                    }
                    binding.reviewposter.loadFromUrl(it.moviedetails.posterPath.toString())
                    binding.movieName.text=it.moviedetails.title
                }
                is MovieDetailsUiState.Error->{
                    StyleableToast.makeText(requireContext(),it.message, R.style.customToast).show()
                }
                is MovieDetailsUiState.Loading->{

                }
            }
        }
    }
}