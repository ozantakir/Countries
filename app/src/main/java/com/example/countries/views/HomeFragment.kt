package com.example.countries.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
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
    private var countryModel : List<Data>? = null
    private var savedModel : List<RoomModel>? = null
    private var recyclerAdapter : HomeRecyclerAdapter? = null
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var pageNum = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val navBar = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        navBar?.visibility = View.VISIBLE
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        if(pageNum == 0){
            binding.previousPage.visibility = View.GONE
        }

        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        getData(pageNum)

        binding.previousPage.setOnClickListener {
            if(pageNum >= 10){
                pageNum -= 10
                getData(pageNum)
            }
            when(pageNum){
              0 -> binding.previousPage.visibility = View.GONE
            }
        }
        binding.nextPage.setOnClickListener(View.OnClickListener() {


        })


        binding.nextPage.setOnClickListener {
            if(binding.previousPage.visibility == View.GONE){
                binding.previousPage.visibility = View.VISIBLE
            }
            if(pageNum <= 180){
                pageNum += 10
                getData(pageNum)
            }
        }
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

    fun getData(page : Int){
        getDataFromDb()
        viewModel.getCountries(page)
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

    fun getDataFromDb(){
        viewModel.getAllFromDb()
        viewModel.savedLiveData.observe(viewLifecycleOwner){
            if(it != null){
                savedModel = it
            }
        }
    }




    override fun click(pos: Int, isChecked: Boolean, item: CheckBox) {
        val name = countryModel?.get(pos)?.name
        val code = countryModel?.get(pos)?.code
        val link = countryModel?.get(pos)?.wikiDataId
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

    override fun onClick(position: Int, item: CheckBox) {
        val code = countryModel?.get(position)?.code
        val country = countryModel?.get(position)?.name
        val link = countryModel?.get(position)?.wikiDataId
        val check = item.isChecked
        val action = HomeFragmentDirections.actionHomeFragmentToCountryDetailsFragment(code = code, country = country, isSaved = check, link = link)
        Navigation.findNavController(requireView()).navigate(action)
        val navBar = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        navBar?.visibility = View.GONE
    }


}