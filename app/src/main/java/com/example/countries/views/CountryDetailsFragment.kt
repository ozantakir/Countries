package com.example.countries.views
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.load
import coil.request.ImageRequest
import com.example.countries.R
import com.example.countries.databinding.FragmentCountryDetailsBinding
import com.example.countries.model.Details
import com.example.countries.model.RoomModel
import com.example.countries.viewmodel.DetailsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CountryDetailsFragment : Fragment() {

    private lateinit var viewModel : DetailsViewModel
    private var detailsModel : Details? = null
    private var _binding : FragmentCountryDetailsBinding? = null
    private val binding get() = _binding!!
    private val args : CountryDetailsFragmentArgs by navArgs<CountryDetailsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCountryDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        val data = args.code
        val ct = args.country
        val isSaved = args.isSaved
        val link = args.link

        binding.checkbox.isChecked = isSaved

        viewModel = ViewModelProvider(this)[DetailsViewModel::class.java]
        viewModel.getCountryDetails(data!!,binding.flagImage)
        viewModel.pageLiveData.observe(viewLifecycleOwner){response ->
            if(response != null){
                detailsModel = response
//                val imageUrl = detailsModel?.data?.flagImageUri
//                val imageLoader = ImageLoader.Builder(requireContext())
//                    .components {
//                        add(SvgDecoder.Factory())
//                    }
//                    .build()
//                val request = ImageRequest.Builder(requireContext())
//                    .data(imageUrl)
//                    .crossfade(true)
//                    .target(binding.flagImage)
//                    .build()
//                imageLoader.enqueue(request)
            }
        }

        binding.countryName.text = ct
        binding.countryCode.text = data
        val url = "https://www.wikidata.org/wiki/${link}"
        binding.moreButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        binding.checkbox.setOnCheckedChangeListener { button, isChecked ->
            if(ct != null && link != null){
                val model = RoomModel(name = ct, code = data, link = link)
                if(isChecked){
                    viewModel.saveCountry(model)
                } else {
                    viewModel.deleteCountry(data)
                }
            }
        }
        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

     fun loadImage() {
        CoroutineScope(Dispatchers.IO).launch {

        }
    }


}