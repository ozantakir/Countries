package com.example.countries.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.countries.model.RoomModel

@Dao
interface CountriesDao {
    // to get all saved countries
    @Query("SELECT * FROM countries")
    suspend fun getAll() : List<RoomModel>
    // to save a country
    @Insert
    suspend fun save(country: RoomModel)
    // to delete a country
    @Query("DELETE FROM countries WHERE code = :countryCode")
    suspend fun delete(countryCode: String)
}