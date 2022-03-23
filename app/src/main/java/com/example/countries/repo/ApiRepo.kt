package com.example.countries.repo
import com.example.countries.model.Countries
import com.example.countries.model.Details
import com.example.countries.service.RetrofitHelper

class ApiRepo {

    suspend fun getCountries(page : Int) : Countries? {
        val request = RetrofitHelper.getCountries(page)
        if(request.isSuccessful){
            return request.body()!!
        }
        return null
    }

    suspend fun getCountryDetails(id: String) : Details? {
        val request = RetrofitHelper.getCountryDetails(id)
        if(request.isSuccessful) {
            return request.body()
        }
        return null
    }

}