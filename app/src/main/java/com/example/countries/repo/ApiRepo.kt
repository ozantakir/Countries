package com.example.countries.repo
import com.example.countries.model.Countries
import com.example.countries.model.Details
import com.example.countries.service.RetrofitHelper
import retrofit2.Response

class ApiRepo {

    // to get all countries in home page
    suspend fun getCountries(page : Int) : Response<Countries>? {
        val request = RetrofitHelper.getCountries(page)
        if(request.isSuccessful){
            return request
        }
        return null
    }

    // to get country details in details page
    suspend fun getCountryDetails(id: String) : Details? {
        val request = RetrofitHelper.getCountryDetails(id)
        if(request.isSuccessful) {
            return request.body()
        }
        return null
    }

}