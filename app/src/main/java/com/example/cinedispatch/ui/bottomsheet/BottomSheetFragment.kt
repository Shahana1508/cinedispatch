package com.example.cinedispatch.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.cinedispatch.R
import com.example.cinedispatch.databinding.FragmentBottomSheetBinding
import com.example.cinedispatch.ui.uistate.BottomSheetUiState
import com.example.cinedispatch.utils.loadFromUrl
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import io.github.muddz.styleabletoast.StyleableToast

@AndroidEntryPoint
class bottomSheetFragment : BottomSheetDialogFragment() {
    private val args:bottomSheetFragmentArgs by navArgs()
    private val viewModel by viewModels<bottomSheetViewModel>()
    private var _binding:FragmentBottomSheetBinding?=null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentBottomSheetBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id=args.id
        viewModel.getCastDetails(id.toInt())
        observe()
    }
    fun observe(){
        viewModel.bottomSheetUiState.observe(viewLifecycleOwner){
            when(it){
                is BottomSheetUiState.Error->{
                    StyleableToast.makeText(requireContext(),it.message, R.style.customToast).show()
                }
                is BottomSheetUiState.Success->{
                    binding.actorName.text=it.details.name
                    binding.actoricon.loadFromUrl(it.details.profilePath)
                    binding.biography.text=it.details.biography
                    binding.birtday.text=it.details.birthday
                    binding.department.text=it.details.knownForDepartment
                }
                is BottomSheetUiState.Loading->{

                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}