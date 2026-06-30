package com.example.mvlprojectairquality.di

import android.R.attr.level
import com.example.mvlprojectairquality.data.repository.MapsRepositoryImplementation
import com.example.mvlprojectairquality.data.repository.MockBookingRepository
import com.example.mvlprojectairquality.data.retrofit.AirQualityApiService
import com.example.mvlprojectairquality.data.retrofit.RetrofitApiService
import com.example.mvlprojectairquality.domain.BookRepository
import com.example.mvlprojectairquality.domain.MapsRepositoryInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }


    @Provides
    @Singleton
    fun provideRetrofitApi():  RetrofitApiService{
        val api: RetrofitApiService by lazy {
            Retrofit.Builder()
                .baseUrl("https://api-bdc.net")
                .addConverterFactory(
                    GsonConverterFactory.create())
                .build()
                .create(RetrofitApiService::class.java)
        }
        return  api
    }

    private const val BASE_URL = "https://api.waqi.info/"


    @Provides
    @Singleton
    fun provideRepoInterface(airQualityApiService: AirQualityApiService,
                             retrofitApiService: RetrofitApiService) : MapsRepositoryInterface{
        return MapsRepositoryImplementation(retrofitApiService, airQualityApiService)
    }

    @Provides
    @Singleton
    fun provideMockBookRepo() : BookRepository{
        return MockBookingRepository()
    }




    @Provides
    @Singleton
    fun provideAirQualityApi(okHttpClient: OkHttpClient): AirQualityApiService {

        val api: AirQualityApiService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AirQualityApiService::class.java)
        }

        return api

    }


}