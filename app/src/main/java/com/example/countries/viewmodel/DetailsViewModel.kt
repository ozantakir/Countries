package com.example.countries.viewmodel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countries.model.Details
import com.example.countries.repo.ApiRepo
import kotlinx.coroutines.launch

class DetailsViewModel : ViewModel() {
    private val repo = ApiRepo()

    private val _pageLiveData = MutableLiveData<Details>()
    val pageLiveData : LiveData<Details> = _pageLiveData

    fun getCountryDetails(id: String){
        viewModelScope.launch {
            val response = repo.getCountryDetails(id)
            _pageLiveData.postValue(response!!)
        }
    }
}