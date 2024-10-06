package com.example.cinedispatch.ui.movies

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.cinedispatch.base.BaseFragment
import com.example.cinedispatch.databinding.FragmentWatchedMoviesBinding
import com.example.cinedispatch.ui.adapter.WatchlistAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WatchedMoviesFragment : BaseFragment<FragmentWatchedMoviesBinding>(FragmentWatchedMoviesBinding::inflate) {
    private val viewModel by viewModels<WatchedMoviesViewModel> ()
    private val adapter=WatchlistAdapter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.watchedmoviesrv.adapter=adapter
        viewModel.watchedMovies()
        adapter.onclick={
            findNavController().navigate(WatchedMoviesFragmentDirections.actionWatchedMoviesFragmentToDetailFragment(it))
        }
        binding.backbutton.setOnClickListener {
            findNavController().popBackStack()
        }
        observe()
    }
    fun observe(){
        viewModel.watchedMovies.observe(viewLifecycleOwner){
            adapter.update(it)
        }
    }

}