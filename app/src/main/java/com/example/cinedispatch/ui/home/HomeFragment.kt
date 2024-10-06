package com.example.cinedispatch.ui.home

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.cinedispatch.R
import com.example.cinedispatch.base.BaseFragment
import com.example.cinedispatch.databinding.FragmentHomeBinding
import com.example.cinedispatch.repo.AuthRepo
import com.example.cinedispatch.ui.adapter.PopularMovieAdapter
import com.example.cinedispatch.ui.adapter.TopRatedAdapter
import com.example.cinedispatch.ui.uistate.MovieUiState
import com.example.cinedispatch.ui.uistate.TopRatedUiState
import com.example.cinedispatch.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import io.github.muddz.styleabletoast.StyleableToast
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment: BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    @Inject
    lateinit var sharedPreferences:SharedPreferences
    @Inject
    lateinit var repo: AuthRepo
    private val adapter= PopularMovieAdapter()
    private val topRatedAdapter= TopRatedAdapter()
    private val homeViewModel by viewModels<HomeViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        set_menu()
        setNameAndEmail()
        topRatedAdapter.onclick={
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailFragment(it))
        }
        adapter.onclick={
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailFragment(it))
        }
        binding.menuButton.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
            set_menu()
        }
        binding.name.text=sharedPreferences.getString("user","User") ?:"User"
        binding.popularmovierv.adapter=adapter
        binding.topratedrv.adapter=topRatedAdapter
        homeViewModel.getMovies()
        homeViewModel.getTopRated()
        observe()
    }
    fun setNameAndEmail(){
        val nav_view=binding.navView
        val headerView: View = nav_view.getHeaderView(0)
        val username: TextView = headerView.findViewById(R.id.username_text)
        val email: TextView = headerView.findViewById(R.id.email_text)

        username.text=sharedPreferences.getString("user","User")?:"user"
        email.text=sharedPreferences.getString("emaill","@email")?:"@email"

    }
    fun set_menu(){
        val nav_view=binding.navView
        nav_view.setNavigationItemSelectedListener {menu->
            menu.isChecked = true
            when(menu.itemId){
                R.id.watchlist->{
                    findNavController().navigate(R.id.watchlistFragment)
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.movies->{
                    findNavController().navigate(R.id.watchedMoviesFragment)
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.logger->{
                    true
                }
                R.id.logout->{
                    repo.logout()
                    findNavController().navigate(R.id.onboardFragment)
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.reviews->{
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToMenuReviewsFragment())
                    true
                }
                else->false
            }
        }
    }
    private fun observe(){
        homeViewModel.uiState.observe(viewLifecycleOwner){
            when(it){
                is MovieUiState.Loading->{

                }
                is MovieUiState.Success->{
                    adapter.updateList(it.movies)
                    binding.populartext.visible()
                }
                is MovieUiState.Error->{
                    StyleableToast.makeText(requireContext(),it.message.toString(),R.style.customToast).show()
                }
            }
        }
        homeViewModel.uiState2.observe(viewLifecycleOwner){
            when(it){
                is TopRatedUiState.Loading->{

                }
                is TopRatedUiState.Success->{
                    topRatedAdapter.updateList(it.movies)
                    binding.topratedtext.visible()
                }
                is TopRatedUiState.Error->{
                    StyleableToast.makeText(requireContext(),it.message.toString(),R.style.customToast).show()
                }
            }
        }
    }
}