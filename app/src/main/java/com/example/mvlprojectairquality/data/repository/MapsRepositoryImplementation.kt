package com.example.mvlprojectairquality.data.repository

import android.util.Log
import com.example.mvlprojectairquality.model.Book
import com.example.mvlprojectairquality.data.retrofit.AirQualityApiService
import com.example.mvlprojectairquality.data.retrofit.RetrofitApiService
import com.example.mvlprojectairquality.domain.MapsRepositoryInterface
import com.google.gson.JsonSyntaxException
import jakarta.inject.Inject

class MapsRepositoryImplementation
@Inject constructor(private val apiService: RetrofitApiService,
    private val airQualityApiService: AirQualityApiService): MapsRepositoryInterface {
    override suspend fun reverseGeocode(latitude: Double, longitude: Double): ReverseGeocodeResponse {
       return apiService.reverseGeocode(latitude, longitude)
    }

    override suspend fun fetchAqiDetails(
        latitude: Double,
        longitude: Double
    ): Int {

//        val response = airQualityApiService.getAirQuality(latitude, longitude,
//            token = "c787a00b8baca22a76acf0e33ebb1e9e5a253a22"
//        ).string()
//
//        Log.d("WAQI_RESPONSE", response)
//        return 22



        return try {

            val response = airQualityApiService.getAirQuality(
                latitude,
                longitude,
                "c787a00b8baca22a76acf0e33ebb1e9e5a253a22"
            )

            response.data.aqi

        } catch (e: JsonSyntaxException) {
            Log.e("WAQI", "API returned an invalid response", e)
            -1 // or another default value
        }
    }
}