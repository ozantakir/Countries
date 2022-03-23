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
        val db= Room.databaseBuilder(getApplication(), CountriesDatabase::class.java,"countries").build()
        val countriesDao = db.countriesDao()
        roomRepo = RoomRepo(countriesDao)
    }

    private val _pageLiveData = MutableLiveData<Countries>()
    val pageLiveData : LiveData<Countries> = _pageLiveData

    private val _savedLiveData = MutableLiveData<List<RoomModel>>()
    val savedLiveData : LiveData<List<RoomModel>> = _savedLiveData

    fun getCountries(page : Int){
        viewModelScope.launch {
            val response = repo.getCountries(page)
            if(response != null){
                _pageLiveData.postValue(response!!)
            }

        }
    }

    fun getAllFromDb(){
        viewModelScope.launch {
            val resp = roomRepo.getAll()
            if(resp != null){
                _savedLiveData.postValue(resp!!)
            }
        }
    }

    fun saveCountry(roomModel: RoomModel){
        viewModelScope.launch {
            roomRepo.save(roomModel)
        }
    }

    fun deleteCountry(countryCode: String){
        viewModelScope.launch {
            roomRepo.delete(countryCode)
        }
    }
}