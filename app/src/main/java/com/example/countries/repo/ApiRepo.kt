package com.example.countries.repo
import com.example.countries.model.Countries
import com.example.countries.model.Details
import com.example.countries.service.RetrofitHelper

class ApiRepo {

    // to get all countries in home page
    suspend fun getCountries(page : Int,search : String? = null) : Countries? {
        val request = RetrofitHelper.getCountries(page,search = search)
        if(request.isSuccessful){
            return request.body()!!
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