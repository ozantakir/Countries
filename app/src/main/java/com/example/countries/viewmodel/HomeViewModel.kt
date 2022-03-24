package com.example.countries.viewmodel
import android.app.Application
import androidx.lifecycle.*
import androidx.room.Room
import com.example.countries.model.Countries
import com.example.countries.model.RoomModel
import com.example.countries.repo.ApiRepo
import com.example.countries.repo.RoomRepo
import com.example.countries.roomdb.CountriesDatabase
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = ApiRepo()
    private val roomRepo : RoomRepo

    init {
        // initializing database
        val db= Room.databaseBuilder(getApplication(), CountriesDatabase::class.java,"countries").build()
        val countriesDao = db.countriesDao()
        roomRepo = RoomRepo(countriesDao)
    }
    // live data for the countries from api
    private val _pageLiveData = MutableLiveData<Countries>()
    val pageLiveData : LiveData<Countries> = _pageLiveData

    // live data for the countries from database
    private val _savedLiveData = MutableLiveData<List<RoomModel>>()
    val savedLiveData : LiveData<List<RoomModel>> = _savedLiveData

    // to get countries from api
    fun getCountries(page : Int,search : String?){
        viewModelScope.launch {
            val response = repo.getCountries(page, search = search)
            if(response != null){
                _pageLiveData.postValue(response!!)
            }
        }
    }
    // to get countries from database
    fun getAllFromDb(){
        viewModelScope.launch {
            val resp = roomRepo.getAll()
            if(resp != null){
                _savedLiveData.postValue(resp!!)
            }
        }
    }

    // to save a country to database
    fun saveCountry(roomModel: RoomModel){
        viewModelScope.launch {
            roomRepo.save(roomModel)
        }
    }
    // delete a country from database
    fun deleteCountry(countryCode: String){
        viewModelScope.launch {
            roomRepo.delete(countryCode)
        }
    }
}