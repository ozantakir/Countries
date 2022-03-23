package com.example.countries.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countries")
class RoomModel (
    @ColumnInfo(name = "code")
    var code: String,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "link")
    var link: String,
        ) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}