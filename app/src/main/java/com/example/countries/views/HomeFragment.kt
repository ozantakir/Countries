package com.example.countries.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.countries.R
import com.example.countries.adapter.HomeRecyclerAdapter
import com.example.countries.databinding.FragmentHomeBinding
import com.example.countries.model.Data
import com.example.countries.viewmodel.HomeViewModel


class HomeFragment : Fragment(), HomeRecyclerAdapter.MyOnClickListener {

    private lateinit var viewModel : HomeViewModel
    private var countryModel : List<Data>? = null
    private var recyclerAdapter : HomeRecyclerAdapter? = null
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        getData()
        // Inflate the layout for this fragment
        return view


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(context)
        binding.homeRecyclerView.layoutManager = layoutManager
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getData(){
        viewModel.getCountries()
        viewModel.pageLiveData.observe(viewLifecycleOwner){ response ->
            if(response != null){
                countryModel = response.data
                countryModel?.let {
                    recyclerAdapter = HomeRecyclerAdapter(it,this)
                    binding.homeRecyclerView.adapter = recyclerAdapter
                }
            }
        }
    }

    override fun onClick(position: Int) {
        Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_countryDetailsFragment)
    }

}