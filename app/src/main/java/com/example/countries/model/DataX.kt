package com.example.countries.model

// data class for country details
data class DataX(
    val callingCode: String?,
    val capital: String?,
    val code: String?,
    val currencyCodes: List<String>?,
    val flagImageUri: String?,
    val name: String?,
    val numRegions: Int?,
    val wikiDataId: String?
)