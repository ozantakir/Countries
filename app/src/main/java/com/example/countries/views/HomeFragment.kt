package com.example.countries.views

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.countries.R
import com.example.countries.adapter.HomeRecyclerAdapter
import com.example.countries.databinding.FragmentHomeBinding
import com.example.countries.model.Data
import com.example.countries.model.RoomModel
import com.example.countries.viewmodel.HomeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeFragment : Fragment(), HomeRecyclerAdapter.MyOnClickListener, HomeRecyclerAdapter.SaveListener {

    private lateinit var viewModel : HomeViewModel
    // model for the countries from api
    private var countryModel : List<Data>? = null
    //model for the countries from database
    private var savedModel : List<RoomModel>? = null
    private var recyclerAdapter : HomeRecyclerAdapter? = null
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    // page number, +10 means next page (offset)
    private var pageNum = 0
    private var searchText = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        // assigning recycler view
        val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(context)
        binding.homeRecyclerView.layoutManager = layoutManager

        // initializing view model
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        // making bottom navigation bar visible
        val navBar = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        navBar?.visibility = View.VISIBLE

        // at the start since it is first page there is no need for previous page button
        if(pageNum == 0){
            binding.previousPage.visibility = View.GONE
        }
        if(countryModel != null){
            if(countryModel!!.size < 10){
                binding.nextPage.visibility = View.GONE
            }
        }


        // loading the data from api
        getData(pageNum,searchText)

        // previous page button
        binding.previousPage.setOnClickListener {
            it.preventDoubleClick()
            if(pageNum >= 10){
                pageNum -= 10
                getData(pageNum,searchText)
            }
            when(pageNum){
              0 -> binding.previousPage.visibility = View.GONE
            }
        }

        // next page button
        binding.nextPage.setOnClickListener {
            it.preventDoubleClick()
            println(pageNum)
            if(pageNum <= 200 && countryModel!!.size == 10){
                pageNum += 10
                getData(pageNum,searchText)
                binding.previousPage.visibility = View.VISIBLE
            }
            binding.countryName.clearFocus()
        }

        // setting text listener for search view
        binding.countryName.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                pageNum = 0
                binding.previousPage.visibility = View.GONE
                getData(0,p0)
                if(p0 != null){
                    searchText = p0
                }
                Handler().postDelayed({
                    if(countryModel!!.size == 10){
                        binding.nextPage.visibility = View.VISIBLE
                    } else {
                        binding.nextPage.visibility = View.GONE
                    }
                },500)
                return false
            }
            // when all the search text deleted
            override fun onQueryTextChange(p0: String?): Boolean {
                when(p0){
                    "" -> {
                        getData(pageNum,"")
                        searchText = ""
                        binding.nextPage.visibility = View.VISIBLE
                    }
                }
                return false
            }
        })
        // when search view closed
        binding.countryName.setOnCloseListener {
            getData(pageNum, "")
            searchText = ""
            binding.nextPage.visibility = View.VISIBLE
            false
        }
        return view
    }

    // to get all countries from api
    fun getData(page : Int,search : String?){
        getDataFromDb()
        viewModel.getCountries(page,search)
        viewModel.pageLiveData.observe(viewLifecycleOwner){ response ->
            if(response != null  && savedModel != null){
                countryModel = response.data
                countryModel?.let {
                    recyclerAdapter = HomeRecyclerAdapter(it,savedModel!!,this,this)
                    binding.homeRecyclerView.adapter = recyclerAdapter
                }
            }
        }
    }
    // to get all countries from database
    private fun getDataFromDb(){
        viewModel.getAllFromDb()
        viewModel.savedLiveData.observe(viewLifecycleOwner){
            if(it != null){
                savedModel = it
            }
        }
    }
    // on click listener for the check box in each row of home page
    // saving/deleting a country to database
    override fun click(pos: Int, isChecked: Boolean, item: CheckBox) {
        val name = countryModel?.get(pos)?.name // country name
        val code = countryModel?.get(pos)?.code // country code
        val link = countryModel?.get(pos)?.wikiDataId // last part of wikipedia link
        if(name != null && code != null && link != null){
            if(isChecked){
                item.isChecked = isChecked
                val model = RoomModel(name = name, code = code, link = link,)
                viewModel.saveCountry(model)
            } else {
                item.isChecked = isChecked
                viewModel.deleteCountry(code)
            }
        }
    }
    // on click listener for each row in home page
    // action from home page to details page and sending data
    override fun onClick(position: Int, item: CheckBox) {
        val code = countryModel?.get(position)?.code // country code
        val country = countryModel?.get(position)?.name // country name
        val link = countryModel?.get(position)?.wikiDataId // last part of wikipedia link
        val check = item.isChecked
        val action = HomeFragmentDirections.actionHomeFragmentToCountryDetailsFragment(code = code, country = country, isSaved = check, link = link)
        Navigation.findNavController(requireView()).navigate(action)

        // hiding the bottom navigation bar when moving to details page
        val navBar = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        navBar?.visibility = View.GONE
    }

    // preventing double click for next/previous page buttons
    private fun View.preventDoubleClick() {
        this.isEnabled = false
        this.postDelayed( { this.isEnabled = true }, 1000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}