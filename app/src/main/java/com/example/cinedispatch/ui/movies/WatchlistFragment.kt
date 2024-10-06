package com.example.cinedispatch.ui.movies


import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.cinedispatch.base.BaseFragment
import com.example.cinedispatch.databinding.FragmentWatchlistBinding
import com.example.cinedispatch.ui.adapter.WatchlistAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WatchlistFragment : BaseFragment<FragmentWatchlistBinding>(FragmentWatchlistBinding::inflate) {
    private val adapter=WatchlistAdapter()
    private val viewModel by viewModels<WatchlistViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.watchlistrv.adapter=adapter
        adapter.onclick={
            findNavController().navigate(WatchlistFragmentDirections.actionWatchlistFragmentToDetailFragment(it))
        }
        viewModel.get_watchlist_movies()
        observe()
    }
    fun observe(){
        viewModel.watchlisted.observe(viewLifecycleOwner){
            adapter.update(it)
        }
    }
}