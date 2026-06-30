package com.example.mvlprojectairquality.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mvlprojectairquality.model.Book
import com.example.mvlprojectairquality.presentation.BookViewModel
import com.example.mvlprojectairquality.presentation.MapsViewModel

@Composable
fun FullBookListScreen(navController: NavController, bookViewModel: BookViewModel){

    LaunchedEffect(Unit) {
        bookViewModel.fetchBookings()
    }
    val bookings by bookViewModel.bookingList.collectAsState()
    val allLocations = bookings.flatMap {

        listOf(
            it.a,
            it.b
        )

    }.mapIndexed { index, book ->

        val label = ('A' + index).toString()

        book.copy(label = label)
    }
    val totalCount = allLocations.size
    val totalPrice = bookings.sumOf {

        it.price

    }


    Column() {
        TopArea()

        SummarySection( modifier = Modifier.fillMaxWidth().height(96.dp),
            totalCount = totalCount, totalPrice = totalPrice)
        Box(modifier = Modifier.fillMaxWidth().height(25.dp).background(Color.LightGray))
        ListScreen(allLocations)
        BottomArea()
    }





}


@Composable
fun ListScreen(bookList : List<Book>){
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.height(700.dp).fillMaxWidth()
    ) {
        items(bookList) { bookItem ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(20.dp),
                ) {
                Text( text = bookItem.label,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold)
                Text( text = bookItem.locationName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold)

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
    ) {

    }
}

@Composable
fun SummarySection(
    totalCount: Int,
    totalPrice: Double,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        // Labels
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = SpaceBetween
        ) {
            Text(
                text = "Total Count",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Total Price",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Values
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = SpaceBetween
        ) {
            Text(text = totalCount.toString(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold)

            Text(text = "₹$totalPrice", fontSize = 24.sp,
                fontWeight = FontWeight.Bold)
        }
    }
}