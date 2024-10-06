package com.example.cinedispatch.ui.Auth

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.cinedispatch.R
import com.example.cinedispatch.base.BaseFragment
import com.example.cinedispatch.databinding.FragmentLogInBinding
import com.example.cinedispatch.ui.uistate.AuthUiState
import com.example.cinedispatch.utils.gone
import com.example.cinedispatch.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LogInFragment : BaseFragment<FragmentLogInBinding>(FragmentLogInBinding::inflate) {
    private val viewmodel by viewModels<LoginViewmodel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodel.getinfo()
        with(binding){
            signUp.setOnClickListener {
                findNavController().navigate(LogInFragmentDirections.actionLogInFragmentToSignUpFragment())
            }
            loginbutton.setOnClickListener{
                val e_mail=binding.username.text.toString()
                val password=binding.password.text.toString()
                if(e_mail.isNotEmpty() && password.isNotEmpty()){
                    viewmodel.login(e_mail,password)
                    viewmodel.getName(e_mail)
                }else{
                    StyleableToast.makeText(requireContext(),"Fields can not be empty", R.style.customToast).show()
                }
            }
        }
        observe()
    }
    fun observe(){
        viewmodel.uiState.observe(viewLifecycleOwner){
                when(it){
                    is AuthUiState.Success->{
                        StyleableToast.makeText(requireContext(),"Logged in successfully", R.style.customToast).show()
                        findNavController().navigate(LogInFragmentDirections.actionLogInFragmentToHomeFragment())
                        binding.lottie.gone()
                    }
                    is AuthUiState.Error->{
                        StyleableToast.makeText(requireContext(),it.message, R.style.customToast).show()
                        binding.lottie.gone()
                    }
                    is AuthUiState.Loading->{
                        binding.lottie.visible()
                    }
                }
        }


        viewmodel.alreadyLoggedIn.observe(viewLifecycleOwner){
            if(it){
                findNavController().navigate(LogInFragmentDirections.actionLogInFragmentToHomeFragment())
            }
        }

    }

}