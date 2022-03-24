package com.example.countries.model

// data class for countries
data class Data(
    val code: String,
    val currencyCodes: List<String>,
    val name: String,
    val wikiDataId: String
)