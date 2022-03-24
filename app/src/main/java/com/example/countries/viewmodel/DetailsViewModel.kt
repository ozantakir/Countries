package com.example.countries.viewmodel
import android.app.Application
import androidx.lifecycle.*
import androidx.room.Room
import com.example.countries.model.Details
import com.example.countries.model.RoomModel
import com.example.countries.repo.ApiRepo
import com.example.countries.repo.RoomRepo
import com.example.countries.roomdb.CountriesDatabase
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DetailsViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = ApiRepo()
    private val roomRepo : RoomRepo

    init {
        // initializing database
        val db= Room.databaseBuilder(getApplication(), CountriesDatabase::class.java,"countries").build()
        val countriesDao = db.countriesDao()
        roomRepo = RoomRepo(countriesDao)
    }
    // live data for saved countries
    private val _pageLiveData = MutableLiveData<Details>()
    val pageLiveData : LiveData<Details> = _pageLiveData

    // to get all countries from database
    fun getCountryDetails(id: String){
        viewModelScope.launch {
            val response = repo.getCountryDetails(id)
            if(response != null){
                _pageLiveData.postValue(response!!)
                this.cancel()
            }
            delay(1500)
            val secondResponse = repo.getCountryDetails(id)
            if(secondResponse != null){
                _pageLiveData.postValue(secondResponse!!)
            }
        }
    }
    // delete country from database with country code
    fun deleteCountry(code: String) {
        viewModelScope.launch {
            roomRepo.delete(code)
        }
    }
    // save country to database
    fun saveCountry(roomModel: RoomModel){
        viewModelScope.launch {
            roomRepo.save(roomModel)
        }
    }
}