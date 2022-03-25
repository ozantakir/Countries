package com.example.countries.viewmodel
import android.app.Application
import androidx.lifecycle.*
import androidx.room.Room
import com.example.countries.model.Countries
import com.example.countries.model.RoomModel
import com.example.countries.repo.ApiRepo
import com.example.countries.repo.RoomRepo
import com.example.countries.roomdb.CountriesDatabase
import com.example.countries.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = ApiRepo()
    private val roomRepo : RoomRepo

    // live data for the countries from api
    val ct : MutableLiveData<Resource<Countries>> = MutableLiveData()
    // page number (offset)
    private var pg = 0
    // response
    private var resp : Countries? = null

    init {
        getCt()
    }

    fun getCt() = viewModelScope.launch {
        ct.postValue(Resource.Loading())
        val response = repo.getCountries(pg)
        ct.postValue(handleCtResponse(response!!))
    }

    private fun handleCtResponse(response: Response<Countries>) : Resource<Countries>{
        if(response.isSuccessful){
            response.body()?.let { resultResp ->
                pg += 10
                if(resp == null){
                    resp = resultResp
                } else {
                    val oldCt = resp?.data
                    val newCt = resultResp.data
                    oldCt?.addAll(newCt)
                }
                return Resource.Success(resp ?: resultResp)
            }
        }
        return Resource.Error(response.message())
    }


    init {
        // initializing database
        val db= Room.databaseBuilder(getApplication(), CountriesDatabase::class.java,"countries").build()
        val countriesDao = db.countriesDao()
        roomRepo = RoomRepo(countriesDao)
    }


    // live data for the countries from database
    private val _savedLiveData = MutableLiveData<List<RoomModel>>()
    val savedLiveData : LiveData<List<RoomModel>> = _savedLiveData


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