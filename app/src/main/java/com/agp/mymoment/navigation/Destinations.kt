package com.agp.mymoment.navigation

sealed class Destinations(val ruta: String) {

    object generico: Destinations("Screen")
    object RegisterScreen: Destinations("RegisterScreen")
}