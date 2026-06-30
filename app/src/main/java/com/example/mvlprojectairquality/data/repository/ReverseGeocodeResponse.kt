package com.example.mvlprojectairquality.data.repository

data class ReverseGeocodeResponse(
    val continent : String?,
    val countryName : String?,
    val city  :String?,
    val locality : String?,
    val postCode : String?,
    val principalSubdivision: String?
)
