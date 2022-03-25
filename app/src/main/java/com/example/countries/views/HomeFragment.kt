package com.example.countries.views

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.CheckBox
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.countries.R
import com.example.countries.adapter.HomeRecyclerAdapter
import com.example.countries.databinding.FragmentHomeBinding
import com.example.countries.model.RoomModel
import com.example.countries.utils.Resource
import com.example.countries.viewmodel.HomeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeFragment : Fragment(), HomeRecyclerAdapter.MyOnClickListener, HomeRecyclerAdapter.SaveListener {

    companion object {
        const val QUERY_PAGE_SIZE = 10
    }
    private lateinit var viewModel : HomeViewModel
    //model for the countries from database
    private var savedModel : List<RoomModel>? = null
    private var recyclerAdapter : HomeRecyclerAdapter? = null
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // initializing view model
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        // assigning recycler view
        val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(context)
        binding.homeRecyclerView.layoutManager = layoutManager
        binding.homeRecyclerView.apply {
            addOnScrollListener(this@HomeFragment.scrollListener)
        }

        // making bottom navigation bar visible
        val navBar = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        navBar?.visibility = View.VISIBLE

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAllFromDb()
        viewModel.savedLiveData.observe(viewLifecycleOwner){
            if(it != null){
                savedModel = it
                viewModel.ct.observe(viewLifecycleOwner){ response ->
                    when(response) {
                        is Resource.Success -> {
                            hideProgressBar()
                            response.data.let { resp ->
                                recyclerAdapter = HomeRecyclerAdapter(savedModel!!,this,this)
                                binding.homeRecyclerView.adapter = recyclerAdapter
                                recyclerAdapter?.differ?.submitList(resp?.data?.toList())
                                binding.homeRecyclerView.scrollToPosition(recyclerAdapter?.differ?.currentList?.size!! -17)
                            }
                        }
                        is Resource.Error -> {
                            hideProgressBar()
                            response.message?.let { mes ->
                                Log.e(TAG, "An error occured : $mes")
                            }
                        }
                        is Resource.Loading -> {
                            showProgressBar()
                        }
                    }
                }
            }
        }
    }

    // on click listener for the check box in each row of home page
    // saving/deleting a country to database
    override fun click(pos: Int, isChecked: Boolean, item: CheckBox) {
        val name = recyclerAdapter?.differ?.currentList?.get(pos)?.name // country name
        val code = recyclerAdapter?.differ?.currentList?.get(pos)?.code // country code
        val link = recyclerAdapter?.differ?.currentList?.get(pos)?.wikiDataId // last part of wikipedia link
        if(name != null && code != null && link != null){
            if(isChecked){
                item.isChecked = isChecked
                val model = RoomModel(name = name, code = code, link = link)
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
        val code = recyclerAdapter?.differ?.currentList?.get(position)?.code // country code
        val country = recyclerAdapter?.differ?.currentList?.get(position)?.name // country name
        val link = recyclerAdapter?.differ?.currentList?.get(position)?.wikiDataId // last part of wikipedia link
        val check = item.isChecked
        val action = HomeFragmentDirections.actionHomeFragmentToCountryDetailsFragment(code = code, country = country, isSaved = check, link = link)
        Navigation.findNavController(requireView()).navigate(action)

        // hiding the bottom navigation bar when moving to details page
        val navBar = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        navBar?.visibility = View.GONE
    }

    // Pagination *********************

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }
    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private val scrollListener = object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible
                    && isScrolling
            if(shouldPaginate){
                viewModel.getCt()
                isScrolling = false
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}