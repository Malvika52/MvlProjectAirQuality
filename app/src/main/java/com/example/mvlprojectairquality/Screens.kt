package com.example.mvlprojectairquality

sealed class Screens(val route: String) {
    object MapScreen : Screens("map_screen")
    object LocationEditScreen : Screens("edit_screen/{label}") {
        fun createRoute(label :String) = "edit_screen/$label"
    }
    object LocationListScreen : Screens("location_list")
    object FullLocationListScreen : Screens("full_list")



}