package com.example.mvlprojectairquality.screens

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mvlprojectairquality.Screens
import com.example.mvlprojectairquality.fetchCurrentLocation
import com.example.mvlprojectairquality.model.Book
import com.example.mvlprojectairquality.presentation.BookViewModel
import com.example.mvlprojectairquality.presentation.MapsViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import okhttp3.Address

@Composable
fun MapScreen(
    mapsViewModel: MapsViewModel = hiltViewModel(),
    bookViewModel: BookViewModel,
    navController: NavController
) {


    val reverseGeocodeResponse = mapsViewModel.reverseGeocodeResponse.collectAsState()
    val aqi by mapsViewModel.aqiDetails.collectAsState()

    val context = LocalContext.current

    var hasLocationPermission by remember {
        mutableStateOf(false)
    }


    var address by remember {
        mutableStateOf("Move map to select location")
    }

    val singapore = remember {
        LatLng(1.35, 103.87)
    }
    val cameraPositionState =
        rememberCameraPositionState {
            position = CameraPosition
                .fromLatLngZoom(
                    singapore,
                    14f
                )
        }

    var currentLocation by remember {
        mutableStateOf<Location?>(null)
    }

    var selectedMarkerLocation by remember {
        mutableStateOf<LatLng?>(null)
    }

    val locationPermissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->

            hasLocationPermission = granted

            if (
                granted && ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                fetchCurrentLocation(
                    context = context,
                    onLocationFetched = {
                        currentLocation = it
                    }
                )
            }
        }


    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    LaunchedEffect(currentLocation) {

        currentLocation?.let {

            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        it.latitude,
                        it.longitude
                    ),
                    16f
                ),
                durationMs = 1000
            )
        }
    }

    LaunchedEffect(cameraPositionState) {

        snapshotFlow {
            cameraPositionState.isMoving
        }
            .distinctUntilChanged()
            .filter { !it }
            .collect {

                val target =
                    cameraPositionState.position.target

                try {

                    mapsViewModel.reverseGeocode(
                        target.latitude,
                        target.longitude
                    )

                    Log.i("Malvika", "Address = $address")

                } catch (e: Exception) {
                    address = "Unable to fetch address"
                }
            }
    }

    LaunchedEffect(reverseGeocodeResponse.value) {
        reverseGeocodeResponse.value?.let { response ->
            address = buildString {
                response.locality?.let { append(it) }
                response.city?.let { append(", $it") }
                response.principalSubdivision?.let { append(", $it") }
                response.countryName?.let { append(", $it") }
            }

            Log.i("Malvika", "Address = $address")
        }
    }



    Column(modifier = Modifier.fillMaxSize()) {
        TopArea(selectedMarkerLocation = selectedMarkerLocation)
        MapContentArea(
            modifier = Modifier.weight(1f),
            hasLocationPermission = hasLocationPermission,
            cameraPositionState = cameraPositionState,
            markerPosition = selectedMarkerLocation
                ?: currentLocation?.let {
                    LatLng(it.latitude, it.longitude)
                }
                ?: singapore,
            aqi = aqi,
            onMapClick = { latLng ->
                selectedMarkerLocation = latLng

                mapsViewModel.getAqiDetails(
                    lat = latLng.latitude,
                    long = latLng.longitude
                )
            },
            onMarkerLocationSelected = {
                selectedMarkerLocation = it
            },
            selectedmarkerLocation = selectedMarkerLocation,
            address = address,
            onBookListClick ={
                bookViewModel.book {
                    navController.navigate(Screens.LocationListScreen.route)
                }

            },
            onAClick = {
                navController.navigate(Screens.LocationEditScreen.createRoute(
                    "A"
                ))
            },
            onBClick = {
                navController.navigate(Screens.LocationEditScreen.createRoute("B"))
            },
            bookViewModel =  bookViewModel
        )
        BottomArea()
    }


}

@Composable
private fun TopArea(selectedMarkerLocation: LatLng?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(47.dp)
            .background(Color.Cyan)
    ) {
        Text(
            text = selectedMarkerLocation?.let {
                "Marker: %.5f, %.5f".format(it.latitude, it.longitude)
            } ?: "Tap marker to get location",
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 8.dp),
            color = Color.Black
        )
    }
}

@Composable
private fun MapContentArea(
    modifier: Modifier = Modifier,
    hasLocationPermission: Boolean,
    cameraPositionState: CameraPositionState,
    markerPosition: LatLng,
    aqi: Int?,
    onMapClick: (LatLng) -> Unit,
    onMarkerLocationSelected: (LatLng) -> Unit,
    bookViewModel: BookViewModel,
    selectedmarkerLocation: LatLng?,
    address: String,
    onBookListClick: () -> Unit,
    onAClick : () -> Unit,
    onBClick : () -> Unit
) {

    val aDetails by bookViewModel.aDetails.collectAsState()
    val bDetails by bookViewModel.bDetails.collectAsState()

    LaunchedEffect(aDetails) {
        Log.d("MALVIKA", "aDetails changed = $aDetails")
    }

    val markerState = remember {
        MarkerState(position = markerPosition)
    }

    LaunchedEffect(markerPosition) {
        markerState.position = markerPosition
    }

    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        val clickCount by bookViewModel.clickCount.collectAsState()
        GoogleMap(
            modifier = Modifier.fillMaxWidth(),
            cameraPositionState = cameraPositionState,

            properties = MapProperties(
                isMyLocationEnabled = hasLocationPermission
            ),
            onMapClick = { latLng ->
                onMapClick(latLng)
            }

//            uiSettings = MapUiSettings(
//                myLocationButtonEnabled = true, compassEnabled = true, tiltGesturesEnabled = true
//            )
        ) {
            Marker(
                state = markerState,
                title = "Selected location",
                onClick = { marker ->
                    onMarkerLocationSelected(marker.position)
                    false
                }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(Color.White)
                .align(Alignment.TopStart),
        ) {
            // add the aqi of the marker
            Text(
                text = "AQI : ${aqi ?: "--"}",
                color = Color.Black,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 10.dp),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                ),
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomEnd)
        ) {
            Row(
                modifier = Modifier
                    .height(168.dp)
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    verticalArrangement = Arrangement.spacedBy(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Button(
                        onClick = {
                           onAClick()
                        },
                        modifier = Modifier
                            .width(246.dp)
                            .height(64.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.LightGray,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(
                            text = when (clickCount) {
                                0 -> "A"
                                else ->{
                                Log.i("Malvika in A", "Details = ${ aDetails?.nickname}")
                                    val displayName = if (aDetails?.nickname.isNullOrBlank()) {
                                        aDetails?.locationName.orEmpty()
                                    } else {
                                        aDetails!!.nickname
                                    }
                                    displayName
                                }
                            },
                            color = Color.Black
                        )
                    }
                    Button(
                        onClick = {
                            onBClick()
                        }, modifier = Modifier
                            .width(246.dp)
                            .height(64.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.LightGray,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(
                            text = when (clickCount) {
                                0 -> "B"
                                1 -> "B"
                                else ->{
                                   // Log.i("Malvika in B", "Details = ${ bookViewModel.bDetails.collectAsState().value!!.locationName}")
                                    if(bDetails?.nickname!!.isBlank()){bDetails?.locationName ?: ""}
                                    else{
                                        bDetails?.nickname!!
                                    }
                                }

                            },
                            color = Color.Black
                        )
                    }
                }

                Column() {
                    Button(
                        onClick = {

                            when (clickCount) {
                                0 -> {
                                    val lat = selectedmarkerLocation?.latitude
                                    val long = selectedmarkerLocation?.longitude
                                    bookViewModel.saveA(
                                        Book(
                                            locationName = address,
                                            aqi = aqi,
                                            nickname = "",
                                            lat = lat,
                                            long = long,
                                            label = "A"
                                        )
                                    )
                                    bookViewModel.onBookButtonClicked()

                                }

                                1 -> {
                                    val lat = selectedmarkerLocation?.latitude
                                    val long = selectedmarkerLocation?.longitude
                                    bookViewModel.saveB(
                                        Book(
                                            locationName = address,
                                            aqi = aqi,
                                            nickname = "",
                                            lat = lat,
                                            long = long,
                                            label = "B"
                                        )
                                    )
                                    bookViewModel.onBookButtonClicked()
                                }

                                2 -> {
                                    bookViewModel.fetchBookList()
                                    Log.i("Malvika", "BookList1133 = ${bookViewModel.bookList}")
                                    onBookListClick()
                                }

                            }

                        }, modifier = Modifier
                            .width(96.dp)
                            .height(136.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Yellow,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text =
                                when (clickCount) {
                                    0 -> "Set A"
                                    1 -> "Set B"
                                    else -> "Book"
                                },
                            color = Color.Black
                        )
                    }
                }
            }
        }

    }

}

@Composable
private fun BottomArea() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(34.dp)
            .background(Color.Cyan)
    )
}
