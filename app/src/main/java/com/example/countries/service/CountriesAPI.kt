package com.example.countries.service

import com.example.countries.model.Countries
import com.example.countries.model.Details
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface CountriesAPI {

    /*
        - to get all countries in home page
        - limit is for the total countries per page
        - offset is for other countries in other pages
        - namePrefix is to search a country in home page
     */
    @Headers(
        "x-rapidapi-key: 439eb4589cmshdfe122a8eaf5270p177cebjsn2d3e23229085"
    )
    @GET("countries")
    suspend fun getCountries(
        @Query("limit") lim:Int = 10,
        @Query("offset") page:Int,
        @Query("namePrefix") search:String?
    ) : Response<Countries>

    // to get country details with country code
    @Headers(
        "x-rapidapi-key: 439eb4589cmshdfe122a8eaf5270p177cebjsn2d3e23229085"
    )
    @GET("countries/{code}")
    suspend fun getCountryDetails(
        @Path("code") code: String
    ) : Response<Details>
}

