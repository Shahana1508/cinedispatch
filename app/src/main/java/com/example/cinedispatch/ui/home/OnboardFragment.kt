package com.example.cinedispatch.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.cinedispatch.base.BaseFragment
import com.example.cinedispatch.databinding.FragmentOnboardBinding
import com.example.cinedispatch.ui.Auth.LoginViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardFragment : BaseFragment<FragmentOnboardBinding>(FragmentOnboardBinding::inflate) {
    private val onBoardViewModel by viewModels<LoginViewmodel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBoardViewModel.getinfo()
        binding.button.setOnClickListener{
            findNavController().navigate(OnboardFragmentDirections.actionOnboardFragmentToLogInFragment())
        }
        observe()
    }
    fun observe(){
        onBoardViewModel.alreadyLoggedIn.observe(viewLifecycleOwner){
            if(it){
                findNavController().navigate(OnboardFragmentDirections.actionOnboardFragmentToHomeFragment())
            }
        }
    }
}