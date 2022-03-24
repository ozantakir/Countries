package com.example.countries.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.countries.R
import com.example.countries.adapter.SavedCountriesRecyclerAdapter
import com.example.countries.databinding.FragmentSavedCountriesBinding
import com.example.countries.model.RoomModel
import com.example.countries.viewmodel.SavedCountriesViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class SavedCountriesFragment : Fragment(), SavedCountriesRecyclerAdapter.MyOnClickListener, SavedCountriesRecyclerAdapter.DeleteListener {

    private var _binding : FragmentSavedCountriesBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel : SavedCountriesViewModel
    private var recyclerAdapter : SavedCountriesRecyclerAdapter? = null
    var model : List<RoomModel>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSavedCountriesBinding.inflate(inflater, container, false)
        val view = binding.root

        // initializing view model
        viewModel = ViewModelProvider(this)[SavedCountriesViewModel::class.java]

        // making bottom navigation bar visible after details page
        val navBar = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        navBar?.visibility = View.VISIBLE

        // assigning recycler view
        val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(context)
        binding.savedRecyclerView.layoutManager = layoutManager

        // to get all saved countries from database
        getAll()

        return view
    }

    private fun getAll() {
        viewModel.getAll()
        viewModel.pageLiveData.observe(viewLifecycleOwner){response ->
            if(response != null){
                model = response
                recyclerAdapter = SavedCountriesRecyclerAdapter(model!!,this,this)
                binding.savedRecyclerView.adapter = recyclerAdapter
            }
        }
    }
    // on click listener for each row in saved countries page and navigation to details page
    override fun onClick(position: Int) {
        val code = model?.get(position)?.code
        val country = model?.get(position)?.name
        val link = model?.get(position)?.link
        val action = SavedCountriesFragmentDirections.actionSavedCountriesFragmentToCountryDetailsFragment(
            code = code, country = country, isSaved = true, link = link)
        Navigation.findNavController(requireView()).navigate(action)

        // hiding bottom navigation bar when moving to details page
        val navBar = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        navBar?.visibility = View.GONE
    }
    // delete icon listener in each row of details page to delete a country from database
    override fun deleteClick(pos: Int) {
        val code = model?.get(pos)?.code
        if(code != null){
            viewModel.deleteCountry(code)
            getAll()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}