package com.example.cinedispatch.ui.search

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.cinedispatch.R
import com.example.cinedispatch.base.BaseFragment
import com.example.cinedispatch.databinding.FragmentSearchBinding
import com.example.cinedispatch.ui.adapter.SearchAdapter
import com.example.cinedispatch.ui.adapter.UpcomingMovieAdapter
import com.example.cinedispatch.ui.uistate.SearchUiState
import com.example.cinedispatch.ui.uistate.UpcomingUiState
import com.example.cinedispatch.utils.gone
import com.example.cinedispatch.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import io.github.muddz.styleabletoast.StyleableToast

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate) {
    private val adapter=UpcomingMovieAdapter()
    private val searchAdapter=SearchAdapter()
    private val viewModel by viewModels<SearchViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUpcomingMovies()
        adapter.onclick={
            findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToDetailFragment(it))
        }
        binding.upcomingrv.adapter=adapter
        searchAdapter.onclick={
            findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToDetailFragment(it))
        }
        setSearchViewSettings()
        observe()
        binding.searchview.setOnQueryTextListener(object :SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        viewModel.searchMovies(it)
                        binding.upcomingrv.adapter = searchAdapter
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    newText?.let {
                        if (it.isNotEmpty()) {
                            binding.textView17.gone()
                            viewModel.searchMovies(it)
                            binding.upcomingrv.adapter = searchAdapter
                        } else {
                            viewModel.getUpcomingMovies()
                            binding.textView17.visible()
                            binding.upcomingrv.adapter = adapter
                        }
                    }
                    return true
                }
            }
        )


    }
    private fun observe(){
        viewModel.upcomingUiState.observe(viewLifecycleOwner){state->
            when(state){
                is UpcomingUiState.Success->{
                    adapter.updateList(state.movies)
                    binding.noresult.gone()
                    binding.lottie.gone()
                }
                is UpcomingUiState.Error->{
                    StyleableToast.makeText(requireContext(),state.message,R.style.customToast).show()
                    binding.lottie.gone()
                    binding.noresult.gone()
                }
                is UpcomingUiState.Loading->{
                    binding.lottie.visible()
                    binding.noresult.gone()
                }
            }
        }
        viewModel.searchUiState.observe(viewLifecycleOwner){state->
            when (state){
                is SearchUiState.Loading->{
                    binding.lottie.visible()
                }
                is SearchUiState.Success->{
                    if (state.search.isEmpty()) {
                        binding.noresult.visible()
                        searchAdapter.updateList(emptyList())
                    } else {
                        searchAdapter.updateList(state.search)
                        binding.noresult.gone()
                    }
                    binding.lottie.gone()
                }
                is SearchUiState.Error->{
                    StyleableToast.makeText(requireContext(),state.message,R.style.customToast).show()
                    binding.lottie.gone()
                    binding.noresult.gone()
                }

            }
        }
    }
    private fun setSearchViewSettings(){
        val searchView = binding.searchview
        val searchTextView = searchView.findViewById<TextView>(androidx.appcompat.R.id.search_src_text)
        val hintColor = ContextCompat.getColor(requireContext(), R.color.hintcolor)
        searchTextView.setHintTextColor(hintColor)
        searchTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.hintcolor))
        val typeface = ResourcesCompat.getFont(requireContext(), R.font.open_sans_semibold)
        searchTextView.typeface = typeface
    }
}