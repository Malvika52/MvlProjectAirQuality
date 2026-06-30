package com.example.mvlprojectairquality.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mvlprojectairquality.presentation.BookViewModel
import com.example.mvlprojectairquality.presentation.MapsViewModel

@Composable
fun LocationEditScreen(bookViewModel: BookViewModel,
                       navController: NavController,
                       label  : String) {



    Column(modifier = Modifier.fillMaxSize()) {
        TopArea()
        CentralContentArea(modifier = Modifier.weight(1f), label,
            navController = navController, bookViewModel = bookViewModel)
        BottomArea()
    }
}


@Composable
fun CentralContentArea(modifier: Modifier = Modifier, label : String,
                       bookViewModel: BookViewModel,
                       navController: NavController) {

    var nickname by rememberSaveable {
        mutableStateOf("")
    }

    val aDetails by bookViewModel.aDetails.collectAsState()
    val bDetails by bookViewModel.bDetails.collectAsState()

    Box( modifier = modifier.fillMaxWidth()){
        Column(modifier = Modifier.fillMaxWidth().background(Color.White)) {
            Row() {
                Text(if(label == "A"){
                    "A"
                }else{
                    "B"
                }, style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ), modifier = Modifier.padding(20.dp))

                Text(if(label == "A"){
                    aDetails?.locationName ?: ""
                }else{
                   bDetails?.locationName ?: ""
                }, style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ), modifier = Modifier.padding(20.dp))
            }

            Row() {
                Text("AQI", style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ), modifier = Modifier.padding(20.dp))

                Text(if(label == "A"){
                    aDetails?.aqi?.toString() ?: "-1"
                }else{
                    bDetails?.aqi?.toString() ?: "-1"
                }, style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ), modifier = Modifier.padding(20.dp))
            }

            Row(){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {

                    OutlinedTextField(
                        value = nickname,
                        onValueChange = { nickname = it },
                        label = {
                            Text("Nickname")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = nickname.isNotEmpty() && nickname.length < 20
                    )

                    if (nickname.isNotEmpty() && nickname.length < 20) {
                        Text(
                            text = "Nickname must be at least 20 characters",
                            color = Color.Red,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            Log.d("NICK", "Submitting nickname = '$nickname'")
                            bookViewModel.editNickName(label, nickname){
                                navController.popBackStack()
                            }

                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled =nickname.trim().length >= 20
                    ) {
                        Text("Submit")

                    }
                }
            }



        }
    }




}


@Composable
private fun TopArea() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(47.dp)
            .background(Color.Cyan)
    )
}
@Composable
private fun BottomArea() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(34.dp)
            .background(Color.Cyan)
    ){

    }
}