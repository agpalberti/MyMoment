package com.agp.mymoment.navigation

sealed class Destinations(val ruta: String) {
    object RegisterScreen: Destinations("RegisterScreen")
    object StartScreen: Destinations("StartScreen")
}