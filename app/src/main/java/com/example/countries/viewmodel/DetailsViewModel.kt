package com.example.countries.viewmodel
import android.app.Application
import android.widget.ImageView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.*
import androidx.room.Room
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.countries.model.Details
import com.example.countries.model.RoomModel
import com.example.countries.repo.ApiRepo
import com.example.countries.repo.RoomRepo
import com.example.countries.roomdb.CountriesDatabase
import kotlinx.coroutines.launch

class DetailsViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = ApiRepo()
    private val roomRepo : RoomRepo

    init {
        val db= Room.databaseBuilder(getApplication(), CountriesDatabase::class.java,"countries").build()
        val countriesDao = db.countriesDao()
        roomRepo = RoomRepo(countriesDao)
    }

    private val _pageLiveData = MutableLiveData<Details>()
    val pageLiveData : LiveData<Details> = _pageLiveData

    fun getCountryDetails(id: String,image: ImageView){
        viewModelScope.launch {
            val response = repo.getCountryDetails(id)
            if(response != null){
                _pageLiveData.postValue(response)
                val imageUrl = response.data?.flagImageUri
                val imageLoader = ImageLoader.Builder(getApplication<Application>().applicationContext)
                    .components {
                        add(SvgDecoder.Factory())
                    }
                    .build()
                val request = ImageRequest.Builder(getApplication<Application>().applicationContext)
                    .data(imageUrl)
                    .crossfade(true)
                    .target(image)
                    .build()
                imageLoader.enqueue(request)
            }
        }
    }

    fun deleteCountry(code: String) {
        viewModelScope.launch {
            roomRepo.delete(code)
        }
    }

    fun saveCountry(roomModel: RoomModel){
        viewModelScope.launch {
            roomRepo.save(roomModel)
        }
    }
}