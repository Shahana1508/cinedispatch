package com.example.cinedispatch.ui.profile

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.cinedispatch.base.BaseFragment
import com.example.cinedispatch.databinding.FragmentProfileBinding
import com.example.cinedispatch.ui.adapter.ProfileMovieAdapter
import com.example.cinedispatch.ui.adapter.ProfileReviewsAdapter
import com.example.cinedispatch.utils.SwipeGesture
import com.example.cinedispatch.utils.gone
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    @Inject
    lateinit var sharedPreferences: SharedPreferences
    @Inject
    lateinit var reviewAdapter: ProfileReviewsAdapter
    private val viewModel by viewModels<ProfileViewModel>()
    private val adapter=ProfileMovieAdapter()
    private val watchedAdapter=ProfileMovieAdapter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.onclick={
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToDetailFragment(it))
        }
        watchedAdapter.onclick={
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToDetailFragment(it))
        }
        binding.usernametext.text=sharedPreferences.getString("user","User") ?:"User"
        binding.emailtext.text=sharedPreferences.getString("emaill","@email")?:"@email"
        binding.favrv.adapter=adapter
        binding.reviewsrv.adapter=reviewAdapter
        binding.watchedrv.adapter=watchedAdapter
       val swipeGesture=object :SwipeGesture(requireContext()){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position =viewHolder.absoluteAdapterPosition
                when(direction){
                    ItemTouchHelper.RIGHT->{
                        val review = reviewAdapter.currentList[position]
                        viewModel.deleteReview(review.review.reviewID)
                        reviewAdapter.deleteItem(position)
                    }
                    ItemTouchHelper.LEFT->{
                        val review = reviewAdapter.currentList[position]
                        viewModel.deleteReview(review.review.reviewID)
                        reviewAdapter.deleteItem(position)
                    }
                }
            }
        }
        ItemTouchHelper(swipeGesture).attachToRecyclerView(binding.reviewsrv)
        viewModel.getDuration()
        viewModel.getTotalMovies()
        viewModel.getRecentWatched()
        viewModel.getFavMovies()
        viewModel.getReviews()
        observe()
    }
    fun observe(){
        viewModel.reviewsVal.observe(viewLifecycleOwner){
            if(it.isEmpty()){
                binding.reviewstextview.gone()
            }
            reviewAdapter.submitList(it)
        }
        viewModel.watchedMovies.observe(viewLifecycleOwner){
            watchedAdapter.update(it)
        }
        viewModel.moviesList.observe(viewLifecycleOwner){
            adapter.update(it)
        }
        viewModel.totalMovies.observe(viewLifecycleOwner){
            binding.watchedcount.text=it.toString()
        }
        viewModel.duration.observe(viewLifecycleOwner){
            binding.timespenttext.text=it
        }
    }
}