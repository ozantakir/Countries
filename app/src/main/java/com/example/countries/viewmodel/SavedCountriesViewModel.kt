package com.example.countries.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.countries.model.RoomModel
import com.example.countries.repo.RoomRepo
import com.example.countries.roomdb.CountriesDatabase
import kotlinx.coroutines.launch

class SavedCountriesViewModel(application: Application) : AndroidViewModel(application) {

    private val repo : RoomRepo

    init {
        // initializing database
        val db= Room.databaseBuilder(getApplication(), CountriesDatabase::class.java,"countries").build()
        val countriesDao = db.countriesDao()
        repo = RoomRepo(countriesDao)
    }
    // live data for the countries from database
    private val _pageLiveData = MutableLiveData<List<RoomModel>>()
    val pageLiveData : LiveData<List<RoomModel>> = _pageLiveData

    // to get all countries from database
    fun getAll(){
        viewModelScope.launch {
            val resp = repo.getAll()
            if(resp != null){
                _pageLiveData.postValue(resp!!)
            }
        }
    }

    // to delete a country from database
    fun deleteCountry(code: String) {
        viewModelScope.launch {
            repo.delete(code)
        }
    }
}