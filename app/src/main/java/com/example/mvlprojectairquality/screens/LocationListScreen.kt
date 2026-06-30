package com.example.mvlprojectairquality.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mvlprojectairquality.Screens
import com.example.mvlprojectairquality.model.Book
import com.example.mvlprojectairquality.presentation.BookViewModel
import com.example.mvlprojectairquality.presentation.MapsViewModel


@Composable
fun LocationListScreen(navController: NavController,
                       bookViewModel : BookViewModel) {



    val bookList by bookViewModel.bookList.collectAsState()
    val price by bookViewModel.price.collectAsState()


    Column(modifier = Modifier.fillMaxSize()) {
        TopArea()
        CentraListScreen(
            modifier = Modifier.weight(1f),
            bookList,
            price,
            navController

        )
        BottomArea()
    }
}


@Composable
fun CentraListScreen(modifier: Modifier, bookList: List<Book>, price : Double,
                     navController: NavController) {



    Column(modifier = modifier.fillMaxWidth()) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.height(700.dp)
            ) {

            items(bookList) { bookItem ->
                BookItem(bookItem)
            }
        }

        Row(horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().height(64.dp).padding(20.dp)
            ){
            Text(
                text = "Price",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )

            Text(
                text = "$price",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

        }

        Button(onClick = {
            navController.navigate(Screens.FullLocationListScreen.route)

        },  modifier = Modifier.width(358.dp)
            .height(64.dp).align(Alignment.CenterHorizontally),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Yellow,
                contentColor = Color.Black
            )) {
            Text("Submit")
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
    ) {

    }
}