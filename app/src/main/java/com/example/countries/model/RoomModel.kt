package com.example.countries.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countries")
class RoomModel (
    // country code
    @ColumnInfo(name = "code")
    var code: String,
    // country name
    @ColumnInfo(name = "name")
    var name: String,
    // last part of link for wikipedia page
    @ColumnInfo(name = "link")
    var link: String,
        ) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}