package com.example.mvlprojectairquality

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mvlprojectairquality.presentation.BookViewModel
import com.example.mvlprojectairquality.screens.FullBookListScreen
import com.example.mvlprojectairquality.screens.LocationEditScreen
import com.example.mvlprojectairquality.screens.LocationListScreen
import com.example.mvlprojectairquality.screens.MapScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val bookViewModel: BookViewModel = hiltViewModel()
            NavHost(navController, Screens.MapScreen.route){
                composable(Screens.MapScreen.route) {
                    MapScreen(navController = navController,
                        bookViewModel = bookViewModel)
                }

                composable(Screens.LocationListScreen.route){
                    LocationListScreen(navController = navController, bookViewModel = bookViewModel)
                }

                composable(Screens.FullLocationListScreen.route){
                    FullBookListScreen(navController = navController, bookViewModel = bookViewModel)
                }

                composable(Screens.LocationEditScreen.route){
                    val label = it.arguments?.getString("label")
                    if(label!=null){
                        LocationEditScreen(navController = navController, label = label,
                            bookViewModel = bookViewModel)
                    }

                }
            }



        }
    }
}



