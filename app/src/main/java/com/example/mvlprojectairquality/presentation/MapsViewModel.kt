package com.example.mvlprojectairquality.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvlprojectairquality.model.Book
import com.example.mvlprojectairquality.data.repository.ReverseGeocodeResponse
import com.example.mvlprojectairquality.domain.MapsRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(private val repositoryInterface: MapsRepositoryInterface)  :
    ViewModel() {
        private val _reverseGeocodeResponse =
            MutableStateFlow<ReverseGeocodeResponse?>(null)
    val reverseGeocodeResponse = _reverseGeocodeResponse.asStateFlow()

    private val _aqiDetails =
        MutableStateFlow<Int?>(null)
    val aqiDetails = _aqiDetails.asStateFlow()


    private val _bookList =
        MutableStateFlow<List<Book>>(emptyList())
    val bookList = _bookList.asStateFlow()


    fun reverseGeocode(lat : Double, long : Double)  {
        viewModelScope.launch {
            _reverseGeocodeResponse.value =
                repositoryInterface.reverseGeocode(lat, long)
        }

    }

    fun getAqiDetails(lat : Double, long : Double){
        viewModelScope.launch {
            _aqiDetails.value = repositoryInterface.fetchAqiDetails(
                lat,
                long
            )
        }
    }


}