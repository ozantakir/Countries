package com.example.countries.roomdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.countries.model.RoomModel
import retrofit2.Response

@Dao
interface CountriesDao {

    @Query("SELECT * FROM countries")
    suspend fun getAll() : List<RoomModel>

    @Insert
    suspend fun save(country: RoomModel)

    @Query("DELETE FROM countries WHERE code = :countryCode")
    suspend fun delete(countryCode: String)

}