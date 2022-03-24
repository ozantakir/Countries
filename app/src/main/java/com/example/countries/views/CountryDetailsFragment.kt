package com.example.countries.views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.countries.databinding.FragmentCountryDetailsBinding
import com.example.countries.model.Details
import com.example.countries.model.RoomModel
import com.example.countries.viewmodel.DetailsViewModel

class CountryDetailsFragment : Fragment() {

    private lateinit var viewModel : DetailsViewModel
    private var detailsModel : Details? = null
    private var _binding : FragmentCountryDetailsBinding? = null
    private val binding get() = _binding!!
    // to get arguments from home/savedcountries page
    private val args : CountryDetailsFragmentArgs by navArgs<CountryDetailsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCountryDetailsBinding.inflate(inflater, container, false)
        val view = binding.root

        // initializing view model
        viewModel = ViewModelProvider(this)[DetailsViewModel::class.java]

        // values from arguments
        val data = args.code // country code
        val ct = args.country  // country name
        val isSaved = args.isSaved  // whether it is saved or not
        val link = args.link // last part of wikipedia link

        // assigning the value from home page
        binding.checkbox.isChecked = isSaved

        // getting the country details and putting the flag image
        getData(data)

        // assigning values
        binding.countryName.text = ct
        binding.countryCode.text = data

        // intent for the wikipedia page of the country
        val url = "https://www.wikidata.org/wiki/${link}"
        binding.moreButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        // saving or deleting a country to database
        binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
            if(ct != null && link != null){
                val model = RoomModel(name = ct, code = data!!, link = link)
                if(isChecked){
                    viewModel.saveCountry(model)
                } else {
                    viewModel.deleteCountry(data)
                }
            }
        }
        return view
    }
    // getting country details
    private fun getData(data: String?){
        viewModel.getCountryDetails(data!!)
        viewModel.pageLiveData.observe(viewLifecycleOwner){response ->
            if(response != null){
                detailsModel = response

                // getting svg image and assigning it to ImageView
                val imageUrl = detailsModel?.data?.flagImageUri
                val imageLoader = ImageLoader.Builder(requireContext())
                    .crossfade(true)
                    .crossfade(500)
                    .components {
                        add(SvgDecoder.Factory())
                    }
                    .build()
                val request = ImageRequest.Builder(requireContext())
                    .data(imageUrl)
                    .listener(
                        onStart = {
                            binding.progressLoader.visibility = View.VISIBLE
                        },
                        onSuccess = { _, _ ->
                            binding.progressLoader.visibility = View.GONE
                        },
                    )
                    .target(binding.flagImage)
                    .build()
                imageLoader.enqueue(request)
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}