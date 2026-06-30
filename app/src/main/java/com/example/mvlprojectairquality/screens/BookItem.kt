package com.example.mvlprojectairquality.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mvlprojectairquality.model.Book

@Composable
fun BookItem(bookItem : Book){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {

        // Top Row
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = bookItem.label,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = bookItem.locationName,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // AQI Row
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "AQI",
                modifier = Modifier.weight(1f),
                color = Color.Gray
            )

            Text(
                text = bookItem.aqi.toString(),
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Nickname Row
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "Nickname",
                modifier = Modifier.weight(1f),
                color = Color.Gray
            )

            Text(
                text = bookItem.nickname,
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}