package com.example.countries.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.countries.databinding.FragmentCountryDetailsBinding
import com.example.countries.model.Details
import com.example.countries.service.RetrofitHelper
import com.example.countries.viewmodel.DetailsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CountryDetailsFragment : Fragment() {

    private lateinit var viewModel : DetailsViewModel
    private var detailsModel : Details? = null
    private var _binding : FragmentCountryDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCountryDetailsBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = ViewModelProvider(this)[DetailsViewModel::class.java]
        viewModel.getCountryDetails("US")
        viewModel.pageLiveData.observe(viewLifecycleOwner){response ->
            if(response != null){
                detailsModel = response
                binding.deneme.text = detailsModel!!.data?.capital
            }
        }






        // Inflate the layout for this fragment
        return view
    }


}