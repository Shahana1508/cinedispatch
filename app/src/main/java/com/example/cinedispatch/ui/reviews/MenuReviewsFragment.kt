package com.example.cinedispatch.ui.reviews


import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.cinedispatch.base.BaseFragment
import com.example.cinedispatch.databinding.FragmentMenuReviewsBinding
import com.example.cinedispatch.ui.adapter.ProfileReviewsAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MenuReviewsFragment : BaseFragment<FragmentMenuReviewsBinding>(FragmentMenuReviewsBinding::inflate) {
    @Inject
    lateinit var reviewAdapter: ProfileReviewsAdapter
    private val viewmodel by viewModels<MenuReviewsViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.menureviewsrv.adapter=reviewAdapter
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        viewmodel.getReviews()
        observe()
    }
    fun observe(){
        viewmodel.reviewsVal.observe(viewLifecycleOwner){
           reviewAdapter.submitList(it)
        }
    }

}