package com.example.mvlprojectairquality.data.retrofit

import com.example.mvlprojectairquality.model.AirQualityResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AirQualityApiService {

    @GET("feed/geo:{lat};{lon}/")
    suspend fun getAirQuality(
        @Path("lat") latitude: Double,
        @Path("lon") longitude: Double,
        @Query("token") token: String
    ): AirQualityResponse
}