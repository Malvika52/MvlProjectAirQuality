package com.example.mvlprojectairquality.model

data class Book(
    val locationName : String,
    val aqi  :Int?,
    val nickname : String ="",
    val lat : Double?,
    val long : Double?,
    val label : String
)
