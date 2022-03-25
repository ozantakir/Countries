package com.example.countries.service

import com.example.countries.model.Countries
import com.example.countries.model.Details
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private const val baseUrl = "https://wft-geo-db.p.rapidapi.com/v1/geo/"
    private fun getInstance() : Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val service : CountriesAPI by lazy {
        getInstance().create(CountriesAPI::class.java)
    }

    // for all countries
    suspend fun getCountries(page : Int) : Response<Countries>{
        return service.getCountries(page = page)
    }
    // for country details
    suspend fun getCountryDetails(countryid: String) : Response<Details>{
        return service.getCountryDetails(countryid)
    }

}