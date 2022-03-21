package com.example.countries.viewmodel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countries.model.Countries
import com.example.countries.repo.ApiRepo
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val repo = ApiRepo()

    private val _pageLiveData = MutableLiveData<Countries>()
    val pageLiveData : LiveData<Countries> = _pageLiveData

    fun getCountries(){
        viewModelScope.launch {
            val response = repo.getCountries()
            _pageLiveData.postValue(response!!)
        }
    }
}