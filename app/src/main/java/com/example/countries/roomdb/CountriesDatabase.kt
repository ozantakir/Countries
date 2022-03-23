package com.example.countries.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.countries.model.RoomModel

@Database(entities = [RoomModel::class], version = 1)
abstract class CountriesDatabase : RoomDatabase() {
    abstract fun countriesDao() : CountriesDao
}