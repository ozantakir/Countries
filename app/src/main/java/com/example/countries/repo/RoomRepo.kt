package com.example.countries.repo

import com.example.countries.model.RoomModel
import com.example.countries.roomdb.CountriesDao

class RoomRepo(private val dao: CountriesDao) {

    suspend fun getAll(): List<RoomModel>? {
        return dao.getAll()
    }

    suspend fun save(roomModel: RoomModel){
        dao.save(roomModel)
    }

    suspend fun delete(countryCode: String){
        dao.delete(countryCode)
    }
}