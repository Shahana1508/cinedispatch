package com.example.cinedispatch.ui.detail


import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.cinedispatch.R
import com.example.cinedispatch.base.BaseFragment
import com.example.cinedispatch.databinding.FragmentDetailBinding
import com.example.cinedispatch.local.Movie_entity
import com.example.cinedispatch.model.credits.People
import com.example.cinedispatch.ui.adapter.CreditsAdapter
import com.example.cinedispatch.ui.adapter.ReviewsAdapter
import com.example.cinedispatch.ui.uistate.CreditsUiState
import com.example.cinedispatch.ui.uistate.MovieDetailsUiState
import com.example.cinedispatch.ui.uistate.MovieReviewsUiState
import com.example.cinedispatch.utils.gone
import com.example.cinedispatch.utils.loadFromUrl
import com.example.cinedispatch.utils.visible
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import io.github.muddz.styleabletoast.StyleableToast

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>(FragmentDetailBinding::inflate) {

    private val viewmodel by viewModels<DetailViewModel>()
    private val adapter=CreditsAdapter()
    private val reviewsAdapter=ReviewsAdapter()
    private val args:DetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodel.getMovieDetail(args.id.toInt())
        viewmodel.getMovieReviews(args.id.toInt())
        viewmodel.getMovieCredits(args.id.toInt(),true)
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        handleButtonSelection(true)
        adapter.onclick={
            findNavController().navigate(DetailFragmentDirections.actionDetailFragmentToBottomSheetFragment(it))
        }
        binding.btnCasts.setOnClickListener{
            handleButtonSelection(true)
            viewmodel.getMovieCredits(args.id.toInt(),true)
        }
        binding.btnCrew.setOnClickListener{
            handleButtonSelection(false)
            viewmodel.getMovieCredits(args.id.toInt(),false)

        }
        binding.seealltext.setOnClickListener {
          viewmodel.show()
        }
        binding.reviewsrv.adapter=reviewsAdapter
        binding.castsrv.adapter=adapter
        observe()
    }

    @SuppressLint("SetTextI18n")
    private fun observe(){
        viewmodel.reviewsUiState.observe(viewLifecycleOwner){
            when(it){
                is MovieReviewsUiState.Success->{
                    if(it.reviews.size<=3){
                        binding.seealltext.gone()
                    }
                    reviewsAdapter.updateList(it.reviews)
                }
                is MovieReviewsUiState.Error->{
                    StyleableToast.makeText(requireContext(),it.message, R.style.customToast).show()
                }
                is MovieReviewsUiState.Loading->{

                }
            }
        }
        viewmodel.showAll.observe(viewLifecycleOwner){
            reviewsAdapter.showrv(it)
        }
        viewmodel.seeAll.observe(viewLifecycleOwner){
            binding.seealltext.text=it
        }
        viewmodel.noreviews.observe(viewLifecycleOwner){
            if(it){
                binding.seealltext.gone()
                binding.noreviewstext.visible()
                binding.reviewstext.gone()
            }
        }
        viewmodel.isWatched.observe(viewLifecycleOwner){state->
                if (state) {
                    binding.eyeIcon.setColorFilter(requireContext().getColor(R.color.yellow))
                    binding.watchstate.text= "Watched"
                    binding.watchstate.setTextColor(requireContext().getColor(R.color.yellow))
                }else {
                    binding.eyeIcon.setColorFilter(requireContext().getColor(R.color.white))
                    binding.watchstate.text = "Not Watched"
                    binding.watchstate.setTextColor(requireContext().getColor(R.color.white))
                }
        }

        viewmodel.crew.observe(viewLifecycleOwner){
            val crewPeople = it.map { People.Crew(it.id.toString(), it.profilePath.toString()) }
            adapter.updatelist(crewPeople)
        }
        viewmodel.casts.observe(viewLifecycleOwner){
            val castPeople=it.map { People.Cast(it.id.toString(),it.profilePath.toString())}

            adapter.updatelist(castPeople)
        }
        viewmodel.isWatchlistedMovie.observe(viewLifecycleOwner){
            if (it){
                binding.addtowatchlist.text="Added to watchlist"
                binding.addtowatchlist.setIconResource(R.drawable.filledbookmark)
            }else{
                binding.addtowatchlist.text="Add to watchlist"
                binding.addtowatchlist.setIconResource(R.drawable.bookmark)
            }
        }
        viewmodel.showSnackbar.observe(viewLifecycleOwner){
            if(it){
                Snackbar.make(requireContext(),requireView(),"Added to the movies",Snackbar.LENGTH_SHORT).setAction("see"){
                    findNavController().navigate(DetailFragmentDirections.actionDetailFragmentToWatchedMoviesFragment())
                }.setActionTextColor(requireContext().getColor(R.color.yellow)).show()
                viewmodel.resetShowSnackbar()
            }
        }

        viewmodel.movieDetailUiState.observe(viewLifecycleOwner){
            when(it){
                is MovieDetailsUiState.Success->{
                    val id=it.moviedetails.id ?:0
                    val poster=it.moviedetails.posterPath?:""
                    val title=it.moviedetails.title?:""
                    val duration=it.moviedetails.runtime?:0
                    viewmodel.is_in_watchlist(id)
                    viewmodel.is_watched(id)
                    binding.reviewbutton.setOnClickListener{
                        findNavController().navigate(DetailFragmentDirections.actionDetailFragmentToReviewFragment(id.toString()))
                    }

                    binding.eyeIcon.setOnClickListener { view->
                        val movie_entity=Movie_entity(movieID = id, posterPath = poster, title = title, duration = duration)
                        Snackbar.make(requireContext(),view,"Added to the movies",Snackbar.LENGTH_SHORT).setAction("see"){
                            findNavController().navigate(DetailFragmentDirections.actionDetailFragmentToWatchedMoviesFragment())
                        }.setActionTextColor(requireContext().getColor(R.color.yellow)).show()
                        viewmodel.toggle_watched(movie_entity)
                    }
                    binding.watchstate.setOnClickListener { view->
                        val movie_entity=Movie_entity(movieID = id, posterPath = poster, title = title, duration =duration)
                        viewmodel.toggle_watched(movie_entity)
                    }
                    binding.addtowatchlist.setOnClickListener{view->
                        val movie_entity=Movie_entity(movieID = id, posterPath = poster, title = title)
                        viewmodel.toggle_watchlist(movie_entity)
                    }
                    binding.backgroundposter.loadFromUrl(it.moviedetails.backdropPath.toString())
                    binding.movieTitle.text=it.moviedetails.title
                    binding.releaseDate.text=viewmodel.formatDate(it.moviedetails.releaseDate.toString())
                    it.moviedetails.productionCompanies?.let {companyList->
                        if(companyList.isNotEmpty()){
                            val companies = companyList.joinToString(separator = ",") { company ->
                                company.name.toString()
                            }
                            binding.companyName.text = companies
                            binding.companyicon.visible()
                        }
                    }?:run {
                        binding.companyName.text="Unknown"
                    }
                    binding.duration.text=it.moviedetails.runtime.toString()+" min"
                    binding.poster.loadFromUrl(it.moviedetails.posterPath.toString())
                    binding.movieOverview.text=it.moviedetails.overview.toString()
                    val vote_average= it.moviedetails.voteAverage ?:0.0
                    val roundedRating = String.format("%.1f",it.moviedetails.voteAverage)
                    binding.ratingtext.text = roundedRating
                    binding.barChart.setData(vote_average.toFloat())
                }
                is MovieDetailsUiState.Error->{
                    StyleableToast.makeText(requireContext(),it.message, R.style.customToast).show()
                }
                is MovieDetailsUiState.Loading->{

                }
            }
        }
        viewmodel.creditsUiState.observe(viewLifecycleOwner){
            when(it){
                is CreditsUiState.Success->{
                    it.credits.crew.find { it.job=="Director" }?.name?.let {name->
                        binding.director.text=name
                        binding.directedbytext.visible()
                        binding.directoricon.visible()
                    }
                }
                is CreditsUiState.Error->{
                    StyleableToast.makeText(requireContext(),it.message, R.style.customToast).show()
                }
                is CreditsUiState.Loading->{

                }
            }
        }
    }
    private fun handleButtonSelection(isCastsSelected: Boolean) {
        with(binding){
            if (isCastsSelected) {
                btnCasts.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFB600"))
                btnCasts.setTextColor(Color.parseColor("#002335"))

                btnCrew.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#002335"))
                btnCrew.setTextColor(Color.WHITE)
            } else {
                btnCrew.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFB600"))
                btnCrew.setTextColor(Color.parseColor("#002335"))

                btnCasts.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#002335"))
                btnCasts.setTextColor(Color.WHITE)
            }
        }
    }
}