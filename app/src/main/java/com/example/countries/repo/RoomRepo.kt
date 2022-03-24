package com.example.countries.repo

import com.example.countries.model.RoomModel
import com.example.countries.roomdb.CountriesDao

class RoomRepo(private val dao: CountriesDao) {

    // to get all saved countries in saved countries page
    suspend fun getAll(): List<RoomModel>? {
        return dao.getAll()
    }

    // to save a country to room database
    suspend fun save(roomModel: RoomModel){
        dao.save(roomModel)
    }

    // to delete the saved country with country code
    suspend fun delete(countryCode: String){
        dao.delete(countryCode)
    }
}