package com.example.mvlprojectairquality.data.retrofit

import com.example.mvlprojectairquality.data.repository.ReverseGeocodeResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitApiService {

    @GET("data/reverse-geocode-client")
    suspend fun reverseGeocode(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("localityLanguage") localityLanguage: String = "en"
    ): ReverseGeocodeResponse
}