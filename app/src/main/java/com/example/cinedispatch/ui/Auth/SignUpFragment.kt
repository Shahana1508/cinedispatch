package com.example.cinedispatch.ui.Auth

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.cinedispatch.R
import com.example.cinedispatch.base.BaseFragment
import com.example.cinedispatch.databinding.FragmentSignUpBinding
import com.example.cinedispatch.ui.uistate.AuthUiState
import com.example.cinedispatch.utils.gone
import com.example.cinedispatch.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import io.github.muddz.styleabletoast.StyleableToast
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding>(FragmentSignUpBinding::inflate) {
    @Inject
    lateinit var sharedPreferences: SharedPreferences
    private val viewmodel by viewModels<SignUpViewmodel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodel.getInfo()
        with(binding){
            login.setOnClickListener {
                findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLogInFragment())
            }
            signUpbutton.setOnClickListener{
                val user=username.text.toString().trim()
                val mail=email.text.toString().trim()
                val pass=password.text.toString().trim()
                if(mail.isNotEmpty() && pass.isNotEmpty() && user.isNotEmpty()){
                    sharedPreferences.edit().putString("user",user).apply()
                    sharedPreferences.edit().putString("emaill",mail).apply()
                    viewmodel.saveUserData(mail, pass, user,
                        onSuccess = {
                            viewmodel.signup(mail,pass)
                        },
                        onFailure = { exception ->
                            StyleableToast.makeText(requireContext(),"Error saving data:$exception", R.style.customToast).show()
                        }
                    )
                }else{
                    StyleableToast.makeText(requireContext(),"Fields can not be empty", R.style.customToast).show()
                }
            }
        }
        observe()
    }
    fun observe(){
        viewmodel.alreadySignedUp.observe(viewLifecycleOwner){
            if(it){
                findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToHomeFragment())
            }
        }
        viewmodel.uistate.observe(viewLifecycleOwner){
            when(it){
                is AuthUiState.Error->{
                    StyleableToast.makeText(requireContext(),it.message, R.style.customToast).show()
                    binding.lottie.gone()
                }
                is AuthUiState.Success->{
                    StyleableToast.makeText(requireContext(),"Registered Successfully", R.style.customToast).show()
                    findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToHomeFragment())
                    binding.lottie.gone()
                }
                is AuthUiState.Loading->{
                    binding.lottie.visible()
                }
            }
        }
    }
}