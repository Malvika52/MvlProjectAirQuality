package com.example.mvlprojectairquality.domain

import com.example.mvlprojectairquality.model.Book
import com.example.mvlprojectairquality.data.repository.ReverseGeocodeResponse

interface MapsRepositoryInterface {

    suspend fun reverseGeocode(latitude : Double, longitude : Double) :
            ReverseGeocodeResponse

    suspend fun fetchAqiDetails(latitude : Double, longitude : Double) :
            Int

}